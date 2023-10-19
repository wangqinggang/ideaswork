package cn.ideaswork.ideacoder.domain.vms.copy;

import java.lang.String;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopyDao extends MongoRepository<Copy, String> {
    Integer countByUserId(String id);

    Integer countByTopicId(String topicId);
}
