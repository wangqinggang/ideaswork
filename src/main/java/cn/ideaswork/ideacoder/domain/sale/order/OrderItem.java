package cn.ideaswork.ideacoder.domain.sale.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Schema(description = "产品类型 0 lms线下课 1 lms线上课 2 vms月会员 3 vms年会员  ")
    private String type;

    @Schema(description = "产品id")
    private String productId;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "产品价格")
    private BigDecimal productPrice;

    @Schema(description = "产品数量")
    private Integer num;

    // 生成产品类型枚举
    public enum ProductTypeEnum {
        LMS_OFFLINE_COURSE("0", "lms线下课"),
        LMS_ONLINE_COURSE("1", "lms线上课"),
        VMS_MONTH_MEMBER("2", "vms月会员"),
        VMS_YEAR_MEMBER("3", "vms年会员"),
        VMS_SALE_PRODUCT("4", "vms产品");

        private String code;
        private String name;

        ProductTypeEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getNameByCode(String code) {
            for (ProductTypeEnum productTypeEnum : ProductTypeEnum.values()) {
                if (productTypeEnum.getCode().equals(code)) {
                    return productTypeEnum.getName();
                }
            }
            return null;
        }

        public static String getCodeByName(String name) {
            for (ProductTypeEnum productTypeEnum : ProductTypeEnum.values()) {
                if (productTypeEnum.getName().equals(name)) {
                    return productTypeEnum.getCode();
                }
            }
            return null;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

}

