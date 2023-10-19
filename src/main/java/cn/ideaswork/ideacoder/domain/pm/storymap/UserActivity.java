package cn.ideaswork.ideacoder.domain.pm.storymap;

import cn.ideaswork.ideacoder.domain.pm.task.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

// 完成用户目标所需要的活动
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class UserActivity {
    @Schema(description = "id")
    private String id;

    @Schema(description = "用户活动")
    private String title;

    @Schema(description = "用户活动简介")
    private String intro;

    @Schema(description = "是否正在修改活动标题")
    private Boolean modifyTitle;

    @Schema(description = "是否正在修改活动内容")
    private Boolean modifyIntro;

    @DBRef
    @Schema(description = "未规划的活动任务")
    private List<Task> childTasks;

    @DBRef
    @Schema(description = "mvp发布计划活动任务")
    private List<Task> mvpTasks;

    @DBRef(lazy = true)
    @Schema(description = "中期发布计划活动任务")
    private List<Task> middleTasks;

    @DBRef(lazy = true)
    @Schema(description = "末期发布计划活动任务")
    private List<Task> endTasks;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createtime;
}
