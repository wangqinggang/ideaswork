package cn.ideaswork.ideacoder.domain.vms.copy;

import cn.hutool.core.util.ObjectUtil;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.vms.topic.Topic;
import cn.ideaswork.ideacoder.domain.vms.topic.TopicDTO;
import cn.ideaswork.ideacoder.domain.vms.topic.TopicService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Api(tags = "文案 API")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/copys")
public class CopyController {
    @Autowired
    CopyService copyService;

    @Autowired
    TopicService topicService;

    @PostMapping
    @ApiOperation("添加文案")
    @Transactional
    public ResponseEntity saveCopy(@RequestBody Copy copy) throws Exception {
        User loginUser = SysTools.getLoginUser();
        String topicId = copy.getTopicId();
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("请先登录");
        }
        Integer copyCount = loginUser.getCopyCount();
        Integer actrualCount = copyService.getActrualCopyCountByTopicId(topicId);
        if (ObjectUtil.isNotEmpty(actrualCount) && actrualCount >= copyCount) {
            return ResponseEntity.badRequest().body("您当前主题的文案数量已经达到上限");
        }

        Topic topicDB = topicService.getTopicById(topicId);
        if (ObjectUtil.isNotEmpty(topicDB)) {
            copy.setTopicId(topicDB.getId());
            copy.setTopicName(topicDB.getName());
        } else {
            return ResponseEntity.badRequest().body("主题不存在");
        }
        copy.setUserId(loginUser.getId());
        copy.setId(UUID.randomUUID().toString());
        copy.setCjsj(LocalDate.now());
        copy.setFinishedScriptNum(0);
        copy.setScriptNum(0);
        if (StringUtils.isBlank(copy.getStatus())) {
            copy.setStatus("文案编辑中");
        }
        Copy copy1 = copyService.saveCopy(copy);
        updateTopicFinishedCopyNum(copy, topicDB);
        return ResponseEntity.ok(copy1);
    }

    /**
     * 更新主题中已经完成的文案数量
     *
     * @param copy    文案
     * @param topicDB 主题
     */
    private void updateTopicFinishedCopyNum(Copy copy, Topic topicDB) {
        CopyDTO copyDTO = new CopyDTO();
        copyDTO.setTopicId(copy.getTopicId());
        List<Copy> copyDBList = copyService.getListByCondition(copyDTO);
        copyDBList=copyDBList.stream().filter(one -> one.getStatus() != null ?!one.getStatus().equals("回收站"):true).collect(Collectors.toList());
        topicDB.setCopyNum(copyDBList.size());
        List<Copy> finishedCopyList = copyDBList.stream().filter(one -> one.getStatus() != null ? one.getStatus().equals("已完成") : false).collect(Collectors.toList());
        topicDB.setCopyFinishedNum(finishedCopyList.size());
        topicService.updateTopicById(topicDB, topicDB.getId());
    }

//  @GetMapping
//  @ApiOperation("获取 文案列表")
//  public List<Copy> getCopys() {
//    return copyService.getAllCopys();
//  }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 文案")
    public Copy getCopy(@PathVariable("id") final String id) {
        return copyService.getCopyById(id);
    }

    /**
     * 根据主键更新文案
     *
     * @param copy
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/{id}")
    @ApiOperation("根据主键更新文案")
    @Transactional
    public ResponseEntity updateCopy(@RequestBody Copy copy, @PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        Copy copyDB = copyService.getCopyById(id);
        if (!copyDB.getUserId().equals(userId)) {
            return ResponseEntity.badRequest().body("只能修改自己的文案");
        }
        Topic topicDB = topicService.getTopicById(copy.getTopicId());
        copy.setTopicName(topicDB.getName());
        Copy copy1 = copyService.updateCopyById(copy, id);
        updateTopicFinishedCopyNum(copy, topicDB);
        return ResponseEntity.ok(copy1);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除文案")
    @Transactional
    public ResponseEntity deleteCopyById(@PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("请先登录");
        }
        String userId = loginUser.getId();
        Copy copyDB = copyService.getCopyById(id);
        // 如果当前文案有脚本，提示先删除脚本
        if (copyDB.getScriptNum() > 0) {
            return ResponseEntity.badRequest().body("请先删除文案下所有脚本");
        }
        if (copyDB.getUserId().equals(userId)) {
            copyDB.setStatus("回收站");
//        copyService.deleteCopyById(id);
            copyService.updateCopyById(copyDB, copyDB.getId());
            Topic topicDB = topicService.getTopicById(copyDB.getTopicId());
            if (ObjectUtil.isNotEmpty(topicDB.getId())) {
                updateTopicFinishedCopyNum(copyDB, topicDB);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看文案是否存在")
    public Boolean isExistCopy(@PathVariable("id") final String id) {
        return copyService.isCopyExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Copy> getPageByCondition(CopyDTO copyDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        copyDTO.setUserId(userId);
        return copyService.getPageByCondition(copyDTO, pageable);
    }
}