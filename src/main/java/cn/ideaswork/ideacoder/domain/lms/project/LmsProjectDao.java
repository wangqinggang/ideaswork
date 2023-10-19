package cn.ideaswork.ideacoder.domain.lms.project;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LmsProjectDao extends MongoRepository<LmsProject, String> {
}
