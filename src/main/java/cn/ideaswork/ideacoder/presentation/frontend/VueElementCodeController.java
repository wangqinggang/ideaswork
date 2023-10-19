package cn.ideaswork.ideacoder.presentation.frontend;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.ideaswork.ideacoder.application.vuecode.VueElementPlusCodeService;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(value = "/v1/vueElementCode")
@Api(tags = "Vue Element 代码生成")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin()
public class VueElementCodeController {

    @Autowired
    VueElementPlusCodeService vueElementPlusCodeService;

    @Autowired
    ProjectService projectService;

    @Autowired
    DomainService domainService;

    @GetMapping("/addForm/{id}")
    @Operation(summary = "生成 AddForm", description = "根据 id 生成 AddForm 内容")
    public String getAddForm(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String addFormCode = vueElementPlusCodeService.generateAddForm(domainById);
        return addFormCode;
    }

    @GetMapping("/infoForm/{id}")
    @Operation(summary = "生成 InfoForm", description = "根据 id 生成 InfoForm 内容")
    public String getUnionPk(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String infoForm = vueElementPlusCodeService.generateInfo(domainById);
        return infoForm;
    }

    @GetMapping("/pageList/{id}")
    @Operation(summary = "生成 PageList ", description = "根据 id 生成 PageList 内容")
    public String getPageList(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String pageListCode = vueElementPlusCodeService.generatePageList(domainById);
        return pageListCode;
    }

    @GetMapping("/serviceJs/{id}")
    @Operation(summary = "生成 ServiceJs ", description = "根据 id 生成 ServiceJs 内容")
    public String getService(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String serviceJsCode = vueElementPlusCodeService.generateServiceJs(domainById);
        return serviceJsCode;
    }

    @GetMapping("/domainVue/{id}")
    @Operation(summary = "生成 DomainVue  ", description = "根据 id 生成 DomainVue 内容")
    public String elementPlusVue(@PathVariable("id") String id) {
        Domain domainById = domainService.getDomainById(id);
        String fileString = "";
        fileString = vueElementPlusCodeService.generateDomainVue(domainById);
        return fileString;
    }
}
