package cn.ideaswork.ideacoder.domain.user.loginlog;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;
import java.util.UUID;

@RestController
@Api(
        tags = "临时登录表"
)
@CrossOrigin
@RequestMapping("/syslogin")
public class SysLoginController {
    @Autowired
    SysLoginService sysLoginService;

    @PostMapping
    @ApiOperation("临时登录")
    public SysLogin saveSysLogin(@RequestBody SysLogin sysLogin) {
//        sysLogin.(UUID.randomUUID().toString());
        return sysLoginService.saveSysLogin(sysLogin);
    }

}