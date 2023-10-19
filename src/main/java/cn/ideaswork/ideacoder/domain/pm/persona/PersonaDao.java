package cn.ideaswork.ideacoder.domain.pm.persona;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaDao extends MongoRepository<Persona, String> {
}
