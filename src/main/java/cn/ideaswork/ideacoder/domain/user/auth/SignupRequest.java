package cn.ideaswork.ideacoder.domain.user.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 20,message = "用户名长度必须大于1小于20")
    private String username;

    @NotBlank(message = "验证码不能为空")
    @Size(max = 50)
    @Email(message = "请输入正确的邮箱格式")
    private String email;

//    private Set<String> role;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 40,message = "密码长度必须大于 6 位")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
