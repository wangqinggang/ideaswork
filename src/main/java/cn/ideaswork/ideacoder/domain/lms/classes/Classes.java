package cn.ideaswork.ideacoder.domain.lms.classes;

import cn.ideaswork.ideacoder.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "CLASSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "班级")
@Accessors(chain = true)
public final class Classes {
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




//    @DBRef
//    @Schema(description = "班级学员")
//    private List<User> students;

//    @DBRef
//    @Schema(description = "排课")
//    private List<String> arrangement; // 课程 所有课程小节 班级 教师 教室 时间

//    @DBRef
//    @Schema(description = "班级签到")
//    private List<String> signIn; // 班级 课程 节次 教室 教师 用户id 用户名 联系方式 迟到、正常、缺勤、请假

//    @Schema(description = "已上课时") // TODO 添加签到时增加
//    private Integer attendSectionNum;
//
//    @Schema(description = "总课时")
//    private Integer maxSectionNum; // 关联课程时添加

    // ---------------------------------------------------------------


}
