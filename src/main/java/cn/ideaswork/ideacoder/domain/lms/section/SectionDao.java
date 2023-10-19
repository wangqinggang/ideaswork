package cn.ideaswork.ideacoder.domain.lms.section;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionDao extends MongoRepository<Section, String> {
}
