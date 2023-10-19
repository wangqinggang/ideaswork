package cn.ideaswork.ideacoder.domain.lms.course;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;

import cn.ideaswork.ideacoder.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
  Course saveCourse(Course course);

  List<Course> getAllCourses();

  Course getCourseById(final String id);

  Course updateCourseById(Course course, final String id);

  void deleteCourseById(final String id);

  Boolean isCourseExist(final String id);

  Page<Course> getPageByCondition(CourseDTO courseDTO, Pageable pageable);

  List<Course> getListByCondition(CourseDTO courseDTO);
  Boolean hasCourse(String id, User loginUser);
}
