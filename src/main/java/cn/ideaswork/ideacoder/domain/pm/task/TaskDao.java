package cn.ideaswork.ideacoder.domain.pm.task;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDao extends MongoRepository<Task, String> {
}
