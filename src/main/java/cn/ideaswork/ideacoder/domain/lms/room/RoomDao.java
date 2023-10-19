package cn.ideaswork.ideacoder.domain.lms.room;

import java.lang.String;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDao extends MongoRepository<Room, String> {
}
