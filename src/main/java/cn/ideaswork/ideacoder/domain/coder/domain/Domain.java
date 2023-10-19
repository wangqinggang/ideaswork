package cn.ideaswork.ideacoder.domain.coder.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Data
@Document
@Accessors(chain = true)
@Schema(title = "领域对象表",description = "领域对象相关")
@AllArgsConstructor
@NoArgsConstructor
public final class Domain {
    @Id
    @Schema(title = "主键",description = "")
    private String id;

    @Schema(title = "用户id",description = "当前领域对象所属用户（判断是否能修改）")
    private String userid;

    @Schema(title = "所属项目id",description = "一个项目对应多个 domain 实体")
    private String projectId;

    @Schema(title = "实体名称英文",description = "首字母大写，实体名称英文")
    private String domainName;

    @Schema(title = "实体名中文",description = "实体名称中文")
    private String domainNameCN;

    @Schema(title = "数据库表名英文",description = "数据库表名英文")
    private String tableName;

    @Schema(title = "实体说明",description = "实体作用说明")
    private String info;

    @Schema(title = "建表语句",description = "实体建表语句")
    private String sql;

    @Schema(title = "属性列表",description = "当前实体包含的所有属性")
    private List<DomainField> domainFieldList;

}
