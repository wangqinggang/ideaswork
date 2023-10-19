package cn.ideaswork.ideacoder.domain.pm.releaseplan;

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

@Document(
    collection = "RELEASEPLAN"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "发布计划"
)
@Accessors(
    chain = true
)
public final class Releaseplan {
  @Id
  @Schema(
      description = "发布计划id"
  )
  private String id;

  @Schema(
      description = "创建人id"
  )
  private String userid;

  @Schema(
      description = "产品id"
  )
  private String productid;

  @Schema(
      description = "版本号"
  )
  private String version;

  @Schema(
      description = "主要发布内容"
  )
  private String content;

  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "GTM+8"
  )
  @Schema(
      description = "开始时间"
  )
  private Date starttime;

  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "GTM+8"
  )
  @Schema(
      description = "预计发布时间"
  )
  private Date expectedendtime;

  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "GTM+8"
  )
  @Schema(
      description = "实际发布时间"
  )
  private Date actualreleasetime;

  @DateTimeFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "GTM+8"
  )
  @Schema(
      description = "创建时间"
  )
  private Date createtime;
}
