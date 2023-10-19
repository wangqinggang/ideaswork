package cn.ideaswork.ideacoder.domain.pm.idea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "IDEA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创意")
@Accessors(chain = true)
public final class Idea {
    @Id
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "创建时间")
    private Date createtime;
}

