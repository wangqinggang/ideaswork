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
public class LoginRequest {
	@NotBlank
	@Email(message = "请输入正确格式的邮箱")
	private String email;

	@NotBlank
	@Size(min = 6, max = 12,message = "密码必须大于 6 位，小于 12 位~")
	private String password;

	@NotBlank(message = "验证码不能为空")
	private String code;
}
