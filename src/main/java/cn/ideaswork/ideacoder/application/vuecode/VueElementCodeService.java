package cn.ideaswork.ideacoder.application.vuecode;

import cn.ideaswork.ideacoder.domain.coder.domain.Domain;

import java.io.FileNotFoundException;
import java.util.Map;

public interface VueElementCodeService {

    String generateAddForm(Domain domain);

    String generateFromText(Map<String, String> mapData, String path);

    String generateServiceJs(Domain domain);


    String generatePageList(Domain domain);

    String generateInfo(Domain domain);

    String generateDomainVue(Domain domainById);

    String generateDomainJs(Domain domain);
}
