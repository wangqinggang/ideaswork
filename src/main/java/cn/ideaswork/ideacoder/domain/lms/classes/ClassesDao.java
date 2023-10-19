package cn.ideaswork.ideacoder.domain.lms.classes;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassesDao extends MongoRepository<Classes, String> {
}
