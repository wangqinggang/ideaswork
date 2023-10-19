package cn.ideaswork.ideacoder.domain.lms.arrangement;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrangementDao extends MongoRepository<Arrangement, String> {
}
