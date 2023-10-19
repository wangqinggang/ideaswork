package cn.ideaswork.ideacoder.application.javacode;

import cn.ideaswork.ideacoder.domain.coder.domain.Domain;

/**
 * 生成 Mybatis Plus 代码
 */
public interface JavaMybatisPlusCodeService {

    String generateDomain(Domain domain) throws ClassNotFoundException;

    String generateDTO(Domain domain) throws ClassNotFoundException;

    String generateService(Domain domain);

    String generateRepository(Domain domain);

    String generateServiceImpl(Domain Domain);

    String generateController(Domain Domain);

    String generateMapperXml(Domain Domain);

}
