package cn.ideaswork.ideacoder.domain.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.String;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Schema(description = "用户id")
    private String id;

    @Schema(description = "用户微信openid")
    private String openid;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "名字")
    private String name;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "邮箱验证码")
    private String emailCode;

    @Schema(description = "用户微信头像url")
    private String imgurl;

    @Schema(description = "用户简介")
    private String info;

    @Schema(description = "状态 0 未激活 1 激活")
    private String status;

    @Schema(description = "用户角色")
    private List<String> roles;

    @Schema(description = "我的课程")
    private List<String> myCourses;

    @Schema(description = "ai辅助剩余用量")
    private Integer aiCount;

    @Schema(description = "语音生成剩余用量")
    private Integer voiceCount;

    @Schema(description = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GTM+8")
    private Date zcsj;
}

