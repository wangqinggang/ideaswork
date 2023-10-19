package cn.ideaswork.ideacoder.domain.user;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.Map;

import cn.ideaswork.ideacoder.domain.lms.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(final String id);

    User updateUserById(User user, final String id);

    void deleteUserById(final String id);

    Boolean isUserExist(final String id);

    Page<User> getPageByCondition(UserDTO userDTO, Pageable pageable);

    List<User> getListByCondition(UserDTO userDTO);

    List<Course> getUserCourses(String id);

    User getUserByEmail(String email);

    void updateUserCode(User userDb);

    Map<String, Integer> getUserStatistics(String id);
}
