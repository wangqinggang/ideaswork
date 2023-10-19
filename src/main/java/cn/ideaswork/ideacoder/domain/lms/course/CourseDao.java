package cn.ideaswork.ideacoder.domain.lms.course;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends MongoRepository<Course, String> {
}
