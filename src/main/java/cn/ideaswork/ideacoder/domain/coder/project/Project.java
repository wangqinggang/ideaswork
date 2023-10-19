package cn.ideaswork.ideacoder.domain.coder.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document
@Schema(name = "项目表", description = "Java 项目或者 vue 项目表")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public final class Project {
    @Id
    @Schema(description = "主键：领域项目主键")
    private String id;

    @Schema(description = "用户id：当前项目的创建人id")
    private String userId;

    @Schema(description = "Java 工程名称英文")
    private String enname;

    @Schema(description = "Java 工程名称中文")
    private String cnname;

    @Schema(description = "vue 工程名称英文")
    private String vuename;

    @Schema(description = "package 项目包名：如 cn.ideaswork")
    private String packageGroup;

    @Schema(description = "artifact 构件名：如 ideaboss")
    private String artifact;

    @Schema(description = "工程描述信息：如 该项目为学习管理系统")
    private String info;

    @Schema(description = "打包类型：如 jar 或 war")
    private String packageType;

    @Schema(description = "java 版本：如 1.8 或 11")
    private String javaVersion;

    @Schema(description = "当前java项目状态：0 草稿 1 发布，如 0 或 1")
    private String status;

    @Schema(description = "后端项目端口：如 8080")
    private String backEndPort;

    @Schema(description = "前端项目端口：如 80")
    private String frontEndPort;

    @Schema(description = "是否公开项目：可以公开到领域市场")
    private Boolean bePublic;

    @Schema(description = "当前项目创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
