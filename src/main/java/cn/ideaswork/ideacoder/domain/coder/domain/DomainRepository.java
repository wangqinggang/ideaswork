package cn.ideaswork.ideacoder.domain.coder.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DomainRepository extends MongoRepository<Domain, String> {
    List<Domain> findByProjectId(String projectId);
}
