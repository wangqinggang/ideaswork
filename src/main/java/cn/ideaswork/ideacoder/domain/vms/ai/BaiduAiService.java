package cn.ideaswork.ideacoder.domain.vms.ai;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

public interface BaiduAiService {

    /**
     * 获取访问access_token
     * @param clientId 百度云应用的AK
     * @param clientSecret 百度云应用的SK
     * @return access_token
     * @throws Exception
     */
    String generateAccessToken(String clientId, String clientSecret) throws Exception;

    /**
     * 生成内容
     * @param access_token
     * @param question
     * @param userId
     * @return
     */
    JSONObject generateContent(String access_token, String question, String userId) throws IOException;
}
