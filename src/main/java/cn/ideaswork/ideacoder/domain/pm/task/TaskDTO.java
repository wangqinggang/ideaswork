package cn.ideaswork.ideacoder.domain.pm.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
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
public class TaskDTO {
    @Schema(description = "任务id")
    private String id;

    @Schema(description = "项目id")
    private String projectid;

    @Schema(description = "用户故事地图id")
    private String storymapid;

    @Schema(description = "用户活动id")
    private String activityid;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "负责人id")
    private String fzrid;

    @Schema(description = "负责人名称")
    private String fzrname;

    @Schema(description = "任务名称")
    private String title;

    @Schema(description = "任务介绍")
    private String intro;

    @Schema(description = "文档链接")
    private String documentUrl;

    @Schema(description = "任务状态")
    private String status;

    @Schema(description = "任务所属版本")
    private String version;

    @Schema(description = "是否正修改标题")
    private Boolean modifyTitle;

    @Schema(description = "是否正修改简介")
    private Boolean modifyIntro;

    @Schema(description = "父任务")
    private String parentTaskId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "开始时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "结束时间")
    private Date finishTime;

    @Schema(description = "预计工时")
    private Integer expectedTime;

    @Schema(description = "已消耗工时")
    private Integer spendTime;

    @Schema(description = "预计还需工时")
    private Integer needTime;
}

