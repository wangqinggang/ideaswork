package cn.ideaswork.ideacoder.application.vuecode;

import cn.hutool.core.io.file.FileWriter;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainField;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VueElementCodeServiceImpl implements VueElementCodeService {
    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    ProjectService projectService;

    @Autowired
    DomainService domainService;

    @Override
    public String generateAddForm(Domain domain) {

        List<DomainField> entityFields = domain.getDomainFieldList();
        // 拼接 start 部分
        String start = templateEngine.process("vueElementPlusComponent/addStart", new Context());

        Map<String, String> mapData = new HashMap<>();
        String entityName = domain.getDomainName();
        String entityRules = entityName + "Rules";
        mapData.put("entityRules", entityRules);
        mapData.put("entityName", entityName);
        String addStart = this.generateFromText(mapData, start);
        // 属性部分
        String middle = "";
        for (int i = 0; i < entityFields.size(); i++) {
            DomainField entityField = entityFields.get(i);
            String fieldName = entityField.getFieldName();
            String lable = entityField.getFieldNameCN();
            String placeholder = "请填写" + lable;
            // 利用 Thymeleaf 模板构建 文本
            Context ctx = new Context();
            ctx.setVariable("lable", lable);
            ctx.setVariable("fieldName", entityName + "." + fieldName);
            ctx.setVariable("propName", fieldName);
            ctx.setVariable("placeholder", placeholder);
            ctx.setVariable("dictionary", "dictionary");

            // 此处替换为模板
            String content = getPlusOneField(entityField, ctx,"add");
            middle += content + "\n";
        }
        String end = templateEngine.process("vueElementPlusComponent/addEnd", new Context());
        String addEnd = this.generateFromText(mapData, end);
        String all = addStart + middle + addEnd;
        return all;
    }

    @Override
    public String generateFromText(Map<String, String> dataMap, String filePath) {
        Set set = dataMap.entrySet();
        Iterator iterator = set.iterator();

        String wholeText = filePath;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();
            String keyformate = "\\$\\{" + key + "\\}";
//            "\\$\\{[^}]+\\}";
            Object value = entry.getValue();
            Pattern pattern = Pattern.compile(keyformate);
            Matcher matcher = pattern.matcher(wholeText);
            wholeText = matcher.replaceAll(value.toString());
//            System.out.println(wholeText);
        }

        return wholeText;
    }

    @Override
    public String generateServiceJs(Domain domain) {
        String entityNameUrl = domain.getDomainName().toLowerCase() + "s";

        // 拼接 JS 方法
        String jsTemplate = templateEngine.process("vueElementPlusJs/ideaBossServiceJs", new Context());

        Map<String, String> mapData = new HashMap<>();
        mapData.put("entityNameUrl", entityNameUrl);
        mapData.put("entityComment", domain.getDomainNameCN());
        mapData.put("entityName", domain.getDomainName());

        String s = this.generateFromText(mapData, jsTemplate);
        return s;
    }

    @Override
    public String generateDomainJs(Domain domain) {
        String entityName = domain.getDomainName();
        String conditionListEnd = templateEngine.process("vueElementPlusComponent/end", new Context());
        Map<String, String> mapData = new HashMap<>();
        mapData.put("entityName", entityName);
        mapData.put("entityNameLowercase", entityName.toLowerCase());
        conditionListEnd = this.generateFromText(mapData, conditionListEnd);
        return conditionListEnd;
    }

    @Override
    public String generatePageList(Domain domain) {
        List<DomainField> entityFields = domain.getDomainFieldList();
        // 开头部分
        String start = "";
        String entityName = domain.getDomainName();
        start = templateEngine.process("vueElementPlusComponent/pageListStart", new Context());

        Map<String, String> startMapData = new HashMap<>();
        startMapData.put("entityName", entityName);
        String conditionPagingListStart = this.generateFromText(startMapData, start);

        // 条件查询表单属性部分
        String conditionForm = "";
        for (int i = 0; i < entityFields.size(); i++) {
            DomainField entityField = entityFields.get(i);
            String fieldName = entityField.getFieldName();
            String lable = entityField.getFieldNameCN();
            String placeholder = "请填写" + lable;
            // 利用 Thymeleaf 模板构建 文本
            Context ctx = new Context();
            ctx.setVariable("lable", lable);
            ctx.setVariable("fieldName", entityName + "Condition." + fieldName);
            ctx.setVariable("propName", fieldName);
            ctx.setVariable("placeholder", placeholder);
            ctx.setVariable("dictionary", "dictionary");

            // 此处替换为模板
            String content = getPlusOneField(entityField, ctx,"condition");
            conditionForm += content + "\n";
        }

        // 条件查询搜索按钮与新增按钮和删除按钮
        String pageListHeaderEnd = templateEngine.process("vueElementPlusComponent/pageListHeaderEnd", new Context());
        Map<String, String> mapData1 = new HashMap<>();
        mapData1.put("entityName", entityName);
        pageListHeaderEnd = this.generateFromText(mapData1, pageListHeaderEnd);

        // table 属性部分
        String pageListTableItem = "";
        for (int i = 0; i < entityFields.size(); i++) {
            DomainField entityField = entityFields.get(i);
            String fieldName = entityField.getFieldName();
            String lable = entityField.getFieldNameCN();

            // 此处替换为模板
            String content = templateEngine.process("vueElementPlusComponent/pageListTableItem", new Context());
            Map<String, String> mapData = new HashMap<>();
            mapData.put("label", lable);
            mapData.put("fieldName", fieldName);
            String item = this.generateFromText(mapData, content);
            pageListTableItem += item + "\n";
        }
        // 结尾部分
        String conditionListEnd = templateEngine.process("vueElementPlusComponent/pageListEnd", new Context());
        Map<String, String> mapData = new HashMap<>();
        mapData.put("entityName", entityName);
        conditionListEnd = this.generateFromText(mapData, conditionListEnd);
        String all = conditionPagingListStart + conditionForm + pageListHeaderEnd + pageListTableItem + conditionListEnd;
        return all;
    }

    @Override
    public String generateInfo(Domain domain) {
        List<DomainField> entityFields = domain.getDomainFieldList();
        // 开头部分
        String start = "";
        String entityName = domain.getDomainName();
        String comment = domain.getDomainNameCN();
        Context startctx = new Context();
        start = templateEngine.process("vueElementPlusComponent/infoStart", startctx);
        Map<String, String> mapData1 = new HashMap<>();
        mapData1.put("comment", comment);
        mapData1.put("entityName", entityName);
        start = this.generateFromText(mapData1, start);
        // 属性部分
        Context ctx = new Context();
        String middle = "";
        for (int i = 0; i < entityFields.size(); i++) {
            DomainField entityField = entityFields.get(i);
            String fieldName = entityField.getFieldName();
            String lable = entityField.getFieldNameCN();

            // 此处替换为模板
            String content = templateEngine.process("vueElementPlusComponent/infoItem", ctx);
            Map<String, String> mapData = new HashMap<>();
            mapData.put("label", lable);
            mapData.put("fieldName", entityName + "." + fieldName);
            String item = this.generateFromText(mapData, content);
            middle += item + "\n";
        }
        // 结尾部分
        String end = templateEngine.process("vueElementPlusComponent/infoEnd", ctx);
        Map<String, String> mapData = new HashMap<>();
        mapData.put("entityName", entityName);
        end = this.generateFromText(mapData, end);
        String all = start + middle + end;
        return all;
    }

    @Override
    public String generateDomainVue(Domain domainById) {
        String vuePageTable = this.generatePageList(domainById);
        String vueAdd = this.generateAddForm(domainById);
        String vueInfo = this.generateInfo(domainById);
        String vueJs = this.generateDomainJs(domainById);
        return vuePageTable + vueAdd + vueInfo + vueJs;
    }

    /**
     *
     * @param domainField
     * @param ctx
     * @param type 枚举 condition、add 分别对应条件查询表单、新增与修改表单
     * @return
     */
    private String getPlusOneField(DomainField domainField, Context ctx,String type) {
        String content = "";
        switch (domainField.getComponentType()) {
            case "Input":
                if ("condition".equals(type)) {
                    content = templateEngine.process("vueElementPlusComponent/input", ctx);
                }else{
                    if(domainField.getIsKey()){
                        content = templateEngine.process("vueElementPlusComponent/disableInput", ctx);
                    }else{
                        content = templateEngine.process("vueElementPlusComponent/input", ctx);
                    }
                }
                break;
            case "TextArea":
                content = templateEngine.process("vueElementPlusComponent/textarea", ctx);
                break;
            case "InputNumber":
                content = templateEngine.process("vueElementPlusComponent/number", ctx);
                break;
            case "DatePicker":
                // 目前有两个 condition add 的表单
                if ("condition".equals(type)){
                    content = templateEngine.process("vueElementPlusComponent/conditionDate", ctx);
                }else{
                    content = templateEngine.process("vueElementPlusComponent/date", ctx);
                }
                break;
            case "TimePicker":
                content = templateEngine.process("vueElementPlusComponent/time", ctx);
                break;
            case "Radio":
                content = templateEngine.process("vueElementPlusComponent/input", ctx);
                break;
            case "Select":
                content = templateEngine.process("vueElementPlusComponent/select", ctx);
                break;
            case "Checkbox":
                content = templateEngine.process("vueElementPlusComponent/checkbox", ctx);
                break;
            case "Rate":
                content = templateEngine.process("vueElementPlusComponent/rate", ctx);
                break;
            case "Slider":
                content = templateEngine.process("vueElementPlusComponent/slider", ctx);
                break;
            case "Switch":
                content = templateEngine.process("vueElementPlusComponent/switch", ctx);
                break;
            default:
                content = templateEngine.process("vueElementPlusComponent/input", ctx);
                break;
        }
        return content;
    }

}
