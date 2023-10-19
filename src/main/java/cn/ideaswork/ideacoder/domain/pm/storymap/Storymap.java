package cn.ideaswork.ideacoder.domain.pm.storymap;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "STORYMAP")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户故事地图")
@Accessors(chain = true)
public final class Storymap {
    @Id
    @Schema(description = "用户故事地图id")
    private String id;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "关联产品id")
    private String productid;

    @Schema(description = "项目关联发布计划id")
    private String releaseid;

    @Schema(description = "故事地图标题")
    private String title;

    @Schema(description = "用户故事地图简介")
    private String intro;

    @Schema(description = "用户故事地图简介")
    private List<UserGoal> goals;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createtime;
}
