package cn.ideaswork.ideacoder.domain.pm.problem;


import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemDao extends MongoRepository<Problem, String> {
}
