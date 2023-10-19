package cn.ideaswork.ideacoder.domain.vms.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaiduAiServiceImpl implements BaiduAiService {
    // access_token_url
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
//    private static final String ACCESS_TOKEN_URL = "https://wenxin.baidu.com/moduleApi/portal/api/oauth/token";

    // api_url
    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";


    @Override
    public String generateAccessToken(String clientId, String clientSecret) throws Exception {
        // 创建默认的HttpClient实例
        HttpClient httpClient = HttpClients.createDefault();
        // 创建HttpPost实例
        HttpPost httpPost = new HttpPost(ACCESS_TOKEN_URL);


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));

        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        // 执行POST请求
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        // 解析响应JSON字符串
        String data = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject responseJson = JSON.parseObject(data);

        // 获取access_token
        String access_token = responseJson.getString("access_token");
        System.out.println("access_token: " + access_token);
        return access_token;
    }

    @Override
    public JSONObject generateContent(String access_token, String question,String userid) throws IOException {
        double temperature = 0.95;
        double top_p = 0.8;
        double penalty_score = 1.0;
        boolean stream = false;

        // 构建请求参数  https://cloud.baidu.com/doc/WENXINWORKSHOP/s/jlil56u11
        JSONObject responseData = requestCompletion( access_token,question, temperature, top_p, penalty_score, stream, userid);
        return responseData;
    }


    /**
     * 文本补全请求方式
     * @param accessToken access_token
     * @param content 文本内容
     * @param temperature 温度
     * @param top_p top_p
     * @param penalty_score 惩罚分数
     * @param stream 是否返回流式文本
     * @param user_id 用户id
     * @return JSONObject
     * @throws IOException
     */
    private static JSONObject requestCompletion(String accessToken, String content, double temperature, double top_p, double penalty_score, boolean stream, String user_id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL+"?access_token="+accessToken);
        JSONObject params = new JSONObject();
        JSONArray messagesArr = new JSONArray();
        JSONObject messageObj = new JSONObject();
        messageObj.put("role", "user");
        messageObj.put("content", content);
        messagesArr.add(messageObj);

        params.put("messages", messagesArr);
        params.put("temperature", temperature);
        params.put("top_p", top_p);
        params.put("penalty_score", penalty_score);
        params.put("stream", stream);
        params.put("user_id", user_id);

//        设置请求为JSON格式
        httpPost.setHeader("Content-Type", "application/json");

        // 设置JSON请求体
        StringEntity requestEntity = new StringEntity(params.toJSONString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
//        获取 entity 中的 data属性
        String data = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        JSONObject responseData = JSONObject.parseObject(data);
        return responseData;
    }


}
