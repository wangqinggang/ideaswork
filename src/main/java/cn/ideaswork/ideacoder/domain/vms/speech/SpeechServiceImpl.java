package cn.ideaswork.ideacoder.domain.vms.speech;

import com.alibaba.nls.client.AccessToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SpeechServiceImpl implements SpeechService {
    @Override
    public AccessToken getAccessToken(String accessKeyId, String accessKeySecret) throws IOException {
        AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
        accessToken.apply();
        return accessToken;
    }


    @Override
    public Response getResponse(String speaker, String text, String format, String appKey, String token) throws IOException {
        int sampleRate = 16000;
        String url = "https://nls-gateway.cn-shanghai.aliyuncs.com/stream/v1/tts";
        url = url + "?appkey=" + appKey;
        url = url + "&token=" + token;
        url = url + "&text=" + text;
        url = url + "&format=" + format;
        url = url + "&voice=" + speaker;
        url = url + "&sample_rate=" + String.valueOf(sampleRate);
        System.out.println("URL: " + url);
        Request request = new Request.Builder().url(url).get().build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response;
    }
}
