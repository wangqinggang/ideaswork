package cn.ideaswork.ideacoder.domain.lms.arrangement;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.time.LocalDate;
import java.time.LocalTime;
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
public class ArrangementDTO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "班级id")
    private String classesId;

    @Schema(description = "班级名称")
    private String classesName;

    @Schema(description = "课程id")
    private String courseId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课时id")
    private String sectionid;

    @Schema(description = "课时名称")
    private String sectionName;

    @Schema(description = "教师id")
    private String teacherId;

    @Schema(description = "教师名称")
    private String teacherName;

    @Schema(description = "教室id")
    private String roomId;

    @Schema(description = "教室名称")
    private String roomName;

    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Schema(description = "日期")
    private LocalDate day;

    @Schema(description = "状态 0 排课 1 完成排课 2 已上 3 未上")
    private String status;
}

