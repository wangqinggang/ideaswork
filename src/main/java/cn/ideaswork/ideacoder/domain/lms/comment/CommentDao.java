package cn.ideaswork.ideacoder.domain.lms.comment;

import java.lang.String;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends MongoRepository<Comment, String> {
}
