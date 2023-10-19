package cn.ideaswork.ideacoder.presentation.backend;

import cn.ideaswork.ideacoder.application.javacode.JavaMybatisPlusCodeService;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/javaMybatisPlusCode")
@Api(tags = "Java Mybatis Plus 代码生成")
@CrossOrigin
public class JavaMybatisPlusController {

    @Autowired
    JavaMybatisPlusCodeService javaMybatisPlusService;

    @Autowired
    ProjectService projectService;

    @Autowired
    DomainService domainService;

    @GetMapping("/domain/{id}")
    @Operation(summary = "生成 Domain", description = "根据 id 生成 domain 对象内容")
    public String getDomain(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateDomain(domainById);
        return domainCode;
    }

    @GetMapping("/domainDTO/{id}")
    @Operation(summary = "生成 DomainDTO ", description = "根据 id 生成 domainDTO 对象内容")
    public String getDomainDTO(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateDTO(domainById);
        return domainCode;
    }

    @GetMapping("/service/{id}")
    @Operation(summary = "生成 Service ", description = "根据 id 生成 Service 对象内容")
    public String getService(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateService(domainById);
        return domainCode;
    }

    @GetMapping("/repository/{id}")
    @Operation(summary = "生成 Dao ", description = "根据 id 生成 Repository 对象内容")
    public String getRepository(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateRepository(domainById);
        return domainCode;
    }

    @GetMapping("/serviceImpl/{id}")
    @Operation(summary = "生成 ServiceImpl ", description = "根据 id 生成 ServiceImpl 对象内容")
    public String getServiceImpl(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateServiceImpl(domainById);
        return domainCode;
    }

    @GetMapping("/controller/{id}")
    @Operation(summary = "生成 Controller ", description = "根据 id 生成 Controller 对象内容")
    public String getController(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateController(domainById);
        return domainCode;
    }

    @GetMapping("/xml/{id}")
    @Operation(summary = "生成 XML ", description = "根据 id 生成 Xml 对象内容")
    public String getXml(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMybatisPlusService.generateMapperXml(domainById);
        return domainCode;
    }

}
