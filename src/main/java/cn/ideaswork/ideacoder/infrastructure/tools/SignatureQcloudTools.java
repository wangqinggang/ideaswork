package cn.ideaswork.ideacoder.infrastructure.tools;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.util.HashMap;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// 签名工具类
public class SignatureQcloudTools {


    private String secretId; // vod id
    private String secretKey; // vod key
    private long currentTime; // 当前时间
    private int random;
    private int signValidDuration;
    private static final String HMAC_ALGORITHM = "HmacSHA1"; //签名算法
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    // 获取上传视频签名
    public String getUploadSignature() throws Exception {
        String strSign = "";
        String contextStr = "";
        // 生成原始参数字符串
        long endTime = (currentTime + signValidDuration);
        contextStr += "secretId=" + java.net.URLEncoder.encode(secretId, "utf8");
        contextStr += "&currentTimeStamp=" + currentTime;
        contextStr += "&procedure=" + "转自适应码流";// 任务流名称 转自适应码流
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + random;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(this.secretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = base64Encode(sigBuf);
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            throw e;
        }
        return strSign;
    }

    /**
     * 获取防盗链播放签名
     *
     * @param FileId vid
     * @return url token
     * @throws Exception
     */
    public String getAdminPlayToken(String FileId) throws Exception {
        Integer AppId = 1253634941;//1253634941
        Integer CurrentTime = Math.toIntExact(currentTime);
        Integer PsignExpire = Math.toIntExact((currentTime + signValidDuration));
        String UrlTimeExpire = Long.toHexString(PsignExpire);

        System.out.println(FileId);
        System.out.println(CurrentTime);
        System.out.println(PsignExpire);

        String Key = "KdaD969sCd4zgh6k6mGp";// 防盗链 key 设置
        HashMap<String, String> urlAccessInfo = new HashMap<String, String>();
        urlAccessInfo.put("t", UrlTimeExpire);// 16进制字符串，表示链接的过期时间，不填表示不过期


        Algorithm algorithm = Algorithm.HMAC256(Key);// 使用秘钥进行签名
        String token = JWT.create()
                .withClaim("appId", AppId)
                .withClaim("fileId", FileId)
                .withClaim("currentTimeStamp", CurrentTime)
                .withClaim("expireTimeStamp", PsignExpire)
                .withClaim("urlAccessInfo", urlAccessInfo)
                .sign(algorithm);
//        System.out.println("token:" + token);

        return token;
    }

    /**
     * 获取学生播放 token
     * @param FileId 视频id
     * @param buyCourse 是否购买了改门课程
     * @param freeTime 若没有购买可试看多长时间
     * @return token
     * @throws Exception
     */
    public String getStudentPlayToken(String FileId,Boolean buyCourse,Integer freeTime) throws Exception {
        Integer AppId = 1253634941;//1253634941
        Integer CurrentTime = Math.toIntExact(currentTime);
        Integer PsignExpire = Math.toIntExact((currentTime + signValidDuration));
        String UrlTimeExpire = Long.toHexString(PsignExpire);

        String Key = "KdaD969sCd4zgh6k6mGp";// 防盗链 key 设置 TODO 需要配置
        HashMap<String, String> urlAccessInfo = new HashMap<String, String>();
        urlAccessInfo.put("t", UrlTimeExpire);// 16进制字符串，表示链接的过期时间，不填表示不过期
//        urlAccessInfo.put("rlimit", 3+"");// 最多允许多少个不同 IP 的终端播放，以十进制表示
        // TODO  试看时长设置 此处用于收费课程的免费试看
        if(!buyCourse){
            urlAccessInfo.put("exper", freeTime+"");// 试看时长，单位为秒，以十进制表示 如果要指定试看时长，时长必须不小于30秒
        }

        Algorithm algorithm = Algorithm.HMAC256(Key);// 使用秘钥进行签名
        String token = JWT.create()
                .withClaim("appId", AppId)
                .withClaim("fileId", FileId)
                .withClaim("currentTimeStamp", CurrentTime)
                .withClaim("expireTimeStamp", PsignExpire)
                .withClaim("urlAccessInfo", urlAccessInfo)
                .sign(algorithm);
//        System.out.println("token:" + token);

        return token;
    }

    private String base64Encode(byte[] buffer) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(buffer);
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public void setSignValidDuration(int signValidDuration) {
        this.signValidDuration = signValidDuration;
    }

}
