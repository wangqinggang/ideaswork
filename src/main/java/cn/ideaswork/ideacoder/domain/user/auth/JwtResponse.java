package cn.ideaswork.ideacoder.domain.user.auth;

import cn.ideaswork.ideacoder.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String openId;
    private String imgUrl;
    private String code;
    private String emailCode;
    private String info;
    private String status;
    private List<String> roles;

    public JwtResponse(String accessToken, UserDetailsImpl userDetails, List<String> roles) {
        this.token = accessToken;
        this.id = userDetails.getId();
        this.username = userDetails.getUsername();
        this.email = userDetails.getEmail();
        this.openId = userDetails.getOpenId();
        this.imgUrl = userDetails.getImgUrl();
        this.code = userDetails.getCode();
        this.emailCode = userDetails.getEmailCode();
        this.info = userDetails.getInfo();
        this.status = userDetails.getStatus();
        this.roles = roles;
    }

    public JwtResponse(String accessToken, User user){
        this.token = accessToken;
        this.id = user.getId();
        this.username = user.getName();
        this.email = user.getEmail();
        this.openId = user.getOpenid();
        this.imgUrl = user.getImgurl();
        this.code = user.getCode();
        this.emailCode = user.getEmailCode();
        this.info = user.getInfo();
        this.status = user.getStatus();
        this.roles = user.getRoles();
    }
}
