package cn.ideaswork.ideacoder.domain.lms.course;

import cn.ideaswork.ideacoder.domain.sale.order.OrdersService;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Course saveCourse(final Course course) {
        return courseDao.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.findAll();
    }

    @Override
    public Course getCourseById(final String id) {
        return courseDao.findById(id).orElse(new Course());
    }

    @Override
    @Transactional
    public Course updateCourseById(final Course course, final String id) {
        Course courseDb = courseDao.findById(id).orElse(new Course());
        BeanUtils.copyProperties(course, courseDb);
        return courseDao.save(courseDb);
    }

    @Override
    @Transactional
    public void deleteCourseById(final String id) {
        courseDao.deleteById(id);
    }

    @Override
    public Boolean isCourseExist(final String id) {
        return courseDao.existsById(id);
    }

    @Override
    public Page<Course> getPageByCondition(final CourseDTO courseDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(courseDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(courseDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getName())) {
            Pattern pattern = Pattern.compile("");// TODO
            Criteria criteria = Criteria.where("name").regex(courseDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(courseDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(courseDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getClassification())) {
            Criteria criteria = Criteria.where("classification").regex(courseDTO.getClassification());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(courseDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (courseDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(courseDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (courseDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(courseDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(courseDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (courseDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(courseDTO.getScore());
            query.addCriteria(criteria);
        }
        if (courseDTO.getLearnerNum() != null) {
            Criteria criteria = Criteria.where("learnerNum").is(courseDTO.getLearnerNum());
            query.addCriteria(criteria);
        }
        if (courseDTO.getMinutes() != null) {
            Criteria criteria = Criteria.where("minutes").is(courseDTO.getMinutes());
            query.addCriteria(criteria);
        }
        if (courseDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").is(courseDTO.getCzsj());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(courseDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Course.class), pageable);
    }

    @Override
    public List<Course> getListByCondition(final CourseDTO courseDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(courseDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(courseDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(courseDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(courseDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(courseDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(courseDTO.getClassification())) {
            Criteria criteria = Criteria.where("classification").regex(courseDTO.getClassification());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(courseDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (courseDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(courseDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (courseDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(courseDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(courseDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (courseDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(courseDTO.getScore());
            query.addCriteria(criteria);
        }
        if (courseDTO.getLearnerNum() != null) {
            Criteria criteria = Criteria.where("learnerNum").is(courseDTO.getLearnerNum());
            query.addCriteria(criteria);
        }
        if (courseDTO.getMinutes() != null) {
            Criteria criteria = Criteria.where("minutes").is(courseDTO.getMinutes());
            query.addCriteria(criteria);
        }
        if (courseDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").is(courseDTO.getCzsj());
            query.addCriteria(criteria);
        }
        if (courseDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(courseDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Course.class);
    }

    @Override
    public Boolean hasCourse(String courseId, User loginUser) {
        User userById = userService.getUserById(loginUser.getId());
        List<String> myCourses = userById.getMyCourses();

        if(myCourses!=null && myCourses.size()>0){
            boolean contains = myCourses.contains(courseId);
           if(contains){
               return true;
           }else{
              return syncCourse(courseId, loginUser, userById, myCourses);
           }
        }else{
            // 确认当前用户的课程订单支付状态
            return syncCourse(courseId, loginUser, userById, myCourses);
        }
    }

    /**
     * 同步当前课程订单并同步到当前用户信息
     * @param courseId
     * @param loginUser
     * @param userById
     * @param myCourses
     */
    private Boolean syncCourse(String courseId, User loginUser, User userById, List<String> myCourses) {
        // 确认当前用户的课程订单支付状态,若支付成功则同步我的课程
        Boolean isHaveCourse = orderService.getIsHaveCourse(courseId, loginUser);
        if(isHaveCourse){
            Set<String> myCoursesSet = new HashSet<>(myCourses);
            myCoursesSet.add(courseId);
            userById.setMyCourses(new ArrayList<>(myCoursesSet));
            userService.updateUserById(userById, userById.getId());
            return true;
        }else{
            return false;
        }
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Course mapToEntity(final CourseDTO courseDTO, final Course course) {
        BeanUtils.copyProperties(courseDTO, course);
        return course;
    }

    public CourseDTO mapToDTO(final Course course, final CourseDTO courseDTO) {
        BeanUtils.copyProperties(course, courseDTO);
        return courseDTO;
    }
}