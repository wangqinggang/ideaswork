package cn.ideaswork.ideacoder.domain.coder.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DomainService {
    Domain saveDomain(Domain domainDTO);

    List<Domain> getAllDomains();

    List<Domain> getAllDomains(String projectId);

    Domain getDomainById(String id);

    Domain updateDomainById(Domain domainDTO, String id);

    void deleteDomainById(String id);

    Boolean isDomainExist(String id);

    Page<Domain> getDomainPageListByCondition(Domain domain, Pageable pageable);

    List<Domain> getDomainListByProjectId(String projectId);

    Domain getDomainBySql(String domainSql) throws Exception;

    Domain getDomainBySqlMysql(String domainSql) throws Exception;
}
