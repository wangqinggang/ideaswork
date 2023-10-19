package cn.ideaswork.ideacoder.infrastructure.config;

import cn.ideaswork.ideacoder.domain.user.auth.AuthEntryPointJwt;
import cn.ideaswork.ideacoder.domain.user.auth.AuthTokenFilter;
import cn.ideaswork.ideacoder.domain.user.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // https://stackoverflow.com/questions/39152803/spring-websecurity-ignoring-doesnt-ignore-custom-filter
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/auth/signin").permitAll()
                .antMatchers("/api/auth/signup").permitAll()
                .antMatchers("/api/auth/sendEmailCode").permitAll()
                .antMatchers("/api/auth/getWechatLoginUrl").permitAll()
                .antMatchers("/api/auth/getWechatUserInfo").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                // lms 课程相关
                .antMatchers("/courses/getPageList").permitAll()
                .antMatchers("/courses/**").permitAll()
                .antMatchers("/sections/**").permitAll()
                .antMatchers("/vod/studentPlay/**").permitAll()
                .antMatchers("/speech/**").permitAll()
                // 订单相关
                .antMatchers("/orderss/createOrder").permitAll()
                .antMatchers("/orderss/createSaleProductOrder").permitAll()

                // sale 产品相关
                .antMatchers("/saleProduct/getPageList").permitAll()
                .antMatchers("/saleProduct/**").permitAll()
//                .antMatchers("/orderss/**").permitAll()
                // pms 产品相关
                .antMatchers("/products/getPageList").permitAll()
//                .antMatchers("/products/**").permitAll()
//                .antMatchers("/**").permitAll()

                // ai 内容相关
                .antMatchers("/ai/**").permitAll()
                // 用户信息相关
//                .antMatchers("/users").permitAll()
//                .antMatchers("/users/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // 解决 h2 数据库页面无法访问的问题
//        http.headers().frameOptions().sameOrigin();
    }
    // 解决登录接口也执行 token 过滤链
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/auth/**");
    }


}
