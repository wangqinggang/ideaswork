package cn.ideaswork.ideacoder.domain.user.auth;

import cn.ideaswork.ideacoder.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String openId;
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String emailCode;
    @Getter
    @Setter
    private String imgUrl;
    @Getter
    @Setter
    private String info;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private Integer aiCount;

    @Getter
    @Setter
    private Integer voiceCount;

    @Getter
    @Setter
    private Integer topicCount;

    @Getter
    @Setter
    private Integer copyCount;

    @Getter
    @Setter
    private Integer scriptCount;

    @Getter
    @Setter
    private List<String> roles;

    @JsonIgnore
    @Getter
    @Setter
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.openId = user.getOpenid();
        this.username = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.imgUrl = user.getImgurl();
        this.code = user.getCode();
        this.emailCode = user.getEmailCode();
        this.status = user.getStatus();
        this.info = user.getInfo();
        this.roles= user.getRoles();
        this.authorities = authorities;
        this.aiCount = user.getAiCount();
        this.voiceCount = user.getVoiceCount();
        this.topicCount = user.getTopicCount();
        this.copyCount = user.getCopyCount();
        this.scriptCount = user.getScriptCount();
    }

    public static UserDetailsImpl build(User user) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (user.getRoles().size() != 0) {
            authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        }
//        System.out.println(authorities);

        return new UserDetailsImpl(user, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
