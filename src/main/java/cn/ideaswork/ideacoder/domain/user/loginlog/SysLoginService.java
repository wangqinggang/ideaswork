package cn.ideaswork.ideacoder.domain.user.loginlog;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysLoginService {

    SysLogin saveSysLogin(SysLogin sysLogin);
}
