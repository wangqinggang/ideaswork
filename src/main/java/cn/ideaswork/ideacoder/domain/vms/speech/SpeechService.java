package cn.ideaswork.ideacoder.domain.vms.speech;

import com.alibaba.nls.client.AccessToken;
import okhttp3.Response;

import java.io.IOException;

public interface  SpeechService {
    AccessToken getAccessToken(String accessKeyId, String accessKeySecret) throws IOException;

    Response getResponse(String speaker, String text, String format, String appKey, String token) throws IOException;
}
