package cn.ideaswork.ideacoder.domain.pm.releaseplan;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseplanDao extends MongoRepository<Releaseplan, String> {
}
