package cn.ideaswork.ideacoder.domain.user;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import cn.ideaswork.ideacoder.domain.lms.course.CourseService;
import cn.ideaswork.ideacoder.domain.sale.order.OrdersService;
import cn.ideaswork.ideacoder.domain.vms.copy.CopyService;
import cn.ideaswork.ideacoder.domain.vms.script.ScriptService;
import cn.ideaswork.ideacoder.domain.vms.topic.TopicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CourseService courseService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private CopyService copyService;

    @Autowired
    private ScriptService scriptService;


    @Override
    @Transactional
    public User saveUser(final User user) {
        return userDao.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User getUserById(final String id) {
        User user = userDao.findById(id).orElse(new User());
        return user;
    }

    @Override
    @Transactional
    public User updateUserById(final User user, final String id) {
        User userDb = userDao.findById(id).orElse(new User());
//        user.setCode(userDb.getCode());
        if (userDb.getPassword()!=null||StringUtils.isNotBlank(userDb.getPassword())){
            user.setPassword(userDb.getPassword());
        }
        BeanUtils.copyProperties(user, userDb);
        return userDao.save(userDb);
    }

    @Override
    @Transactional
    public void deleteUserById(final String id) {
        userDao.deleteById(id);
    }

    @Override
    public Boolean isUserExist(final String id) {
        return userDao.existsById(id);
    }

    @Override
    public Page<User> getPageByCondition(final UserDTO userDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(userDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(userDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getOpenid())) {
            Criteria criteria = Criteria.where("openid").is(userDTO.getOpenid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(userDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getEmail())) {
            Criteria criteria = Criteria.where("email").regex(userDTO.getEmail());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getPhone())) {
            Criteria criteria = Criteria.where("phone").regex(userDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            Criteria criteria = Criteria.where("password").is(userDTO.getPassword());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getCode())) {
            Criteria criteria = Criteria.where("code").is(userDTO.getCode());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getEmailCode())) {
            Criteria criteria = Criteria.where("emailCode").is(userDTO.getEmailCode());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(userDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").regex(userDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (userDTO.getZcsj() != null) {
            Criteria criteria = Criteria.where("zcsj").gte(userDTO.getZcsj());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, User.class), pageable);
    }

    @Override
    public List<User> getListByCondition(final UserDTO userDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(userDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(userDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getOpenid())) {
            Criteria criteria = Criteria.where("openid").is(userDTO.getOpenid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(userDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getEmail())) {
            Criteria criteria = Criteria.where("email").regex(userDTO.getEmail());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getPhone())) {
            Criteria criteria = Criteria.where("phone").regex(userDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getPassword())) {
            Criteria criteria = Criteria.where("password").is(userDTO.getPassword());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getCode())) {
            Criteria criteria = Criteria.where("code").is(userDTO.getCode());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getEmailCode())) {
            Criteria criteria = Criteria.where("emailCode").is(userDTO.getEmailCode());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(userDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(userDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").regex(userDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (userDTO.getZcsj() != null) {
            Criteria criteria = Criteria.where("zcsj").is(userDTO.getZcsj());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<Course> getUserCourses(String id) {
        List<Course> courses = new ArrayList<>();
        User userById = this.getUserById(id);
        List<String> myCourses = userById.getMyCourses();
        if(myCourses != null && myCourses.size()>0){
            myCourses.forEach(one -> {
                Course courseById = courseService.getCourseById(one);
                courses.add(courseById);
            });
        }else{
            return new ArrayList<>();
        }

        return courses;
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email).orElse(new User());
    }

    @Override
    public void updateUserCode(User userDb) {
        userDao.save(userDb);
    }

    @Override
    public Map<String, Integer> getUserStatistics(String id) {
        Integer topicNum = topicService.getTopicNumByUserId(id);
        Integer copyNum = copyService.getCopyNumByUserId(id);
        Integer scriptNum = scriptService.getScriptNumByUserId(id);
        HashMap<String, Integer> hashMap = new HashMap<>() {{
            put("topicNum", topicNum);
            put("copyNum", copyNum);
            put("scriptNum", scriptNum);
        }};
        return hashMap;
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public User mapToEntity(final UserDTO userDTO, final User user) {
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    public UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}