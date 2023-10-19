package cn.ideaswork.ideacoder.domain.sale.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单")
@Accessors(chain = true)
public final class Orders {
    @Id
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

    @Schema(description = "用户姓名")
    private String username;

    @Schema(description = "用户手机号")
    private String phone;

    @Schema(description = "用户微信 openid ")
    private String openid;

    @Schema(description = "订单信息")
    private String info;

    @Schema(description = "订单来源 0 lms 1 pms 2 vms 3 crm ")
    private String type;

    @Schema(description = "支付渠道 微信 支付宝")
    private String PayChannel;

    @Schema(description = "附带数据")
    private String attach;

    @Schema(description = "订单项目")
    private List<OrderItem> items;

    @Schema(description = "办理人id")
    private String blrId;

    @Schema(description = "办理人名称")
    private String blrName;

    @Schema(description = "订单总价")
    private BigDecimal money;

    @Schema(description = "订单备注")
    private String bz;

    @Schema(description = "订单状态 0 新建未支付 1 支付完成 ")
    private String status;

    @Schema(description = "发货状态 0 未发货 1 已发货 2 已收货 3 已退货 4 已退款 5 已取消")
    private String shippingStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date czsj;

    // 发货状态枚举类
    public enum ShippingStatus {
        UNDELIVERED("0", "未发货"),
        DELIVERED("1", "已发货"),
        RECEIVED("2", "已收货"),
        RETURNED("3", "已退货"),
        REFUNDED("4", "已退款"),
        CANCELED("5", "已取消");

        private String code;
        private String name;

        ShippingStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(String code) {
            for (ShippingStatus c : ShippingStatus.values()) {
                if (c.getCode() == code) {
                    return c.name;
                }
            }
            return null;
        }

        public static String getCode(String name) {
            for (ShippingStatus c : ShippingStatus.values()) {
                if (c.getName().equals(name)) {
                    return c.code;
                }
            }
            return "-1";
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 订单来源枚举类
    public enum OrderType {
        LMS("0", "lms"),
        PMS("1", "pms"),
        VMS("2", "vms"),
        CRM("3", "crm");

        private String code;
        private String name;

        OrderType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(String code) {
            for (OrderType c : OrderType.values()) {
                if (c.getCode() == code) {
                    return c.name;
                }
            }
            return null;
        }

        public static String getCode(String name) {
            for (OrderType c : OrderType.values()) {
                if (c.getName().equals(name)) {
                    return c.code;
                }
            }
            return "-1";
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    // 订单状态枚举
    public enum OrderStatus {
        NEW("0", "新建未支付"),
        PAYED("1", "支付完成");

        private String code;
        private String name;

        OrderStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(String code) {
            for (OrderStatus c : OrderStatus.values()) {
                if (c.getCode() == code) {
                    return c.name;
                }
            }
            return null;
        }

        public static String getCode(String name) {
            for (OrderStatus c : OrderStatus.values()) {
                if (c.getName().equals(name)) {
                    return c.code;
                }
            }
            return "-1";
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
