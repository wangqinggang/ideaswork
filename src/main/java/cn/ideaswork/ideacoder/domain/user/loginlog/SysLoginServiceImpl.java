package cn.ideaswork.ideacoder.domain.user.loginlog;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import cn.ideaswork.ideacoder.domain.lms.course.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SysLoginServiceImpl implements SysLoginService {
    @Autowired
    private SysLoginDao sysLoginDao;

    @Override
    public SysLogin saveSysLogin(SysLogin sysLogin) {
//        sysLogin.setId(UUID.randomUUID().toString());
        return sysLoginDao.save(sysLogin);
    }
}