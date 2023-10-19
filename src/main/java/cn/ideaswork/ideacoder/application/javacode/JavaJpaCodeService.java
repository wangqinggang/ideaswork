package cn.ideaswork.ideacoder.application.javacode;

import cn.ideaswork.ideacoder.domain.coder.domain.Domain;

/**
 * 生成 Jpa 代码
 */
public interface JavaJpaCodeService {

    String generateDomain(Domain domain) throws ClassNotFoundException;

    String generatePk(Domain domain) throws ClassNotFoundException;

    String generateDTO(Domain domain) throws ClassNotFoundException;

    String generateService(Domain domain);

    String generateRepository(Domain domain);

    String generateServiceImpl(Domain domain);

    String generateController(Domain domain);

    void generateJavaProject(String projectId,String basePath);

    String getCompleteServiceImpl(String file);

    String getCompleteController(String file);

    String generateApplication(String packageName ,String basePath);

    String generateProperties(String projectId);

    String generateApplicationTests(String packageName,String uppercaseProjectName);
}
