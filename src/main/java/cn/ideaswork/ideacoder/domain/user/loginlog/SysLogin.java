package cn.ideaswork.ideacoder.domain.user.loginlog;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

//@Document(collection = "sys_login")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Schema(description = "临时登录表")
//@Accessors(chain = true)
public final class SysLogin {
    @Id
    @Schema(description = "id")
    private String id;

    @Schema(description = "openid")
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+8")
    @Schema(description = "登录时间")
    private Date loginTime;
}
