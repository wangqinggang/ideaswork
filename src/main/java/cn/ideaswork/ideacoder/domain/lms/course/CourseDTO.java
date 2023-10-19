package cn.ideaswork.ideacoder.domain.lms.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Boolean;
import java.lang.Float;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "课程名")
    private String name;

    @Schema(description = "课程图片url")
    private String imgurl;

    @Schema(description = "课程简介")
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

    @Schema(description = "课程分类")
    private String classification;

    @Schema(description = "课程特色")
    private List<String> highlights;

    @Schema(description = "课程问答")
    private List<FaqDTO> faqs;

    @Schema(description = "学习者数量")
    private Integer learnerNum;

    @Schema(description = "课时数（分钟）")
    private Integer minutes;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
    private Date czsj;

    @Schema(description = "是否发布")
    private Boolean isPublished;
}

