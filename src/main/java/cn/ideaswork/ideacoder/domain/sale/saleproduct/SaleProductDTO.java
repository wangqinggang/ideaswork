package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductDTO {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "产品名")
    private String name;

    @Schema(description = "产品图片url")
    private String imgurl;

    @Schema(description = "产品简介")
    private String info;

    @Schema(description = "是否免费")
    private Boolean isfree;

    @Schema(description = "现价")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "评价得分")
    private Float score;

    @Schema(description = "是否推荐")
    private Boolean isPopular;

    @Schema(description = "产品分类")
    private String classification;

    @Schema(description = "产品特色")
    private List<String> highlights;

    @Schema(description = "产品问答")
    private List<ProductFaqDTO> faqs;

    @Schema(description = "购买数量")
    private Integer buyNum;

    @Schema(description = "库存数量")
    private Integer stockNum;

    @Schema(description = "产品类型：1-课程，2-虚拟物品")
    private String productType;

    @Schema(description = "课时数（分钟）-产品类型为课程时")
    private Integer minutes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date czsj;

    @Schema(description = "是否发布")
    private Boolean isPublished;
}

