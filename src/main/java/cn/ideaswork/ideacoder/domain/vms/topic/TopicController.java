package cn.ideaswork.ideacoder.domain.vms.topic;

import cn.hutool.core.util.ObjectUtil;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
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
@Api(
        tags = "主题 API"
)
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    TopicService topicService;

    @PostMapping
    @ApiOperation("添加主题")
    @Transactional
    public ResponseEntity saveTopic(@RequestBody Topic topic) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请先登录");
        }
        String userId = loginUser.getId();
        Integer topicCount = loginUser.getTopicCount();
        Integer actualTopicCount = topicService.checktopicCount(userId);
        if (ObjectUtil.isNotEmpty(actualTopicCount) && actualTopicCount >= topicCount) {
            return ResponseEntity.badRequest().body("您的当前文案数量已达上限");
        }

        topic.setUserId(userId);
        topic.setCjsj(new Date());
        topic.setCopyNum(0);
        topic.setCopyFinishedNum(0);
        if (StringUtils.isBlank(topic.getStatus())) {
            topic.setStatus("TODO");
        }

        topic.setId(UUID.randomUUID().toString());
        return ResponseEntity.ok(topicService.saveTopic(topic));
    }

//  @GetMapping
//  @ApiOperation("获取 主题列表")
//  public List<Topic> getTopics() {
//    return topicService.getAllTopics();
//  }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 主题")
    public Topic getTopic(@PathVariable("id") final String id) {
        return topicService.getTopicById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新主题")
    @Transactional
    public Topic updateTopic(@RequestBody Topic topic, @PathVariable("id") final String id) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new RuntimeException("请先登录");
        }
        String userId = loginUser.getId();
        topic.setUserId(userId);
        return topicService.updateTopicById(topic, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除主题")
    public ResponseEntity deleteTopicById(@PathVariable("id") final String id) throws Exception {
        // 检查是否登录
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("请先登录");
        }
        // 检查是否有权限
        Topic topic = topicService.getTopicById(id);
        if (!topic.getUserId().equals(loginUser.getId())) {
            return ResponseEntity.badRequest().body("没有权限");
        }
        // 检查是否有文案，有文案则删除失败
        if (topic.getCopyNum() > 0) {
            return ResponseEntity.badRequest().body("该主题下有文案或脚本，请先删除");
        }
        topicService.deleteTopicById(id);
        return ResponseEntity.ok().body("删除成功");
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看主题是否存在")
    public Boolean isExistTopic(@PathVariable("id") final String id) {
        return topicService.isTopicExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Topic> getPageByCondition(TopicDTO topicDTO,
                                          @PageableDefault(value = 1, size = 20) Pageable pageable) {
        User loginUser = SysTools.getLoginUser();
        topicDTO.setUserId(loginUser.getId());
        return topicService.getPageByCondition(topicDTO, pageable);
    }
}