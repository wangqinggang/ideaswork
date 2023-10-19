//package cn.ideaswork.ideacoder.infrastructure.vod;
//
//import com.aliyuncs.profile.DefaultProfile;
//import com.aliyuncs.DefaultAcsClient;
//import com.aliyuncs.exceptions.ClientException;
//
//public class AliyunVod {
//    //填入AccessKey信息
//    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
//        String regionId = "cn-shanghai";  // 点播服务接入地域
//        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
//        DefaultAcsClient client = new DefaultAcsClient(profile);
//        return client;
//    }
//
//
//    //填入STS信息
//    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret, String securityToken) throws ClientException {
//        String regionId = "cn-shanghai";  // 点播服务接入地域
//        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret, securityToken);
//        DefaultAcsClient client = new DefaultAcsClient(profile);
//        return client;
//    }
//}

