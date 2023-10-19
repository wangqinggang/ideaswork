package cn.ideaswork.ideacoder.presentation.frontend;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.ideaswork.ideacoder.application.javacode.JavaJpaCodeService;
import cn.ideaswork.ideacoder.application.vuecode.VueElementPlusCodeService;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(value = "/v1/vueElementPlusCode")
@Api(tags = "Vue ElementPlus 代码生成")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin()
public class VueElementPlusCodeController {

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

    @PostMapping("/generateProject/{projectId}")
    @Operation(summary = "生成 Vue ElementPlus 工程 ", description = "根据 id 生成 Vue ElementPlus 工程文件")
    public void downloadVueElementPlusProject(@PathVariable("projectId") String projectId, HttpServletResponse response) throws ClassNotFoundException, IOException {
        String sysPath = "/Users/william/Eastsoft/generateCode/"; // 打包存储的系统绝对路径  TODO 需要设定路径
        Project projectById = projectService.getProjectById(projectId);
        if (StringUtils.isNotBlank(projectById.getId())) {
            // 生成文件结构
            String folder = projectId; //  打包文件夹使用 java 项目 id
            String basePath = sysPath + folder + "/";// 此路径后面可以继续拼接包名

            // 打包路径
            String packagePath = sysPath + folder;
            String zipFileLocation = packagePath + ".zip";
            String zipFieName = folder + ".zip";
            // TODO 此处需要加判断  目录存在才删除
//            SysTools.deleteDir(packagePath);
            Files.deleteIfExists(Path.of(zipFileLocation));
            vueElementPlusCodeService.generateVueProject(projectId,basePath);

            // 打包该文件
            ZipUtil.zip(packagePath);

            System.out.println(zipFileLocation + "");
            System.out.println(zipFieName);

            // 打包文件下载
            InputStream inputStream = FileUtil.getInputStream(new File(zipFileLocation));
            response.setContentLength(inputStream.available());
            response.setContentType("application/octet-stream");
            response.setHeader("fileName", zipFieName);
            response.setHeader("Access-Control-Expose-Headers", "fileName");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFieName + "\"");
            FileCopyUtils.copy(inputStream, response.getOutputStream());

        }
    }

}
