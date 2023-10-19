package cn.ideaswork.ideacoder.domain.vms.speech;

import cn.hutool.core.codec.Base64Encoder;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.vms.script.Script;
import cn.ideaswork.ideacoder.domain.vms.script.ScriptService;
import cn.ideaswork.ideacoder.domain.vms.topic.Topic;
import cn.ideaswork.ideacoder.domain.vms.topic.TopicDTO;
import cn.ideaswork.ideacoder.domain.vms.topic.TopicService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.nls.client.AccessToken;
import com.sun.mail.util.BASE64DecoderStream;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(tags = "语音 API")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/speech")
public class SpeechController {
    @Autowired
    ScriptService scriptService;
    @Autowired
    SpeechService speechService;

    static String token = "";

//    @GetMapping("/{id}")
//    @ApiOperation("根据脚本id获取一条脚本的语音base64")
//    public String getSpeechByScriptId(@PathVariable("id") final String id) {
//
//        Script scriptById = scriptService.getScriptById(id);
//        String caption = scriptById.getCaption();
//
//        String speaker = "ailun";
//        String text = "今天是周一，天气挺好的。";
//        String format = "wav";
//        String appKey = "BbQ4J5uJrwZxs63R";
//
//        String accessKeyId = "LTAI5tJPMbY8mBSNNikM19ni";
//        String accessKeySecret = "klnZCKTUu7rZwmHohWSmMP90IjJLzm";
//
//        return "";
//    }

    @GetMapping("/getAudio")
    @ApiOperation("获取一个脚本的语音")
//     String speaker ="ailun";
//        String format = "wav";
    // 拦截权限为vms-语音生成
    @PreAuthorize("hasAuthority('VMS_AUDIO')")
    public String getAudio(String text, String speaker, String format) {
        String appKey = "BbQ4J5uJrwZxs63R";
        String accessKeyId = "LTAI5tJPMbY8mBSNNikM19ni";
        String accessKeySecret = "klnZCKTUu7rZwmHohWSmMP90IjJLzm";

        AccessToken accessToken = null;
        try {
            accessToken = speechService.getAccessToken(accessKeyId, accessKeySecret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String token = accessToken.getToken();
        long expireTime = accessToken.getExpireTime();


        long now = System.currentTimeMillis();
        long isExpire = now - expireTime;
        if (isExpire > 0) {
//            token 能继续使用
        } else {
//            重新获取token
        }


        Response response = null;
        try {
            response = speechService.getResponse(speaker, text, format, appKey, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentType = response.header("Content-Type");
        if ("audio/mpeg".equals(contentType)) {
            try {
                byte[] bytes = response.body().bytes();
                String encode = Base64Encoder.encode(bytes);
                String head = "data:audio/wav;base64,";
                System.out.println(encode);
                return head + encode;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String errorMessage = null;
            try {
                errorMessage = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("The GET request failed: " + errorMessage);
        }


        return "";
    }

}