package cn.ideaswork.ideacoder.domain.coder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 前端传递到后端的值
 *
 * @author 王庆港
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(name = "创建领域对象实体信息", description = "实体属性列表")
public class EntityDTO {
    @Schema(description = "Entity id 标识唯一实体")
    private String entityid;

    @Schema(description = "PackageName 项目包名名称，如 club.ideaworks")
    private String packageName;

    @Schema(description = "Entity 名称")
    private String entityName;

    @Schema(description = "Entity 对应的表")
    private String tableName;

    @Schema(description = "Entity 说明 ApiModel 注解用")
    private String comment;

    @Schema(description = "Entity 详细功能说明")
    private String entityInfo;

    @Schema(description = "Controller 接口说明 API（tags） 注解用")
    private String apiTag;

    @Schema(description = "实体包含的属性列表")
    private List<EntityField> entityFields;
}

