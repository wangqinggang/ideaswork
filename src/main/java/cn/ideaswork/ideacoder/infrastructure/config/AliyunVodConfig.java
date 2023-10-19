package cn.ideaswork.ideacoder.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunVodConfig {

    @Value("${accessKeyId}")
    public String accessKeyId;

    @Value("${accessKeySecret}")
    public String accessKeySecret;

    @Value("${endpoint}")
    public String endpoint;

    @Value("${bucketName}")
    public String bucketName;
}
