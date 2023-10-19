package cn.ideaswork.ideacoder.domain.lms.classes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassesDTO {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "班级名称")
    private String name;

    // 一个班级关联一个课程
    @Schema(description = "课程id")
    private String courseId;

    @Schema(description = "课程名")
    private String courseName;

    // 一个班级关联一个老师
    @Schema(description = "教师id")
    private String teacherId;

    @Schema(description = "教师名")
    private String teacherName;

    @Schema(description = "入班人数") // TODO 添加学员时新增，移除学员时减少
    private Integer inNum;

    @Schema(description = "满班人数")
    private Integer maxNum;

    @Schema(description = "状态 0 未开班 1 已开班 2 已结班")
    private String status; // 签到完成后更改

    @Schema(description = "年度")
    private Integer nd;// 2022

    @Schema(description = "季度")
    private String jd; // 春、夏、秋、冬

    @Schema(description = "开班时间")
    private LocalDate kbsj;

    @Schema(description = "结班时间")
    private LocalDate jbsj;

}

