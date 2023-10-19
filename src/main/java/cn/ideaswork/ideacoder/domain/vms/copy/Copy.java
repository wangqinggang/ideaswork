package cn.ideaswork.ideacoder.domain.vms.copy;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "vms_copy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文案")
@Accessors(chain = true)
public final class Copy {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "选题id")
    private String topicId;

    @Schema(description = "选题名称")
    private String topicName;

    @Schema(description = "文案标题")
    private String title;

    @Schema(description = "文案简介")
    private String intro;

    @Schema(description = "文案内容")
    private String content;

    @Schema(description = "引用文稿")
    private String wgurl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间")
    private LocalDate cjsj;

    @Schema(description = "排序号")
    private Integer pxh;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "脚本数量")
    private Integer scriptNum;

    //    脚本拍摄完成
    @Schema(description = "完成脚本数量")
    private Integer finishedScriptNum;

}
