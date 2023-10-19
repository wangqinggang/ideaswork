package cn.ideaswork.ideacoder.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QcloudVodConfig {
    // 腾讯云秘钥配置
    @Value("${SecretId}")
    public String secretId;

    @Value("${SecretKey}")
    public String secretKey;

    // 腾讯云 vod 应用 appid
    @Value("${appid}")
    public String appid;

    // 腾讯云 vod 防盗链 key
    @Value("${key}")
    public String key;


}
