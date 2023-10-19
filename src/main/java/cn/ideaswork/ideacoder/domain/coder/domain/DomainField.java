package cn.ideaswork.ideacoder.domain.coder.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;


@Data
@Schema( title = "实体属性",description = "实体所包含的实体属性")
@Accessors(chain = true)
public class DomainField {

    @Id
    @Schema(title = "主键")
    private String id;

    @Schema(title = "对应实体主键（多对一）")
    private String domainId;

    @Schema(title = "属性名称小写英文")
    private String fieldName;

    @Schema(title = "属性名称中文")
    private String fieldNameCN;

    @Schema(title = "表字段名称英文")
    private String tableFieldName;

    @Schema(title = "数据类型")
    private String dataType;

    @Schema(title = "数据长度（String）")
    private Integer length;

    @Schema(title = "是否为主键")
    private Boolean isKey;

    @Schema(title = "是否必须")
    private Boolean isRequired;

    @Schema(title = "前端组件类型")
    private String componentType;

    @Schema(title = "字段验证类型")
    private String rulesType;

    @Schema(title = "字段验证提示消息")
    private String ruleMessage;

}

