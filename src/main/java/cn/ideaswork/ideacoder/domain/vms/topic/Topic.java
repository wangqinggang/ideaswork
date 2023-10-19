package cn.ideaswork.ideacoder.domain.vms.topic;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "vms_topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "主题")
@Accessors(chain = true)
public final class Topic {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "主题名")
    private String name;

    @Schema(description = "主题目标")
    private String target;

    @Schema(description = "目标人群")
    private String custom;

    @Schema(description = "内容大纲")
    private String outline;

    @Schema(description = "排序号")
    private Integer pxh;

    @Schema(description = "文案数")
    private Integer copyNum;

    @Schema(description = "完成文案数")
    private Integer copyFinishedNum;

    @Schema(description = "状态")
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date cjsj;

    @Schema(description = "是否完成")
    private Boolean isFinished;
}

