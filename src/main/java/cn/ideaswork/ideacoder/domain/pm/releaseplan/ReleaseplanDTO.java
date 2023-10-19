package cn.ideaswork.ideacoder.domain.pm.releaseplan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
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
public class ReleaseplanDTO {
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

  @Schema(
      description = "开始时间"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd hh:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd hh:mm:ss",
      timezone = "GTM+8"
  )
  private Date starttime;

  @Schema(
      description = "预计发布时间"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd hh:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd hh:mm:ss",
      timezone = "GTM+8"
  )
  private Date expectedendtime;

  @Schema(
      description = "实际发布时间"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd hh:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd hh:mm:ss",
      timezone = "GTM+8"
  )
  private Date actualreleasetime;

  @Schema(
      description = "创建时间"
  )
  @DateTimeFormat(
      pattern = "yyyy-MM-dd hh:mm:ss"
  )
  @JsonFormat(
      pattern = "yyyy-MM-dd hh:mm:ss",
      timezone = "GTM+8"
  )
  private Date createtime;
}
