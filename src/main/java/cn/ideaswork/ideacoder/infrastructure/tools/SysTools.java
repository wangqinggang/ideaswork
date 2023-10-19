package cn.ideaswork.ideacoder.infrastructure.tools;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.auth.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SysTools {
    /**
     * 列表转化成分页列表
     *
     * @param list     list
     * @param pageable pageable
     * @param <T>      T
     * @return
     */
    public static <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    /**
     * 删除文件夹内内容
     *
     * @param directory 目录绝对路径，如 /user/java
     * @throws IOException
     */
    public static void deleteDir(String directory) throws IOException {
        Files.walkFileTree(Path.of(directory), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //遍历文件时删除文件
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                //遍历目录时删除目录
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });

    }

    public static User getLoginUser() {

        UserDetailsImpl userDetails;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userDetails = (UserDetailsImpl) principal;
        } catch (Exception e) {
            return null;
        }

        User user = new User();
        user.setId(userDetails.getId())
                .setEmail(userDetails.getEmail())
                .setRoles(userDetails.getRoles())
                .setName(userDetails.getUsername())
                .setCode(userDetails.getCode())
                .setImgurl(userDetails.getImgUrl())
                .setInfo(userDetails.getInfo())
                .setOpenid(userDetails.getOpenId())
                .setAiCount(userDetails.getAiCount())
                .setVoiceCount(userDetails.getVoiceCount())
                .setTopicCount(userDetails.getTopicCount())
                .setCopyCount(userDetails.getCopyCount())
                .setScriptCount(userDetails.getScriptCount())
                .setStatus(userDetails.getStatus());

        return user;
    }


    /**
     * 生成随机数字验证码
     *
     * @param length 验证码长度
     * @return 验证码字符串
     */
    public static String getRandomCode(int length) {

        // 自定义纯数字的验证码（随机6位数字，可重复）
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", length);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);

        // 生成code
        lineCaptcha.createCode();
        String code = lineCaptcha.getCode();
        return code;
    }

    public static Integer getYear() {
        return java.time.LocalDate.now().getYear();
    }

    /**
     * 获取当前年份的第一天
     * @param year
     * @return
     */
    public static Date getYearFirst(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取当前年份的最后一天
     * @param year
     * @return
     */
    public static Date getYearLast(Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return calendar.getTime();
    }
}
