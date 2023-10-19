package cn.ideaswork.ideacoder.domain.pm.storymap;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

// 用户目标
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class UserGoal {
    @Schema(description = "用户故事地图id")
    private String id;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "用户目标")
    private String title;

    @Schema(description = "是否正在修改")
    private Boolean modify;

    @Schema(description = "用户活动步骤")
    private List<UserActivity> activities;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createtime;
}
