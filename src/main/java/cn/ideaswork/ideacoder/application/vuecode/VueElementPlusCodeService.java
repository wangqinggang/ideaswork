package cn.ideaswork.ideacoder.application.vuecode;

import cn.ideaswork.ideacoder.domain.coder.domain.Domain;

import java.io.FileNotFoundException;
import java.util.Map;

public interface VueElementPlusCodeService {

    String generateAddForm(Domain domain);

    String generateFromText(Map<String, String> mapData, String path);

    String generateServiceJs(Domain domain);

    String generateDomainJs(Domain domain);

    String generatePageList(Domain domain);

    String generateInfo(Domain domain);

    void generateVueProject(String projectId, String basePath) throws FileNotFoundException;

    String generateDomainVue(Domain domainById);
}
