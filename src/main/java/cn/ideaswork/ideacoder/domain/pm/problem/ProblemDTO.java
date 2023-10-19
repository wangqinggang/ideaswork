package cn.ideaswork.ideacoder.domain.pm.problem;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDTO {
    @Schema(description = "问题id")
    private String id;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "创建人名称")
    private String userName;

    @Schema(description = "用户画像id")
    private String personaid;

    @Schema(description = "用户画像名称")
    private String personaName;

    @Schema(description = "产品id")
    private String productid;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "问题痛点")
    private String painspot;

    @Schema(description = "存在原因")
    private String reason;

    @Schema(description = "要提升的乐趣")
    private String enjoyment;

    @Schema(description = "现有解决方案")
    private String solution;

    @Schema(description = "机会")
    private String opportunity;

    @Schema(description = "备注")
    private String bz;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GTM+8")
    private Date createtime;
}

