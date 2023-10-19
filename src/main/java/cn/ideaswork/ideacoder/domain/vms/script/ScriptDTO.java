package cn.ideaswork.ideacoder.domain.vms.script;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScriptDTO {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "主键")
    private String userId;

    @Schema(description = "文案id")
    private String copyId;

    @Schema(description = "文案名称")
    private String copyName;

    @Schema(description = "场景")
    private String location;

    @Schema(description = "场景号")
    private Integer scene;

    @Schema(description = "情节")
    private String plot;

    @Schema(description = "镜号")
    private Integer shot;

    @Schema(description = "景别")
    private String shotSize;

    @Schema(description = "摄像机角度")
    private String shotAngle;

    @Schema(description = "运镜")
    private String shotMove;

    @Schema(description = "拍摄内容")
    private String content;

    @Schema(description = "解说词或对白")
    private String caption;

    @Schema(description = "存储号（卷+相机存储号）")
    private String cch;

    @Schema(description = "备注")
    private String bz;

    @Schema(description = "是否完成")
    private Boolean finished;

    @Schema(description = "排序号")
    private Integer pxh;

    @Schema(description = "拍摄号")
    private Integer psh;

//    @Schema(description = "状态")
//    private String status;

    @Schema(description = "是否按排序号排列")
    private Boolean orderByPxh;
}

