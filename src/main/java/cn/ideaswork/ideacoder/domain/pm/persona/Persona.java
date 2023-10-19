package cn.ideaswork.ideacoder.domain.pm.persona;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PERSONA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户画像")
@Accessors(chain = true)
public final class Persona {
    @Id
    @Schema(description = "主键")
    private String id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "头像地址")
    private String imageUrl;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "职业")
    private String profession;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "语录")
    private String quotations;

    @Schema(description = "教育")
    private String education;

    @Schema(description = "工作状态")
    private String workingStatus;

    @Schema(description = "家庭状态")
    private String familyStatus;

    @Schema(description = "所在城市及生活工作环境")
    private String environment;

    @Schema(description = "兴趣爱好")
    private String hobbies;

    @Schema(description = "对生活社交政治等态度")
    private String attitudeToThings;

    @Schema(description = "愿景、动机、目标")
    private String targets;

    @Schema(description = "互联网使用程度")
    private String usageLevelOfInternet;

    @Schema(description = "硬件产品使用程度")
    private String usageLevelOfHardware;

    @Schema(description = "应用软件使用程度")
    private String usageLevelOfApplication;

    @Schema(description = "对新技术的态度")
    private String attitudeToNewTech;

    @Schema(description = "硬件操作水平")
    private String operantLevelToHardware;

    @Schema(description = "与目标产品服务的关系")
    private String relationshipWithTargetProduct;

    @Schema(description = "对目标产品的态度")
    private String attitudeToTargetProduct;

    @Schema(description = "竞品使用情况")
    private String usageStatusOfCompetingProduct;

    @Schema(description = "产品配件和周边服务需求")
    private String accessoriesAndPeripheral;

    @Schema(description = "产品使用场景")
    private String situationOfUsingProduct;

    @Schema(description = "最常使用的功能")
    private String frequentlyUsedFunction;

    @Schema(description = "当前困难和阻碍")
    private String currentDifficultiesAndObstacles;
}

