package cn.ideaswork.ideacoder.domain.coder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 实体属性列表
 *
 * @author william
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@Schema(name = "实体属性", description = "实体属性列表")
public class EntityField {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "属性数据类型")
    private String dataType;

    @Schema(description = "属性名称")
    private String fieldName;

    @Schema(description = "属性对应数据库字段名称")
    private String tableFieldName;

    @Schema(description = "属性长度（字符串时使用）")
    private Integer length;

    @Schema(description = "属性注释 Schema")
    private String fieldComment;

    @Schema(description = "是否为主键")
    private Boolean isKey;

    @Schema(description = "是否为必填字段")
    private Boolean isRequired;

    @Schema(description = "组件类型")
    private String componentType;

    @Schema(description = "验证类型 string number boolean integer float array url email")
    private String rulesType;

    @Schema(description = "验证提示消息")
    private String ruleMessage;

}
