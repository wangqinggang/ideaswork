package cn.ideaswork.ideacoder.domain.sale.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建订单 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户下单对象")
public class CreateOrderDTO {
    @ApiModelProperty(name = "用户 id ")
    private String userid;

    @ApiModelProperty(name = "用户名 ")
    private String username;

    @ApiModelProperty(name = "手机号")
    private String phone;

    @Schema(description = "订单项目")
    private List<OrderItem> items;

    @Schema(description = "办理人id")
    private String blrId;

    @Schema(description = "办理人名称")
    private String blrName;

    @Schema(description = "订单备注")
    private String bz;

    @Schema(description = "支付方式 0 线下 1 线上微信")
    private String payType;
}
