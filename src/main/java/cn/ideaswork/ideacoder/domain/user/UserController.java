package cn.ideaswork.ideacoder.domain.user;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.awt.*;
import java.lang.Boolean;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        tags = "用户 API"
)
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("添加用户")
    public User saveUser(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        return userService.saveUser(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取 用户列表")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 用户")
    public User getUser(@PathVariable("id") final String id) {
        User userById = userService.getUserById(id);
        userById.setPassword("");
        userById.setCode("");
        userById.setEmailCode("");
        return userById;
    }
    @GetMapping("/currentUserInfo")
    @ApiOperation("根据主键获取一条 用户")
    public User getCurrentUserInfo() {
        User loginUser = SysTools.getLoginUser();
        String id = loginUser.getId();
        User userById = userService.getUserById(id);
        userById.setPassword("");
        userById.setCode("");
        userById.setEmailCode("");
        return userById;
    }


    @GetMapping("/getStatistics/{id}")
    @ApiOperation("获取某个用户统计信息")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Map<String,Integer> getUserStatisticsByUserId(@PathVariable("id") final String id) {
        return userService.getUserStatistics(id);
    }

    @GetMapping("/getStatistics")
    @ApiOperation("获取当前用户统计信息")
    public Map<String,Integer> getUserStatistics() {
        User loginUser = SysTools.getLoginUser();
        String id = loginUser.getId();
        return userService.getUserStatistics(id);
    }

    @GetMapping("/{id}/courses")
    @ApiOperation("根据主键获取用户的所有课程")
    public List<Course> getUserCourses(@PathVariable("id") final String id) {
        return userService.getUserCourses(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键更新用户")
    public User updateUser(@RequestBody User user, @PathVariable("id") final String id) {
        return userService.updateUserById(user, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键 id 删除用户")
    public void deleteUserById(@PathVariable("id") final String id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看用户是否存在")
    public Boolean isExistUser(@PathVariable("id") final String id) {
        return userService.isUserExist(id);
    }

    @GetMapping("/getPageList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("分页条件查询")
    public Page<User> getPageByCondition(UserDTO userDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return userService.getPageByCondition(userDTO, pageable);
    }
}