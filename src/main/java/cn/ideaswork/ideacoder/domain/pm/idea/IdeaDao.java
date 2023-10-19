package cn.ideaswork.ideacoder.domain.pm.idea;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaDao extends MongoRepository<Idea, String> {
}
