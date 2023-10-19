package cn.ideaswork.ideacoder.domain.vms.script;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
//@EqualsAndHashCode(callSuper = false)
public class ScriptExcelDTO {

    @ExcelProperty(index = 0,value = "镜头序号")
    private Integer pxh;

//    @ExcelProperty
//    @Schema(description = "拍摄序号")
//    private Integer psh;

    @ExcelProperty(index = 1,value = "情节")
    private String plot;

    @ExcelProperty(index = 2,value = "场景")
    private String location;

    @ExcelProperty(index = 3,value = "景别")
    private String shotSize;

    @ExcelProperty(index = 4,value = "摄像机角度")
    private String shotAngle;

    @ExcelProperty(index = 5,value = "运镜")
    private String shotMove;

    @ExcelProperty(index = 6,value = "拍摄内容")
    private String content;

    @ExcelProperty(index = 7,value = "解说词或对白")
    private String caption;

    @ExcelProperty(index = 8,value = "拍摄完成")
    private String finished;

//    @Schema(description = "备注")
//    private String bz;

}

