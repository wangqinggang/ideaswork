package cn.ideaswork.ideacoder.domain.vms.ai;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai")
@Api(tags = "AI接口")
@CrossOrigin()
public class BaiduAIController {
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    // 文案
    private static final String API_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";

    private static final String CLIENT_ID = "VOuBUZ2HTvuGT72KTPQzAXLv";
    private static final String CLIENT_SECRET = "bgbEAgqxXxP8GfStcSG08qcq7YpUQozp";

    /**
     * 获取 access_token
     *
     * @return
     * @throws IOException
     */
    public static String generateAccessToken() throws IOException {

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(ACCESS_TOKEN_URL);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        params.add(new BasicNameValuePair("client_id", CLIENT_ID));
        params.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));

        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
//        获取 entity 中的 data属性
        String data = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//        转化为json
        JSONObject jsonObject = JSONObject.parseObject(data);
//        System.out.println(jsonObject);
//        获取 access_token
        String access_token = jsonObject.getString("access_token");
//        System.out.println(access_token);
        return access_token;
    }

    @Autowired
    private BaiduAiService baiduAiService;

    @Autowired
    private UserService userService;

    /**
     * 写作辅助
     *
     * @param question
     * @return
     * @throws IOException
     */
    @GetMapping("/test")
    @ApiOperation(value = "写作辅助", notes = "写作辅助")
    @Transactional
    public ResponseEntity<String> test(@RequestParam(value = "question")String question) throws Exception {
        // 获取当前登录用户
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            return ResponseEntity.badRequest().body("请先登录");
        }
        String userId = loginUser.getId();
        // 检查当前登录用户是否有存量，如果有则继续生成，否则提示用户购买写作辅助用量
//        if (loginUser.getAiCount() <= 0) {
//            throw new Exception("您的存量不足，请购买写作辅助用量");
//        }
        String access_token = baiduAiService.generateAccessToken(CLIENT_ID, CLIENT_SECRET);
//            question = "视频主题是【如何用软件管理视频拍摄流程】，目标用户是【视频博主】。文案标题列表如下：";
        JSONObject jsonResult = baiduAiService.generateContent(access_token, question, userId);
        String result = jsonResult.getString("result");

        // 生成文案后，扣除用户的存量
//        Integer lastAiCount = loginUser.getAiCount() - 1;
        loginUser.setAiCount(100);
        loginUser.setVoiceCount(100);
        loginUser.setTopicCount(9);
        loginUser.setCopyCount(48);
        loginUser.setScriptCount(100);

        userService.updateUserById(loginUser, loginUser.getId());
        return ResponseEntity.ok(result);

    }

    /**
     * 写作辅助
     *
     * @param topic
     * @return
     * @throws IOException
     */
    @GetMapping("/topicTitleList")
    @ApiOperation(value = "文案创意", notes = "文案创意")
    @Transactional
    public ResponseEntity<String> generateContent(String topic) throws Exception {
        // 获取当前登录用户
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请先登录");
        }
        String userId = loginUser.getId();

        // 检查当前登录用户是否有存量，如果有则继续生成，否则提示用户购买写作辅助用量
        if (loginUser.getAiCount() <= 0) {
            throw new Exception("您的存量不足，请购买写作辅助用量");
        }
        String access_token = baiduAiService.generateAccessToken(CLIENT_ID, CLIENT_SECRET);
        topic = "方向：" + topic + "。创意主题标题列表如下：";
        JSONObject jsonResult = baiduAiService.generateContent(access_token, topic, userId);
        String result = jsonResult.getString("result");

        // 生成文案后，扣除用户的存量
        Integer lastAiCount = loginUser.getAiCount() - 1;
        loginUser.setAiCount(lastAiCount);
        userService.updateUserById(loginUser, loginUser.getId());
        return ResponseEntity.ok(result);

    }


    public static void main(String[] args) throws IOException {


        String access_token = generateAccessToken();

//        String content = "视频主题是「用软件管理视频」，给我一些视频创意，只返回无格式文本";

//        String content = "视频主题是【如何用软件管理视频拍摄流程】，目标用户是【视频博主】。简介如下：";
//        String content = "视频主题是【如何用软件管理视频拍摄流程】，目标用户是【视频博主】。文案标题列表如下：";
//        String content = "视频主题是【如何用软件管理视频拍摄流程】，目标用户是【视频博主】，文案标题为【【新手必看】如何用软件管理视频拍摄流程？】，大纲如下：";
//          String content = "内容大纲为【1. 开场\n" +
//                  "* 问候目标用户，简要介绍视频主题\n" +
//                  "* 解释视频将探讨如何用软件管理视频拍摄流程\n" +
//                  "* 展示一些视频拍摄流程管理的案例\n" +
//                  "2. 软件选择\n" +
//                  "* 介绍市场上的一些视频拍摄流程管理软件\n" +
//                  "* 对比不同软件的优缺点\n" +
//                  "* 强调选择适合自己需求的软件的重要性\n" +
//                  "3. 拍摄前的准备工作\n" +
//                  "* 确定视频拍摄流程和计划\n" +
//                  "* 制定拍摄时间表和预算\n" +
//                  "* 确定所需设备和材料\n" +
//                  "* 建立团队或寻找合作伙伴\n" +
//                  "4. 拍摄过程管理\n" +
//                  "* 合理规划拍摄时间和地点\n" +
//                  "* 管理团队成员和协作\n" +
//                  "* 控制预算和资源\n" +
//                  "* 处理突发情况和问题\n" +
//                  "5. 软件使用技巧和注意事项\n" +
//                  "* 介绍如何使用所选软件进行视频拍摄流程管理\n" +
//                  "* 演示一些实用的软件功能和操作技巧\n" +
//                  "* 提醒注意的安全和版权问题\n" +
//                  "6. 结语\n" +
//                  "* 总结视频要点，鼓励观众行动起来\n" +
//                  "* 提供联系方式，方便观众提问和交流】，文案如下：";

        String content = "你是一名分镜大师，能够将我的视频大纲分解为要拍摄的分镜，我的视频大纲为【1. 开场\n" +
                "* 问候目标用户，简要介绍视频主题\n" +
                "* 解释视频将探讨如何用软件管理视频拍摄流程\n" +
                "* 展示一些视频拍摄流程管理的案例\n" +
                "2. 软件选择\n" +
                "* 介绍市场上的一些视频拍摄流程管理软件\n" +
                "* 对比不同软件的优缺点\n" +
                "* 强调选择适合自己需求的软件的重要性\n" +
                "3. 拍摄前的准备工作\n" +
                "* 确定视频拍摄流程和计划\n" +
                "* 制定拍摄时间表和预算\n" +
                "* 确定所需设备和材料\n" +
                "* 建立团队或寻找合作伙伴\n" +
                "4. 拍摄过程管理\n" +
                "* 合理规划拍摄时间和地点\n" +
                "* 管理团队成员和协作\n" +
                "* 控制预算和资源\n" +
                "* 处理突发情况和问题\n" +
                "5. 软件使用技巧和注意事项\n" +
                "* 介绍如何使用所选软件进行视频拍摄流程管理\n" +
                "* 演示一些实用的软件功能和操作技巧\n" +
                "* 提醒注意的安全和版权问题\n" +
                "6. 结语\n" +
                "* 总结视频要点，鼓励观众行动起来\n" +
                "* 提供联系方式，方便观众提问和交流】，按照场景的分镜列表（格式：一句话行为主体与动作）如下：";

        double temperature = 0.95;
        double top_p = 0.8;
        double penalty_score = 1.0;
        boolean stream = false;
        String user_id = "123456";

        // 构建请求参数  https://cloud.baidu.com/doc/WENXINWORKSHOP/s/jlil56u11
        JSONObject responseData = requestCompletion(access_token, content, temperature, top_p, penalty_score, stream, user_id);
        System.out.println(responseData);

//        List<String> copyTitleList = getCopyTitleListByTopicName(responseData);
//        System.out.println(copyTitleList);
    }

    /**
     * 文本补全
     *
     * @param accessToken   access_token
     * @param content       文本内容
     * @param temperature   温度
     * @param top_p         top_p
     * @param penalty_score 惩罚分数
     * @param stream        是否返回流式文本
     * @param user_id       用户id
     * @return JSONObject
     * @throws IOException
     */
    private static JSONObject requestCompletion(String accessToken, String content, double temperature, double top_p, double penalty_score, boolean stream, String user_id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL + "?access_token=" + accessToken);
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

    /**
     * 根据主题名称返回文案列表
     *
     * @param responseData 文本补全返回的数据
     * @return 文案标题列表数据
     */
    private static List<String> getCopyTitleListByTopicName(JSONObject responseData) {
        String result = responseData.getString("result");
        // 解析文本列表
        String[] resultList = result.split("\\\n");
        List<String> resultList2 = new ArrayList<>();
        for (String item : resultList) {
            String trimmedItem = item.trim();
            // 查找引号内的数据内容
            int startIndex = trimmedItem.indexOf("\"");
            int endIndex = trimmedItem.lastIndexOf("\"");

            if (startIndex != -1 && endIndex != -1) {
                String line = trimmedItem.substring(startIndex + 1, endIndex);
                resultList2.add(line);
                System.out.println(line);
            }
        }
        return resultList2;
    }


    /**
     *
     * @param responseData 文本补全返回的数据
     * @return 文案标题列表数据
     */


}
