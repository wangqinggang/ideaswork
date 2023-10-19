package cn.ideaswork.ideacoder.domain.vms.topic;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDao extends MongoRepository<Topic, String> {
    Integer countByUserId(String id);
}
