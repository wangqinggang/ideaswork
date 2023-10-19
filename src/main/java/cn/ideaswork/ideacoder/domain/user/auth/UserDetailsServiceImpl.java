package cn.ideaswork.ideacoder.domain.user.auth;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserDao userRepository;

    /**
     * 根据用户名查找用户（邮箱唯一，此处参数使用邮箱）
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("没有找到当前账户: " + email));
        if (user.getRoles()==null){
            user.setRoles(new ArrayList<String>());
        }
        return UserDetailsImpl.build(user);
    }

}
