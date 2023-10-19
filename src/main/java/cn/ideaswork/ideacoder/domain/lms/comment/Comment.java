package cn.ideaswork.ideacoder.domain.lms.comment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "COMMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论")
@Accessors(chain = true)
public final class Comment {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "业务id")
    private String ywid;

    @Schema(description = "业务表")
    private String tableName;

    @Schema(description = "评论人id")
    private String plrid;

    @Schema(description = "评论人名称")
    private String plrname;

    @Schema(description = "头像地址")
    private String avatarUrl;

    @Schema(description = "得分")
    private Integer score;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论时间")
    private LocalDate plsj;

    @Schema(description = "是否审批通过")
    private Boolean isPublic;
}

