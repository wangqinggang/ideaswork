package cn.ideaswork.ideacoder;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import cn.ideaswork.ideacoder.infrastructure.speech.SpeechRestful;
import com.alibaba.nls.client.AccessToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

@SpringBootTest
class TestSpeech {


    @Test
    void testSpeech() throws IOException {
        String speaker ="ailun";
        String text = "今天是周一，天气挺好的。";
        String format = "wav";
        String appKey = "BbQ4J5uJrwZxs63R";

        String accessKeyId = "LTAI5tJPMbY8mBSNNikM19ni";
        String accessKeySecret = "klnZCKTUu7rZwmHohWSmMP90IjJLzm";

        AccessToken accessToken = getAccessToken(accessKeyId,accessKeySecret);
        String token = accessToken.getToken();
        long expireTime = accessToken.getExpireTime();


        long now = System.currentTimeMillis();
        long isExpire= now - expireTime;
        if (isExpire>0){
//            token 能继续使用
        }else{
//            重新获取token
        }


        Response response = getResponse(speaker, text, format, appKey, token);
        System.out.println(response);
        String contentType = response.header("Content-Type");
        if ("audio/mpeg".equals(contentType)) {
            System.out.println(response.body().bytes().length);

        }else {
            String errorMessage = response.body().string();
            System.out.println("The GET request failed: " + errorMessage);
        }
    }

    private Response getResponse(String speaker, String text, String format, String appKey, String token) throws IOException {
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

    private AccessToken getAccessToken(String accessKeyId,String accessKeySecret) throws IOException {
        AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
        accessToken.apply();
        return accessToken;
    }
}
