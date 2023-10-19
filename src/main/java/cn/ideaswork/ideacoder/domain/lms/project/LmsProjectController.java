package cn.ideaswork.ideacoder.domain.lms.project;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@Api(tags = "项目 API")
@CrossOrigin()
@RequestMapping("/lmsProjects")
public class LmsProjectController {
    @Autowired
    LmsProjectService lmsProjectService;

    @PostMapping
    @ApiOperation("添加项目")
    public LmsProject saveProject(@RequestBody LmsProject project) {
        project.setId(UUID.randomUUID().toString());
        return lmsProjectService.saveProject(project);
    }

    @GetMapping
    @ApiOperation("获取 项目列表")
    public List<LmsProject> getProjects() {
        return lmsProjectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 项目")
    public LmsProject getProject(@PathVariable("id") final String id) {
        return lmsProjectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新项目")
    public LmsProject updateProject(@RequestBody LmsProject project, @PathVariable("id") final String id) {
        return lmsProjectService.updateProjectById(project, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除项目")
    public void deleteProjectById(@PathVariable("id") final String id) {
        lmsProjectService.deleteProjectById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看项目是否存在")
    public Boolean isExistProject(@PathVariable("id") final String id) {
        return lmsProjectService.isProjectExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<LmsProject> getPageByCondition(LmsProjectDTO projectDTO,
                                               @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return lmsProjectService.getPageByCondition(projectDTO, pageable);
    }
}