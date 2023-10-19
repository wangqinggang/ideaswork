package cn.ideaswork.ideacoder.domain.pm.product;

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

@Document(collection = "PRODUCT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "产品")
@Accessors(chain = true)
public final class Product {
    @Id
    @Schema(description = "产品id")
    private String id;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "创建人名称")
    private String userName;

    @Schema(description = "用户画像id")
    private String personaids;

    @Schema(description = "用户画像名称")
    private String personaName;

    @Schema(description = "问题id")
    private String problemid;

    @Schema(description = "问题名称")
    private String problemName;

    @Schema(description = "产品名")
    private String name;

    @Schema(description = "产品图片")
    private String imgUrl;

    @Schema(description = "产品简介")
    private String intro;

    @Schema(description = "产品方案")
    private String solution;

    @Schema(description = "如何改变的现状")
    private String pfunction;

    @Schema(description = "替代产品的解决方法")
    private String replacement;

    @Schema(description = "目标用户客户")
    private String targetuser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createtime;

 // --------------------------------
    @Schema(description = "是否免费")
    private Boolean isfree;

    @Schema(description = "现价")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "评价得分")
    private Float score;

    @Schema(description = "学习者数量")
    private Integer learnerNum;

    @Schema(description = "是否推荐")
    private Boolean isPopular;

    @Schema(description = "项目分类")
    private String projectClassification;

    @Schema(description = "是否发布")
    private Boolean isPublished;

    @Schema(description = "时间线")
    private List<TimeLine> timeLineList;

}

