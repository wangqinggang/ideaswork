package cn.ideaswork.ideacoder.domain.lms.project;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LmsProjectDTO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "项目图片")
    private String imgUrl;

    @Schema(description = "项目简介")
    private String intro;

    @Schema(description = "负责人id")
    private String fzrid;

    @Schema(description = "负责人名称")
    private String fzrName;

    @Schema(description = "项目内容")
    private String content;

    @Schema(description = "已用工时")
    private Integer spentTime;

    @Schema(description = "还需工时")
    private Integer needTime;

    @Schema(description = "项目状态 0 开始 1 MVP 2 版本2 3 版本3")
    private String status;

    @Schema(description = "项目里程碑")
    private String milestone;

    @Schema(description = "附件名称")
    private String fjName;

    @Schema(description = "附件url")
    private String fjUrl;

    @Schema(description = "开始时间")
    private LocalDate startDate;

    @Schema(description = "结束时间")
    private LocalDate endDate;
}

