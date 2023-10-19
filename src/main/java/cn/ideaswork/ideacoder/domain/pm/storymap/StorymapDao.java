package cn.ideaswork.ideacoder.domain.pm.storymap;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorymapDao extends MongoRepository<Storymap, String> {
}
