package cn.ideaswork.ideacoder.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfig {

    @Value("${wxpay_mchId}")
    public String mchId;

    @Value("${wxpay_key}")
    public String key;

    @Value("${wxpay_notify_url}")
    public String notify_url;

    @Value("${wxpay_return_url}")
    public String return_url;

    @Value("${wxlogin_callback_url}")
    public String wxlogin_callback_url;
}
