package cn.ideaswork.ideacoder.domain.lms.feedback;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackDao extends MongoRepository<Feedback, String> {
}
