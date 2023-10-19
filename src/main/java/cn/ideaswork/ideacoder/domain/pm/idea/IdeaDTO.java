package cn.ideaswork.ideacoder.domain.pm.idea;

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
public class IdeaDTO {
    @Schema(description = "创意id")
    private String id;

    @Schema(description = "创建人id")
    private String userid;

    @Schema(description = "创建人名称")
    private String userName;

    @Schema(description = "产品id")
    private String productid;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "问题id")
    private String problemid;

    @Schema(description = "问题名称")
    private String problemName;

    @Schema(description = "产品想法")
    private String idea;

    @Schema(description = "目标公司客户")
    private String customer;

    @Schema(description = "目标用户角色")
    private String useRole;

    @Schema(description = "购买使用动机")
    private String motivation;

    @Schema(description = "开发产品收益")
    private String reason;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GTM+8")
    private Date createtime;
}

