package cn.ideaswork.ideacoder.domain.lms.oss;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import cn.ideaswork.ideacoder.infrastructure.tools.SignatureQcloudTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Api(
        tags = "Oss 对象存储 API"
)
@CrossOrigin
@RequestMapping("/oss")
public class OssController {
    @Autowired
    QcloudVodConfig qcloudVodConfig;

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
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 2 * 1); // 签名有效期：2 小时
        String signature = sign.getUploadSignature();
        System.out.println("signature : " + signature);
        return signature;
    }



    @GetMapping("/playSign/{fileid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("获取 视频播放防盗链 签名")
    public String getAdminPlayToken(@PathVariable String fileid) throws Exception {
        System.out.println(fileid+" -----------");
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId(qcloudVodConfig.secretId);
        sign.setSecretKey(qcloudVodConfig.secretKey);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 8 * 1); // 签名有效期：8h
        String token = sign.getAdminPlayToken(fileid);
//        System.out.println("token : " + token);
        return token;
    }

    @GetMapping("/studentPlay/{fileid}")
    @ApiOperation("获取 视频播放防盗链 签名")
    public String getStudentPlayToken(@PathVariable String fileid) throws Exception {
        System.out.println(fileid+" -----------");
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId(qcloudVodConfig.secretId);
        sign.setSecretKey(qcloudVodConfig.secretKey);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 8 * 1); // 签名有效期：8h

        // TODO 需要查询该学生是否购买了该课程，获取视频播放id
        String token = sign.getStudentPlayToken(fileid,false,30);// TODO
//        System.out.println("token : " + token);
        return token;
    }
}