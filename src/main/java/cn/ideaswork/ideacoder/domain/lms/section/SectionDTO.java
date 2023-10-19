package cn.ideaswork.ideacoder.domain.lms.section;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.String;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
  @Schema(description = "主键")
  private String id;

  @Schema(description = "课程id")
  private String courseId;

  @Schema(description = "课时名")
  private String name;

  @Schema(description = "文稿地址（cms文章）")
  private String wgurl;

  @Schema(description = "课时简介")
  private String info;

  @Schema(description = "视频id")
  private String vid;

  @Schema(description = "课时时长")
  private String minites;

  @Schema(description = "排序号")
  private Integer pxh;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
  @Schema(description = "创建时间")
  private Date czsj;
}
