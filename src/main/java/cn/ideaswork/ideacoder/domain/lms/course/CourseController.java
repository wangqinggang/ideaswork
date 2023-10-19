package cn.ideaswork.ideacoder.domain.lms.course;

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

import org.apache.commons.lang3.StringUtils;
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

@RestController
@Api(
    tags = "课程 API"
)
@CrossOrigin
@RequestMapping("/courses")
public class CourseController {
  @Autowired
  CourseService courseService;

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("添加课程")
  public Course saveCourse(@RequestBody Course course) {
    course.setId(UUID.randomUUID().toString());
    course.setCzsj(new Date());
    return courseService.saveCourse(course);
  }

  @GetMapping
  @ApiOperation("获取 课程列表")
  public List<Course> getCourses() {
    return courseService.getAllCourses();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据主键获取一条 课程")
  public Course getCourse(@PathVariable("id") final String id) {
    return courseService.getCourseById(id);
  }

  @GetMapping("/hasCourse/{id}")
  @ApiOperation("获取当前用户是否拥有当前课程")
  public Boolean getIsHave(@PathVariable("id") final String id) {
    User loginUser = SysTools.getLoginUser();
    if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
      return false;
    }
    return courseService.hasCourse(id,loginUser);
  }


  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("根据主键更新课程")
  public Course updateCourse(@RequestBody Course course, @PathVariable("id") final String id) {
    return courseService.updateCourseById(course,id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("根据主键 id 删除课程")
  public void deleteCourseById(@PathVariable("id") final String id) {
    Course courseById = courseService.getCourseById(id);
    courseById.setIsPublished(false);
    courseService.updateCourseById(courseById,courseById.getId());
//    courseService.deleteCourseById(id);
  }

  @GetMapping("/isExist/{id}")
  @ApiOperation("根据主键 id 查看课程是否存在")
  public Boolean isExistCourse(@PathVariable("id") final String id) {
    return courseService.isCourseExist(id);
  }

  @GetMapping("/getPageList")
  @ApiOperation("分页条件查询")
  public Page<Course> getPageByCondition(CourseDTO courseDTO,
      @PageableDefault(value = 1, size = 20) Pageable pageable) {
    courseDTO.setIsPublished(true);
    return courseService.getPageByCondition(courseDTO ,pageable);
  }

  @GetMapping("/adminGetPageList")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("分页条件查询")
  public Page<Course> adminGetPageList(CourseDTO courseDTO,
      @PageableDefault(value = 1, size = 20) Pageable pageable) {
    return courseService.getPageByCondition(courseDTO ,pageable);
  }
}