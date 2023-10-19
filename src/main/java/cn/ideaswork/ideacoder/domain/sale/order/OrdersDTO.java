package cn.ideaswork.ideacoder.domain.sale.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
    @Schema(description = "订单id")
    private String id;

    @Schema(description = "yungouos 系统订单id")
    private String ygosorderid;

    @Schema(description = "支付单号（第三方支付单号）")
    private String payno;

    @Schema(description = "商户单号")
    private String shorderno;

    @Schema(description = "用户id")
    private String userid;

    @Schema(description = "用户微信 openid ")
    private String openid;

    @Schema(description = "用户姓名")
    private String username;

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "订单信息")
    private String info;

    @Schema(description = "订单来源 0 lms 1 pmp 2 dddcoder 3 crm ")
    private String type;

    @Schema(description = "支付渠道 微信 支付宝")
    private String PayChannel;

    @Schema(description = "附带数据")
    private String attach;

    @Schema(description = "产品id")
    private String productId;

    @Schema(description = "订单总价")
    private BigDecimal money;

    @Schema(description = "订单状态 0 新建未支付 1 支付完成 ")
    private String status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
    private Date czsj;


    @Schema(description = "当前年度")
    private Integer year;

}

