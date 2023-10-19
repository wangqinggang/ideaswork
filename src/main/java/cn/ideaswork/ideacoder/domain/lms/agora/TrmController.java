package cn.ideaswork.ideacoder.domain.lms.agora;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import cn.ideaswork.ideacoder.infrastructure.tools.SignatureQcloudTools;
import io.agora.rtm.RtmTokenBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Api(
        tags = "Rtm API"
)
@CrossOrigin
@RequestMapping("/rtm")
public class TrmController {
    @Autowired
    QcloudVodConfig qcloudVodConfig;

    private static String appId = "85d77c5eaea048b8bff38d245f970ebb";
    private static String appCertificate = "415c576bbabc4f699742a31a93c326a5";
    private static int expireTimestamp = 0;

    @GetMapping("/token")
    @ApiOperation("获取 rtm token")
    public String getToken(String userid) throws Exception {
        RtmTokenBuilder token = new RtmTokenBuilder();
        String result = token.buildToken(appId, appCertificate, userid, RtmTokenBuilder.Role.Rtm_User, expireTimestamp);
        System.out.println(result);
        return result;
    }



}