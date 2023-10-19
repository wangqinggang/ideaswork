package cn.ideaswork.ideacoder.domain.coder.project;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@Api(tags = "工程管理 API")
@CrossOrigin()
@PreAuthorize("hasRole('ROLE_ADMIN')")
//@PreAuthorize("hasRole('ROLE_DOMAIN')")
//@Tag(name = "工程",description = "java 工程相关 crud 接口")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    @Operation(summary = "新增工程", description = "新建一个 java 工程")
    public ResponseEntity<?> saveProject(@RequestBody Project project) {
        project.setId(UUID.randomUUID().toString());
        project.setCreateTime(LocalDateTime.now());
        String[] split = project.getPackageGroup().split("\\.");
        if (split.length!=2){
            return ResponseEntity.badRequest().body("Java 分组不正确");
        }
        project.setPackageGroup(project.getPackageGroup().trim());
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @GetMapping
    @Operation(summary = "获取所有工程", description = "")
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/allProjectsByUserId/{userid}")
    @Operation(summary = "获取所有工程", description = "")
    public List<Project> getProjectsByUserId(@PathVariable("userid") String userid) {
        return projectService.getAllProjectsByUserId(userid);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id获取工程", description = "")
    public Project getProject(@PathVariable("id") String id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "根据id更新工程", description = "")
    public Project updateProject(@RequestBody Project project, @PathVariable("id") String id) {
        return projectService.updateProjectById(project, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "根据id删除工程", description = "")
    public void deleteProject(@PathVariable("id") String id) {
        projectService.deleteProjectById(id);
    }

    @GetMapping("/isExist/{id}")
    @Operation(summary = "根据id判断工程是否存在", description = "")
    public Boolean isExistProject(@PathVariable("id") String id) {
        return projectService.isProjectExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Project> getPageByCondition(Project project,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return projectService.getProjectPageListByCondition(project ,pageable);
    }

}
