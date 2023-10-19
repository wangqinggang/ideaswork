package cn.ideaswork.ideacoder.domain.lms.feedback;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "FEEDBACK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "反馈")
@Accessors(chain = true)
public final class Feedback {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "主题")
    private String subject;

    @Schema(description = "内容")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date cjsj;

    @Schema(description = "已读")
    private Boolean read;
}
