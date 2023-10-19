package cn.ideaswork.ideacoder.domain.vms.script;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptDao extends MongoRepository<Script, String> {
    Integer countByUserId(String id);

    Integer countByCopyId(String copyId);
}
