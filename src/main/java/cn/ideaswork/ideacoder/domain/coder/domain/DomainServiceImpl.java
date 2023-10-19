package cn.ideaswork.ideacoder.domain.coder.domain;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DomainServiceImpl implements DomainService {
    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Domain saveDomain(final Domain domain) {
        return domainRepository.save(domain);
    }

    @Override
    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    @Override
    public List<Domain> getAllDomains(String projectId) {
        return domainRepository.findByProjectId(projectId);
    }

    @Override
    public Domain getDomainById(final String id) {
        return domainRepository.findById(id).orElse(new Domain());
    }

    @Override
    public Domain updateDomainById(final Domain domain, final String id) {
        return domainRepository.save(domain);
    }

    @Override
    public void deleteDomainById(final String id) {
        domainRepository.deleteById(id);
    }

    @Override
    public Boolean isDomainExist(final String id) {
        return domainRepository.existsById(id);
    }

    @Override
    public Page<Domain> getDomainPageListByCondition(Domain domain, Pageable pageable) {
        Query query = new Query();
        if (StringUtils.isNotBlank(domain.getId())) {
            Criteria criteria = Criteria.where("id").is(domain.getId());
            query.addCriteria(criteria);
        }

        if (StringUtils.isNotBlank(domain.getDomainName())) {
            Criteria criteria = Criteria.where("domainName").regex(domain.getDomainName());
            query.addCriteria(criteria);
        }

        if (StringUtils.isNotBlank(domain.getProjectId())) {
            Criteria criteria = Criteria.where("projectId").is(domain.getProjectId());
            query.addCriteria(criteria);
        }
        List<Domain> domains = mongoTemplate.find(query, Domain.class);

        return SysTools.listToPage(domains, pageable);
    }

    @Override
    public List<Domain> getDomainListByProjectId(String projectId) {
        Query query = new Query();
        if (StringUtils.isNotBlank(projectId)) {
            Criteria criteria = Criteria.where("projectId").is(projectId);
            query.addCriteria(criteria);
        }
        List<Domain> domains = mongoTemplate.find(query, Domain.class);
        return domains;
    }

    @Override
    public Domain getDomainBySql(String domainSql) throws Exception {
        JSONObject paramsJSONObject = JSONObject.parseObject(domainSql);
        String sql = paramsJSONObject.getString("sql");
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("没有找到sql信息");
        }
        Domain domain = this.parseDDLSqlMethod(sql);
        return domain;
    }

    @Override
    public Domain getDomainBySqlMysql(String domainSql) throws Exception {
        JSONObject paramsJSONObject = JSONObject.parseObject(domainSql);
        String sql = paramsJSONObject.getString("sql");
        if (StringUtils.isEmpty(sql)) {
            throw new Exception("没有找到sql信息");
        }
        Domain domain = this.parseDDLSqlMysqlMethod(sql);
        return domain;
    }

    private Domain parseDDLSqlMysqlMethod(String sql) {
        Domain entityDTO = new Domain();
        List<DomainField> entityFieldList = new ArrayList<>();
        String sqlLowerCase = sql;
        sqlLowerCase = sqlLowerCase.replaceAll("`", "");
        sqlLowerCase = sqlLowerCase.replaceAll("default ", " ");
        sqlLowerCase = sqlLowerCase.replaceAll("DEFAULT ", " ");
        sqlLowerCase = sqlLowerCase.replaceAll("CREATE ", "create ");
        sqlLowerCase = sqlLowerCase.replaceAll(" TABLE ", " table ");
        sqlLowerCase = sqlLowerCase.replaceAll(" COMMENT ", " comment ");
        sqlLowerCase = sqlLowerCase.replaceAll(" NULL ", " null ");
        sqlLowerCase = sqlLowerCase.replaceAll(" NOT ", " not ");
//        System.out.println(sqlLowerCase);

        // 匹配表名 create\s+table\s+[a-z_]+\s*[(]+\n
        Matcher matchTableName = Pattern.compile(
                "create\\s+table\\s+[a-z_]+\\s*[(]+\\n")
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
            if (x.contains("primary") || x.contains("PRIMARY") || x.contains("engine=")|| x.contains("ENGINE")) {
                continue;
            }
            // 设置基础属性对象
            DomainField domainField = new DomainField();
            domainField.setId(UUID.randomUUID().toString());
            domainField.setIsKey(false);
            domainField.setLength(0);
            domainField.setDataType("String");
            domainField.setComponentType("Input");
            domainField.setIsRequired(false);
            Matcher property = null;
            if (x.contains("_")){
                property = Pattern.compile("[a-z_]+").matcher(x);
            }else{
                property = Pattern.compile("[a-zA-Z]+").matcher(x);
            }

            Matcher numberMatcher = Pattern.compile("[0-9]+").matcher(x);
            if (property.find()) {
                String rowProperty = property.group();
                domainField.setTableFieldName(rowProperty);
                if (rowProperty.contains("_")) {
                    domainField.setFieldName(NamingCase.toCamelCase(rowProperty));
                } else {
                    domainField.setFieldName(rowProperty);
                }

                if (property.find()) {
                    String dataType = property.group();
                    String dataTypeFromString = this.getDataTypeFromString(dataType);
                    String componetType = this.getComponetType(dataType);
                    domainField.setDataType(dataTypeFromString);
                    domainField.setComponentType(componetType);
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
            String[] split = x.split(" comment ");

            String comment = split[1].replace("'", "").replace(",", "").replace(" ", "");

            domainField.setFieldNameCN(comment);
            entityFieldList.add(domainField);

        }
        entityDTO.setDomainFieldList(entityFieldList);
        return entityDTO;
    }

    private String getComponetType(String dataType) {
        String returnComponent = "";
        switch (dataType) {
            case "varchar":
            case "text":
            case "longtext":
            case "char":
                returnComponent = "Input";
                break;
            case "date":
                returnComponent = "DatePicker";
                break;
            case "datetime":
                returnComponent = "DatePicker";
                break;
            case "int":
            case "bigint":
            case "integer":
            case "double":
            case "Long":
            case "float":
                returnComponent = "InputNumber";
                break;
            case "byte":
                returnComponent = "byte";
                break;
            case "byte[]":
                returnComponent = "byte[]";
                break;
            default:
                returnComponent = "Input";
        }
        return returnComponent;
    }

    private Domain parseDDLSqlMethod(String sql) {
        Domain entityDTO = new Domain();

        String output = sql;
        String sqlLowerCase = output.toLowerCase();

//        create\s+table\s*\w+\s*\W
        Matcher matchTableName = Pattern.compile(
                "table\\s*\\w+\\s*\\W")
                .matcher(sqlLowerCase);

        boolean findTable = matchTableName.find();
        if (findTable) {
            String tableGroup = matchTableName.group();
            String[] split = tableGroup.split("\\s");
            if (split.length > 1) {
                entityDTO.setTableName(split[1].toUpperCase());
                entityDTO.setDomainName(split[1].substring(0, 1).toUpperCase() + split[1].substring(1));
            }
        } else {
            System.out.println("没有找到table名");
        }
        // 删除第一行
        Matcher matchFirstSql = Pattern.compile(
                "create\\s+table\\s*\\w+\\s*\\W\\s*\\\\n")
                .matcher(sqlLowerCase);
        if (matchFirstSql.find()) {
            String group = matchFirstSql.group();
            System.out.println(group);
            sqlLowerCase = sqlLowerCase.replace(group, "");
            System.out.println("新的SQL-" + sqlLowerCase);
        }

        System.out.println(sqlLowerCase);

        // 匹配属性字段
        Matcher matchFieldRow = Pattern.compile(".*null.*\\/").matcher(sqlLowerCase);// 匹配所有的属性字段

        System.out.println("去掉首行的 sql = " + sqlLowerCase);// 去掉首行的 sql

        String delimeter1 = "\\n";  // 指定分割字符， \ 号需要转义
        String[] rows = sqlLowerCase.split(delimeter1); // 分割字符串
        for (String x : rows) {
            System.out.println(x);
            System.out.println("");
        }

        List<DomainField> entityFieldList = new ArrayList<>();


        // 按行匹配 TODO  方法二  按照 \n 换行符号进行分割字符串
        while (matchFieldRow.find()) {
            String row = matchFieldRow.group();// 匹配的一行（包含 comment）
            System.out.println("当前行的内容是：" + row);
            Matcher matchFieldComment = Pattern.compile("\\/\\*.*\\*\\/").matcher(row);// 匹配行的内容
            String comment = "";
            if (matchFieldComment.find()) {
                comment = matchFieldComment.group();// 匹配的一行 comment
                comment = comment.replaceAll("\\/\\*", "").replaceAll("\\*\\/", "");
            }
            DomainField field = new DomainField();
            field.setFieldNameCN(comment);

            System.out.println("当前行的注释：" + comment);
            String noCommentRow = row.replace(comment, "");// ID varchar(36) NOT NULL,
            System.out.println("没有注释的行：" + noCommentRow);

            //  \w+  匹配单词
            Matcher fieldWord = Pattern.compile("\\w+").matcher(noCommentRow);// 匹配改行的内容
            int matchNum = 0;
            while (fieldWord.find()) {
                if (matchNum == 0) {// 字段
                    field.setFieldName(fieldWord.group());
                    field.setTableFieldName(fieldWord.group());
                }
                if (matchNum == 1) {// 字段类型
                    String dateType = fieldWord.group();
//                    field.setDataType(getDataType(dateType));
                    field.setDataType(this.getDataTypeFromString(dateType));
                }
                if (matchNum == 2) {// 可能是数字
                    Matcher numMatch = Pattern.compile("[0-9]+").matcher(row);// 匹配数字
                    if (numMatch.find()) {
                        field.setLength(Integer.parseInt(numMatch.group()));
                    } else {
                        field.setLength(0);
                    }
                }
                matchNum++;
            }

            if (noCommentRow.contains("not")) {
                field.setIsRequired(true);
            } else {
                field.setIsRequired(false);
            }
            field.setId(UUID.randomUUID().toString());
            field.setIsKey(false);
            field.setRulesType("String");
            field.setRuleMessage("请输入内容");
            entityFieldList.add(field);
        }

        entityDTO.setDomainFieldList(entityFieldList);

        // 先匹配  .*null.*\/  获取带注释的一行，再匹配 .*null,  获取一行   两者有包含关系则只建立一个  没有则建立两个

        //    ID varchar(36) NOT NULL,

        // 针对每一行进行分解，含有 not 则为非空
        // 含有按照空格进行分组，0 号字段为 数据库id值  转小写为 bean 属性
        // 数据类型提取，需要一个搜索方法，搜索各种常见数据字段类型，匹配相应的 java bean 数据类型
        // 数据长度直接使用 正则提取连续数字
        return entityDTO;
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

    public Domain mapToEntity(final Domain domainDTO, final Domain domain) {
        BeanUtils.copyProperties(domainDTO, domain);
        return domain;
    }

    public Domain mapToDTO(final Domain domain, final Domain domainDTO) {
        BeanUtils.copyProperties(domain, domainDTO);
        return domainDTO;
    }
}
