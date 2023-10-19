package cn.ideaswork.ideacoder.presentation.backend;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.ideaswork.ideacoder.application.javacode.JavaJpaCodeService;
import cn.ideaswork.ideacoder.application.javacode.JavaMongoCodeService;
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
import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/v1/javaMongoCode")
@Api(tags = "Java Mongodb 代码生成")
@CrossOrigin()
public class JavaMongoCodeController {

    @Autowired
    JavaMongoCodeService javaMongoCodeService;

    @Autowired
    ProjectService projectService;

    @Autowired
    DomainService domainService;

    @GetMapping("/domain/{id}")
    @Operation(summary = "生成 Domain ", description = "根据 id 生成 domain 对象内容目")
    public String getDomain(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMongoCodeService.generateDomain(domainById);
        return domainCode;
    }
    @GetMapping("/domainDTO/{id}")
    @Operation(summary = "生成 DomainDTO ", description = "根据 id 生成 domainDTO 对象内容目")
    public String getDomainDTO(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMongoCodeService.generateDTO(domainById);
        return domainCode;
    }

    @GetMapping("/service/{id}")
    @Operation(summary = "生成 Service ", description = "根据 id 生成 Service 对象内容目")
    public String getService(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMongoCodeService.generateService(domainById);
        return domainCode;
    }

    @GetMapping("/serviceImpl/{id}")
    @Operation(summary = "生成 ServiceImpl ", description = "根据 id 生成 ServiceImpl 对象内容")
    public String getServiceImpl(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String serviceImplCode = javaMongoCodeService.generateServiceImpl(domainById);
        String code = javaMongoCodeService.getCompleteServiceImpl(serviceImplCode);
        return code;
    }


    @GetMapping("/repository/{id}")
    @Operation(summary = "生成 Dao ", description = "根据 id 生成 Repository 对象内容目")
    public String getRepository(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String domainCode = javaMongoCodeService.generateRepository(domainById);
        return domainCode;
    }

    @GetMapping("/controller/{id}")
    @Operation(summary = "生成 Controller ", description = "根据 id 生成 Controller 对象内容")
    public String getController(@PathVariable("id") String id) throws ClassNotFoundException {
        Domain domainById = domainService.getDomainById(id);
        String controllerCode = javaMongoCodeService.generateController(domainById);
        String code = javaMongoCodeService.getCompleteController(controllerCode);
        return code;
    }

    @PostMapping("/generateProject/{projectId}")
    @Operation(summary = "生成 Java Mongo 工程 ", description = "根据 id 生成 Java Mongo 工程文件")
    public void downloadMongoProject(@PathVariable("projectId") String projectId, HttpServletResponse response) throws ClassNotFoundException, IOException {
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
            javaMongoCodeService.generateJavaProject(projectId,basePath);

            // 打包该文件

            ZipUtil.zip(packagePath);
            System.out.println(zipFileLocation + "-----------------------------------");
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
