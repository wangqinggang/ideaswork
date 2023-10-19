package cn.ideaswork.ideacoder.domain.pm.idea;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(
        tags = "创意 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/ideas")
@CrossOrigin()
public class IdeaController {
    @Autowired
    IdeaService ideaService;

    @PostMapping
    @ApiOperation("添加创意")
    public Idea saveIdea(@RequestBody Idea idea) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        idea.setUserid(loginUser.getId())
                .setUserName(loginUser.getName());
        idea.setId(UUID.randomUUID().toString());
        idea.setCreatetime(new Date());
        return ideaService.saveIdea(idea);
    }

    @GetMapping
    @ApiOperation("获取 创意列表")
    public List<Idea> getIdeas() {
        return ideaService.getAllIdeas();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 创意")
    public Idea getIdea(@PathVariable("id") final String id) {
        return ideaService.getIdeaById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新创意")
    public Idea updateIdea(@RequestBody Idea idea, @PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        Idea ideaById = ideaService.getIdeaById(id);
        if(ideaById.getUserid().equals(loginUser.getId())){
            if (idea.getCreatetime() == null) {
                idea.setCreatetime(new Date());
            }
            return ideaService.updateIdeaById(idea, id);
        }else{
            throw new Exception("只能修改自己创建的内容");
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除创意")
    public void deleteIdeaById(@PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        Idea ideaById = ideaService.getIdeaById(id);
        if(ideaById.getUserid().equals(loginUser.getId())){
            ideaService.deleteIdeaById(id);
        }else{
            throw new Exception("只能删除自己创建的内容");
        }

    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看创意是否存在")
    public Boolean isExistIdea(@PathVariable("id") final String id) {
        return ideaService.isIdeaExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Idea> getPageByCondition(IdeaDTO ideaDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        ideaDTO.setUserid(loginUser.getId());
        return ideaService.getPageByCondition(ideaDTO, pageable);
    }

}