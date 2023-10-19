package cn.ideaswork.ideacoder;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainField;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.lms.vod.VodService;
import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import cn.ideaswork.ideacoder.infrastructure.tools.SignatureQcloudTools;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class IdeacoderApplicationTests1 {

    @Autowired
    VodService vodService;
    @Autowired
    DomainService domainService;

    @Test
    void generateXml() {
        String domainName = "User";
        Domain domain = new Domain();
        domain.setDomainName(domainName);
        domain.setDomainNameCN("用户表");
        domain.setTableName("user");

        List<DomainField> domainFields = new ArrayList<>();

        /**
         * string->VARCHAR
         * date->DATE
         * int->INTEGER
         * double->DOUBLE
         * float->FLOAT
         * timestamp->TIMESTAMP
         * BigDecimal->DECIMAL
         * Boolean->BOOLEAN
         */


        // String 类型  varchar
        DomainField domainField = new DomainField();
        domainField.setDomainId(UUID.randomUUID().toString());
        domainField.setFieldName("name");
        domainField.setTableFieldName("name");
        domainField.setDataType("String");
        domainField.setIsKey(false);
        domainFields.add(domainField);

        // Integer 类型
        DomainField domainField2 = new DomainField();
        domainField2.setDomainId(UUID.randomUUID().toString());
        domainField2.setFieldName("hight");
        domainField2.setTableFieldName("height");
        domainField2.setDataType("Integer");
        domainField2.setIsKey(false);
        domainFields.add(domainField2);

        // Double 类型
        DomainField domainField3 = new DomainField();
        domainField3.setDomainId(UUID.randomUUID().toString());
        domainField3.setFieldName("hight");
        domainField3.setTableFieldName("height");
        domainField3.setDataType("Double");
        domainField3.setIsKey(false);
        domainFields.add(domainField3);

        // Float 类型
        DomainField domainField4 = new DomainField();
        domainField4.setDomainId(UUID.randomUUID().toString());
        domainField4.setFieldName("name");
        domainField4.setTableFieldName("name");
        domainField4.setDataType("Float");
        domainField4.setIsKey(false);
        domainFields.add(domainField4);

        // Date 类型
        DomainField domainField5 = new DomainField();
        domainField5.setDomainId(UUID.randomUUID().toString());
        domainField5.setFieldName("name");
        domainField5.setTableFieldName("name");
        domainField5.setDataType("Date");
        domainField5.setIsKey(false);
        domainFields.add(domainField5);

        // BigDecimal 类型
        DomainField domainField6 = new DomainField();
        domainField6.setDomainId(UUID.randomUUID().toString());
        domainField6.setFieldName("name");
        domainField6.setTableFieldName("name");
        domainField6.setDataType("BigDecimal");
        domainField6.setIsKey(false);
        domainFields.add(domainField6);

        // BigDecimal 类型
        DomainField domainField7 = new DomainField();
        domainField7.setDomainId(UUID.randomUUID().toString());
        domainField7.setFieldName("name");
        domainField7.setTableFieldName("name");
        domainField7.setDataType("Boolean");
        domainField7.setIsKey(false);
        domainFields.add(domainField7);


        domain.setDomainFieldList(domainFields);

        String whole = generateXml(domain);
        System.out.println(whole);
    }

    private String generateXml(Domain domain) {
        List<DomainField> domainFields = domain.getDomainFieldList();
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                "<mapper namespace=\"com.ltmap.mapper.main." + domain.getDomainName() + "Mapper\">\n";
        String resultMapHeader = "<resultMap id=\"BaseResultMap\" type=\"com.ltmap.entity.main." + domain.getDomainName() + "\">";

        String resultMapFooter = "</resultMap>";

        StringBuffer propertyList = new StringBuffer("");

        domainFields.forEach(one -> {

            String fieldName = one.getFieldName();
            String tableFieldName = one.getTableFieldName();
            String componentType = one.getDataType();
            String temp = "<result property=\"" + fieldName + "\" column=\"" + tableFieldName + "\" jdbcType=\"" + this.getJdbcType(componentType) + "\"/>" + "\n";
            propertyList.append(temp);
        });

//        <id property="id" column="Id" jdbcType="VARCHAR"/>
//        <result property="code" column="code" jdbcType="VARCHAR"/>

        StringBuffer sqlBuffer = new StringBuffer("");
        domainFields.forEach(one -> {
            sqlBuffer.append(one.getTableFieldName() + ",");
        });
        String sql = sqlBuffer.toString();
        sql = sql.substring(0, sql.length() - 1);

        String sqlHeader = "<sql id=\"Base_Column_List\">\n";
        String sqlFooter = "\n</sql>\n";


        String footer = "\n</mapper>";


        String whole = header + resultMapHeader + propertyList + sqlHeader + sql + sqlFooter + resultMapFooter + footer;
        return whole;
    }

    private String getJdbcType(String componentType) {
        if (componentType.equals("String")) {
            return "VARCHAR";
        } else if (componentType.equals("Date")) {
            return "DATE";
        } else if (componentType.equals("Integer")) {
            return "INTEGER";
        } else if (componentType.equals("BigDecimal")) {
            return "DECIMAL";
        } else if (componentType.equals("Boolean")) {
            return "BOOLEAN";
        } else if (componentType.equals("Double")) {
            return "DOUBLE";
        } else if (componentType.equals("float")) {
            return "FLOAT";
        } else if (componentType.equals("Timestamp")) {
            return "TIMESTAMP";
        } else {
            return "VARCHAR";
        }
    }


    private String getDataTypeFromString(String dataType) {
        String returnDataType = "";
        switch (dataType) {
            case "varchar":
            case "text":
            case "longtext":
            case "char":
                returnDataType = "String";
                break;
            case "date":
                returnDataType = "Date";
                break;
            case "datetime":
                returnDataType = "Date";
                break;
            case "int":
            case "bigint":
            case "integer":
                returnDataType = "Integer";
                break;
            case "double":
                returnDataType = "Double";
                break;
            case "Long":
                returnDataType = "Long";
                break;
            case "float":
                returnDataType = "Float";
                break;
            case "byte":
                returnDataType = "byte";
                break;
            case "byte[]":
                returnDataType = "byte[]";
                break;
            default:
                return "String";
        }

        return returnDataType;
    }

    @Test
    void testSql() throws Exception {
        String sql = " CREATE TABLE `project_news` (\n" +
                "  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Id',\n" +
                "  `project_manage_id` bigint(20) NOT NULL COMMENT '关联科研专项',\n" +
                "  `news_source` varchar(200) DEFAULT NULL COMMENT '新闻来源',\n" +
                "  `news_title` varchar(200) DEFAULT NULL COMMENT '新闻标题',\n" +
                "  `news_type` varchar(200) DEFAULT NULL COMMENT '新闻类型',\n" +
                "  `publish_state` varchar(50) DEFAULT '0' COMMENT '发布状态',\n" +
                "  `rich_text_content` longtext COMMENT '富文本内容',\n" +
                "  `click_num` int(11) DEFAULT '0' COMMENT '点击数量',\n" +
                "  `update_id` bigint(20) DEFAULT NULL COMMENT '修改用户Id',\n" +
                "  `update_time` datetime DEFAULT NULL COMMENT '修改时间',\n" +
                "  `top_num` int(11) DEFAULT '9999' COMMENT '指定序号',\n" +
                "  `is_en` varchar(10) DEFAULT '0' COMMENT '是否英文版（0为非英文版，1为英文版）',\n" +
                "  `brief_content` varchar(2000) DEFAULT NULL COMMENT '简介内容',\n" +
                "  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',\n" +
                "  `second_type` varchar(100) DEFAULT NULL COMMENT '二级类别',\n" +
                "  PRIMARY KEY (`Id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='科研专项新闻表';";

        Domain entityDTO = new Domain();
        List<DomainField> entityFieldList = new ArrayList<>();
        String sqlLowerCase = sql.toLowerCase();
        sqlLowerCase = sqlLowerCase.replaceAll("`", "");
        sqlLowerCase = sqlLowerCase.replaceAll("default", "");
//        System.out.println(sqlLowerCase);

        // 匹配表名 create\s+table\s+[a-z_]+\s*[(]+\n
        Matcher matchTableName = Pattern.compile(
                "create\\s+table\\s+[a-z_]+\\s*[(]+")
                .matcher(sqlLowerCase);

        boolean findTable = matchTableName.find();
        if (findTable) {
            String tableGroup = matchTableName.group();
            String[] split = tableGroup.split("\\s");
            if (split.length > 1) {
//                System.out.println(split[0]+"  "+split[1]+"  "+split[2]+"  "+ split[3]);
                entityDTO.setTableName(split[2]);
                entityDTO.setDomainName(StrUtil.upperFirst(NamingCase.toCamelCase(split[2])));
            }
        } else {
            System.out.println("没有找到table名");
        }

        // 删除首行 create\s+table\s+[a-z_]+\s*[(]+\n
        Matcher matchFirstSql = Pattern.compile(
                "create\\s+table\\s+[a-z_]+\\s*[(]+\\n")
                .matcher(sqlLowerCase);
        if (matchFirstSql.find()) {
            String group = matchFirstSql.group();
            sqlLowerCase = sqlLowerCase.replace(group, "");
        }

//        System.out.println(sqlLowerCase);

        // 匹配属性字段
        String delimeter1 = "\\n";  // 指定分割字符， \ 号需要转义
        String[] rows = sqlLowerCase.split(delimeter1); // 分割字符串
        for (String x : rows) {
            // 跳过没有属性的行
            if (x.contains("primary") || x.contains("engine=")) {
                continue;
            }
            // 设置基础属性对象
            DomainField domainField = new DomainField();
            domainField.setId(UUID.randomUUID().toString());
            domainField.setIsKey(false);
            domainField.setLength(0);
            domainField.setDataType("String");
            domainField.setComponentType("String");
            domainField.setIsRequired(false);

            Matcher property = Pattern.compile("[a-z_]+").matcher(x);
            Matcher numberMatcher = Pattern.compile("[0-9]+").matcher(x);
            if (property.find()) {
                String rowProperty = property.group();
                domainField.setTableFieldName(rowProperty);
                domainField.setFieldName(StrUtil.upperFirst(NamingCase.toCamelCase(rowProperty)));
                if (property.find()) {
                    String dataType = property.group();
                    String dataTypeFromString = this.getDataTypeFromString(dataType);
                    domainField.setDataType(dataTypeFromString);
                    if (x.contains(dataType + "(")) {
                        if (numberMatcher.find()) {
                            String group = numberMatcher.group();
                            domainField.setLength(Integer.valueOf(group));
                        }
                    }
                }
            }
            if (x.contains(" id ")) {
                domainField.setIsKey(true);
            }
            if (x.contains(" not ")) {
                domainField.setIsRequired(true);
            }

            String[] split = x.split("comment");
            String comment = split[1].replace("'", "").replace(",", "").replace(" ", "");

            domainField.setFieldNameCN(comment);
            entityFieldList.add(domainField);

        }
    }

    @Test
    void testLocalMonthDate() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        Month month = today.getMonth();
        int monthValue = today.getMonthValue();
        int dayOfMonth = today.getDayOfMonth();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(year + " " + monthValue + " " + dayOfMonth); // 2022 6 12
        System.out.println(now.toLocalDate()); // 2022-06-12
        LocalTime localTime = now.toLocalTime();
        System.out.println(localTime);// 16:53:44.370789
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        System.out.println(dayOfWeek.getValue());// 7
    }

    @Test
    void contextLoads() {
//        String text = "cn.ideaswork";
//        System.out.println(text);
//        String[] split = text.split("\\.");
//        System.out.println(split[0]);
//        System.out.println(split[1]);
//        System.out.println(split.length);
        String text = "  public Page<T> listToPage(final List<User> list, Pageable pageable) {";
        String[] split = text.split("Page<T>");
        System.out.println(split.length);
        String whole = "  public <T> Page<T> " + split[1];
        System.out.println(whole);
    }

    @Test
    void testPath() {
        URL loginPngUrl = this.getClass().getResource("/static/img/login.png");
        String loginPngPath = loginPngUrl.getPath();

        System.out.println(loginPngPath);
    }

    @Test
    void testSianature() {
        SignatureQcloudTools sign = new SignatureQcloudTools();
        // 设置 App 的云 API 密钥
        sign.setSecretId("");
        sign.setSecretKey("个人 API 密钥中的 Secret Key");
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
    }

    @Test
    void testGetVideoInfo() throws TencentCloudSDKException {
        String fileid = "387702300934564360";
        MediaInfo vodInfo = vodService.getVodInfo(fileid);
        String mediaUrl = vodInfo.getBasicInfo().getMediaUrl();
        System.out.println(mediaUrl);
    }

}
