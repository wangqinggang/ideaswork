package cn.ideaswork.ideacoder.domain.user.loginlog;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysLoginDao extends MongoRepository<SysLogin, String> {

}
