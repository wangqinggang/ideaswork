package cn.ideaswork.ideacoder.domain.user.auth;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Console;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserDao;
import cn.ideaswork.ideacoder.domain.user.UserService;
import cn.ideaswork.ideacoder.infrastructure.config.WxPayConfig;
import cn.ideaswork.ideacoder.infrastructure.email.MailService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yungouos.pay.entity.WxOauthInfo;
import com.yungouos.pay.entity.WxUserInfo;
import com.yungouos.pay.entity.WxWebLoginBiz;
import com.yungouos.pay.wxapi.WxApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@Api(tags = "用户登录和注册")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDao userRepository;

    @Autowired
    WxPayConfig wxPayConfig;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MailService mailService;

    @GetMapping("/sendEmailCode")
    @ApiOperation("发送邮箱图片验证码")
    public ResponseEntity<?> sendEmailCode(String email) throws IOException {
        if (StringUtils.isBlank(email)) {
            return ResponseEntity
                    .badRequest()
                    .body("错误: 请检查邮箱是否正确!");
        }

        String randomCode = SysTools.getRandomCode(6);

        // ①已经存在账户则查看是否有密码，有说明已经注册
        if (userRepository.existsByEmail(email)) {
            User userDb = userService.getUserByEmail(email);

            if (userDb.getPassword() != null || StringUtils.isNotBlank(userDb.getPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body("您的账户已经存在，请直接登录!");
            } else {
                if (StringUtils.isNotBlank(userDb.getId())) {
                    userDb
                            .setEmail(email)
                            .setEmailCode(randomCode)
                            .setStatus("0")
                            .setRoles(Arrays.asList("ROLE_STUDENT"))
                            .setZcsj(new Date());
                    userService.updateUserById(userDb, userDb.getId());
                }
            }


        } else {
            User user = new User();
            user.setId(UUID.randomUUID().toString())
                    .setEmail(email)
                    .setEmailCode(randomCode)
                    .setStatus("0")
                    .setRoles(Arrays.asList("ROLE_STUDENT"))
                    .setZcsj(new Date());
            userService.saveUser(user);
        }

        try {
            mailService.sendTemplate("您的注册验证码", email, "您的网站验证码为  " + randomCode);
        } catch (MessagingException e) {
            return ResponseEntity
                    .badRequest()
                    .body("错误: 请检查邮箱是否正确!");
        }
        return ResponseEntity
                .ok("发送成功，请查看您的邮箱！");
    }

    @GetMapping("/capatcha")
    @ApiOperation("获取图形验证码")
    public void getCapatcha(String email, String time, HttpServletResponse response) throws IOException {

        //定义图形验证码的长和宽
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);

        //输出code
        Console.log(captcha.getCode());
        Console.log(time);
        //验证图形验证码的有效性，返回boolean值
//        boolean verify = captcha.verify("1234");
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        outputStream.close();
        User userDb = userService.getUserByEmail(email);
        if (userDb.getId() != null) {
            userDb.setCode(captcha.getCode());
            userService.updateUserCode(userDb);
        }
    }


    /**
     * 登录
     *
     * @param loginRequest 登录请求参数
     * @return JwtResponse
     */
    @PostMapping("/signin")
    @ApiOperation("用户登录")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity
                    .badRequest()
                    .body("账号不存在，请注册!");
        }
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            if (loginRequest.getCode().equals(userDetails.getCode())) {
                return ResponseEntity.ok(new JwtResponse(jwt, userDetails, roles));
            } else {
                return ResponseEntity.badRequest().body("验证码错误");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("用户名或密码错误");
        }
    }

    /**
     * 注册
     *
     * @param signUpRequest 注册请求参数
     * @return MessageResponse
     */
    @PostMapping("/signup")
    @ApiOperation("用户注册")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            User userDb = userService.getUserByEmail(signUpRequest.getEmail());
            if (userDb.getPassword() != null || StringUtils.isNotBlank(userDb.getPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body("已经注册邮箱，请直接登录!");
            } else {
                if (StringUtils.isBlank(signUpRequest.getCode())) {
                    return ResponseEntity
                            .badRequest()
                            .body("请输入验证码");
                }
                if (signUpRequest.getCode().equals(userDb.getEmailCode())) {
                    userDb.setName(signUpRequest.getUsername())
                            .setEmail(signUpRequest.getEmail())
                            .setPassword(encoder.encode(signUpRequest.getPassword()))
                            .setStatus("1")
                            .setRoles(Arrays.asList("ROLE_STUDENT"))
                            .setZcsj(new Date())
                            .setInfo("");
                    userService.updateUserById(userDb, userDb.getId());
                } else {
                    return ResponseEntity
                            .badRequest()
                            .body("验证码错误，请重试!");
                }
            }
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("请先发送邮箱验证码信息!");
        }
        return ResponseEntity.ok("注册成功！请登录");
    }

    /**
     * 修改密码
     *
     * @param modifyInfoRequest 修改密码参数
     * @return MessageResponse
     */
    @PostMapping("/modifyInfo")
    @ApiOperation("修改用户密码")
    public ResponseEntity<?> modifyUser(@RequestBody ModifyInfoRequest modifyInfoRequest) {
        if (userRepository.existsByEmail(modifyInfoRequest.getEmail())) {


            Optional<User> userByEmail = userRepository.findByEmail(modifyInfoRequest.getEmail());
            if (userByEmail.isPresent()) {

                User user = userByEmail.get();

                if (modifyInfoRequest.getCode().equals(user.getCode())) {


                    user.setPassword(encoder.encode(modifyInfoRequest.getPassword()));

                    try {
                        mailService.sendTemplate("修改密码成功", modifyInfoRequest.getEmail(), "您已经修改密码。若遇到其他问题请添加站长微信解决 wx：wqg599252594  ");
                    } catch (MessagingException e) {
                        return ResponseEntity
                                .badRequest()
                                .body("错误: 请检查邮箱是否正确!");
                    }

                    userRepository.save(user);
                } else {
                    String randomCode = SysTools.getRandomCode(6);
                    user.setCode(randomCode);

                    try {
                        mailService.sendTemplate("您正在修改密码，验证码为", modifyInfoRequest.getEmail(), "您的网站验证码为  " + randomCode);
                        userRepository.save(user);
                    } catch (MessagingException e) {
                        return ResponseEntity
                                .badRequest()
                                .body("错误: 请检查邮箱是否正确!");
                    }
                    return ResponseEntity
                            .badRequest()
                            .body("验证码错误，请输入已经发送新的验证码!");
                }
            }

            return ResponseEntity.ok("修改成功！请重新登录");
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("错误: 请检查邮箱是否正确!");
        }
    }

    /**
     * 获取微信授权登录链接
     */
    @GetMapping("/getWechatLoginUrl")
    @ApiOperation("获取微信扫码登录链接")
    public ResponseEntity<String> getWechatLoginUrl(@RequestParam(required = false) String loginId) {
//            String callback_url = wxPayConfig.notify_url + "/api/auth/wxlogin/notify";// 登录完成后异步回调的地址
            String callback_url =  wxPayConfig.wxlogin_callback_url;// 登录完成后异步回调的地址
            String mchId = wxPayConfig.mchId;
            String key = wxPayConfig.key;
            // 定义一个JSON参数对象
            JSONObject param = new JSONObject();
            // 设置唯一登录参数
            param.put("loginId", loginId);
//        System.out.println(loginId);
//        param.put("body", body);
//        WxWebLoginBiz webLogin = WxApi.getWebLogin(mchId, callback_url, param, key);

        String wxOauthUrl = WxApi.getWxOauthUrl(mchId, callback_url, "open-url", param, key);

       return ResponseEntity.ok(wxOauthUrl);
    }

    /**
     * 微信授权登录回调
     */
    @GetMapping("/getWechatUserInfo")
    @ApiOperation("微信授权登录回调")
    @CrossOrigin
    public ResponseEntity<?> callbackNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
//        String loginid = request.getParameter("loginId");
        System.out.println(code);
        String mchId = wxPayConfig.mchId;
        String key = wxPayConfig.key;
        WxOauthInfo wxOauthInfo = WxApi.getWxOauthInfo(mchId, code, key);
        // 根据信息创建用户
        WxUserInfo wxUserInfo = wxOauthInfo.getWxUserInfo();
        // 查找openid是否存在
        Optional<User> dbUser = userRepository.findByOpenid(wxOauthInfo.getOpenId());
        if (dbUser.isPresent()){
            User user = dbUser.get();
            user.setImgurl(wxUserInfo.getHeadimgurl());
            // 生成token
            String jwt = jwtUtils.generateJwtTokenByEmail(user.getEmail());
            // 查找当前用户的所有角色
            return ResponseEntity.ok(new JwtResponse(jwt, user));
        }else{
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setOpenid(wxOauthInfo.getOpenId());
            user.setEmail(wxOauthInfo.getOpenId());
            user.setName(wxUserInfo.getNickname());
            user.setAiCount(50);
            user.setVoiceCount(30);
            user.setImgurl(wxUserInfo.getHeadimgurl());
            user.setRoles(Arrays.asList("ROLE_STUDENT"));
            user.setStatus("1");
            user.setZcsj(new Date());
            user.setInfo("微信用户");
            User save = userRepository.save(user);
            String jwt = jwtUtils.generateJwtTokenByEmail(save.getOpenid());
            return ResponseEntity.ok(new JwtResponse(jwt, save));
        }
    }
}
