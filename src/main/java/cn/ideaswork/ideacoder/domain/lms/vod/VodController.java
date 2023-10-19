package cn.ideaswork.ideacoder.domain.lms.vod;

import cn.ideaswork.ideacoder.domain.lms.course.CourseService;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import cn.ideaswork.ideacoder.infrastructure.tools.SignatureQcloudTools;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@Api(
        tags = "VOD 视频点播 API"
)
@CrossOrigin
@RequestMapping("/vod")
public class VodController {
    @Autowired
    QcloudVodConfig qcloudVodConfig;

    @Autowired
    CourseService courseService;


    //  @PostMapping
//  @ApiOperation("添加学生")
//  public Student saveStudent(@RequestBody Student student) {
//    student.setId(UUID.randomUUID().toString());
//    return studentService.saveStudent(student);
//  }
//
    @GetMapping("/signature")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取 Qcloud 签名")
    public String getSignature() throws Exception {
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId(qcloudVodConfig.secretId);
        sign.setSecretKey(qcloudVodConfig.secretKey);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 2 * 1); // 签名有效期：2 小时
        String signature = sign.getUploadSignature();
        System.out.println("signature : " + signature);
        return signature;
    }

    @GetMapping("/playSign/{fileid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取 视频播放防盗链 签名")
    public String getAdminPlayToken(@PathVariable String fileid) throws Exception {
        System.out.println(fileid + " -----------");
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId(qcloudVodConfig.secretId);
        sign.setSecretKey(qcloudVodConfig.secretKey);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 8 * 1); // 签名有效期：8h
        String token = sign.getAdminPlayToken(fileid);
//        System.out.println("token : " + token);
        return token;
    }

    @GetMapping("/studentPlay/{courseid}/{fileid}")
    @ApiOperation("获取 视频播放防盗链 签名")
    public String getStudentPlayToken(@PathVariable String courseid, @PathVariable String fileid) throws Exception {
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId(qcloudVodConfig.secretId);
        sign.setSecretKey(qcloudVodConfig.secretKey);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 8 * 1); // 签名有效期：8h

        // TODO 需要查询该学生是否购买了该课程，获取视频播放id
        User loginUser = SysTools.getLoginUser();

        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            return "";
        }

        Boolean hasCourse = courseService.hasCourse(courseid, loginUser);
        if (hasCourse) {
//            String token = sign.getStudentPlayToken(fileid, true, 30);// TODO
            String token = sign.getAdminPlayToken(fileid);
            return token;
        }
        return "";
    }

    @DeleteMapping("/{fileid}")
    @ApiOperation("根据 fileid 删除视频")
    public String deleteVodFile(@PathVariable String fileid) throws Exception {
        System.out.println(fileid + " -----------");

        // 实例化要请求产品的client对象,clientProfile是可选的
        Credential credential = new Credential(qcloudVodConfig.secretId, qcloudVodConfig.secretKey);
        String region = "";
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        VodClient client = new VodClient(credential, region, clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeleteMediaRequest req = new DeleteMediaRequest();
        req.setFileId(fileid);
        // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
        DeleteMediaResponse resp = client.DeleteMedia(req);
        // 输出json格式的字符串回包
        System.out.println(DeleteMediaResponse.toJsonString(resp));
        return resp.getRequestId();
    }
}