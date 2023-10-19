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
public class VueElementPlusCodeServiceImpl implements VueElementPlusCodeService {
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
    public void generateVueProject(String projectId, String basePath) throws FileNotFoundException {
        // basePath =  sysPath + projectId + "/"
        Project projectById = projectService.getProjectById(projectId);
        String projectName = projectById.getEnname() + "-front";
        String frontEndPort = projectById.getFrontEndPort();


//        String[] split = projectById.getPackageGroup().split("\\.");
//        String uppercaseProjectName = "";
//        String firstLtr = projectName.substring(0, 1);
//        String restLtrs = projectName.substring(1, projectName.length());
//        uppercaseProjectName = firstLtr.toUpperCase() + restLtrs;

        List<Domain> allDomains = domainService.getAllDomains(projectId);

        // xxx/projectId/projectName/src/main/java/packageLocationName/domain/xxx.java
//        ideaboss-front
//        ideaboss-front/public/
//        ideaboss-front/src/
//                ideaboss-front/.gitignore
//        ideaboss-front/index.html
//        ideaboss-front/package-lock.json
//        ideaboss-front/package.json
//        ideaboss-front/postcss.config.js
//        ideaboss-front/README.md
//        ideaboss-front/tailwind.config.js
//        ideaboss-front/vite.config.js

        // 生成 xxxProject/index.html
        String indexBasePath = basePath + projectName.toLowerCase() + "/";
        String indexContent = this.generateIndex(projectName);
        String indexFileName = "index.html";
        String indexLocation = indexBasePath + indexFileName;
        FileWriter indexWriter = new FileWriter(indexLocation);
        indexWriter.write(indexContent);


        // 生成 xxxProject/package.json
        String packageJsonBasePath = basePath + projectName.toLowerCase() + "/";
        String packageJsonContent = this.generatePackageJson(projectId);
        String packageJsonFileName = "package.json";
        String packageJsonLocation = packageJsonBasePath + packageJsonFileName;
        FileWriter packageJsonWriter = new FileWriter(packageJsonLocation);
        packageJsonWriter.write(packageJsonContent);

        // 生成 xxxProject/Readme.md
        String readMeBasePath = basePath + projectName.toLowerCase() + "/";
        String readMeContent = this.generateReadMe();
        String readMeFileName = "README.md";
        String readMeLocation = readMeBasePath + readMeFileName;
        FileWriter readMeWriter = new FileWriter(readMeLocation);
        readMeWriter.write(readMeContent);

        // 生成 xxxProject/vite.config.js
        String viteConfigJsBasePath = basePath + projectName.toLowerCase() + "/";
        String viteConfigJsContent = this.generateViteConfigJs(frontEndPort);
        String viteConfigJsFileName = "vite.config.js";
        String viteConfigJsLocation = viteConfigJsBasePath + viteConfigJsFileName;
        FileWriter viteConfigJsWriter = new FileWriter(viteConfigJsLocation);
        viteConfigJsWriter.write(viteConfigJsContent);

        // 生成 xxxProject/tailwind.config.js
        String tailwindConfigJsBasePath = basePath + projectName.toLowerCase() + "/";
        String tailwindConfigJsContent = this.generateTailwindConfigJs();
        String tailwindConfigJsFileName = "tailwind.config.js";
        String tailwindConfigJsLocation = tailwindConfigJsBasePath + tailwindConfigJsFileName;
        FileWriter tailwindConfigJsWriter = new FileWriter(tailwindConfigJsLocation);
        tailwindConfigJsWriter.write(tailwindConfigJsContent);

        // 生成 xxxProject/postcss.config.js
        String postcssConfigJsBasePath = basePath + projectName.toLowerCase() + "/";
        String postcssConfigJsContent = this.generatePostcssConfigJs();
        String postcssConfigJsFileName = "postcss.config.js";
        String postcssConfigJsLocation = postcssConfigJsBasePath + postcssConfigJsFileName;
        FileWriter postcssConfigJsWriter = new FileWriter(postcssConfigJsLocation);
        postcssConfigJsWriter.write(postcssConfigJsContent);


        // 生成 xxxProject/src/main.js
        String mainJsBasePath = basePath + projectName.toLowerCase() + "/src/";
        String mainJsContent = this.generateMainJs();
        String mainJsFileName = "main.js";
        String mainJsLocation = mainJsBasePath + mainJsFileName;
        FileWriter mainJsWriter = new FileWriter(mainJsLocation);
        mainJsWriter.write(mainJsContent);

        // 生成 xxxProject/src/index.css
        String indexCssBasePath = basePath + projectName.toLowerCase() + "/src/";
        String indexCssContent = this.generateIndexCss();
        String indexCssFileName = "index.css";
        String indexCssLocation = indexCssBasePath + indexCssFileName;
        FileWriter indexCssWriter = new FileWriter(indexCssLocation);
        indexCssWriter.write(indexCssContent);

        // 生成 xxxProject/src/App.vue
        String appVueBasePath = basePath + projectName.toLowerCase() + "/src/";
        String appVueContent = this.generateAppVue();
        String appVueFileName = "App.vue";
        String appVueLocation = appVueBasePath + appVueFileName;
        FileWriter appVueWriter = new FileWriter(appVueLocation);
        appVueWriter.write(appVueContent);

        // 生成 xxxProject/src/utils/request.js
        String requestJsBasePath = basePath + projectName.toLowerCase() + "/src/utils/";
        String requestJsContent = this.generateRequestJs(projectById.getBackEndPort());
        String requestJsFileName = "request.js";
        String requestJsLocation = requestJsBasePath + requestJsFileName;
        FileWriter requestJsWriter = new FileWriter(requestJsLocation);
        requestJsWriter.write(requestJsContent);

        // 生成 xxxProject/src/store/store.js
        String storeJsBasePath = basePath + projectName.toLowerCase() + "/src/store/";
        String storeJsContent = this.generateStoreJs();
        String storeJsFileName = "store.js";
        String storeJsLocation = storeJsBasePath + storeJsFileName;
        FileWriter storeJsWriter = new FileWriter(storeJsLocation);
        storeJsWriter.write(storeJsContent);


        // 生成 xxxProject/src/router.js
        String routerJsBasePath = basePath + projectName.toLowerCase() + "/src/";
        String routerJsContent = this.generateRouterJs(allDomains);
        String routerJsFileName = "router.js";
        String routerJsLocation = routerJsBasePath + routerJsFileName;
        FileWriter routerJsWriter = new FileWriter(routerJsLocation);
        routerJsWriter.write(routerJsContent);


        // 生成 xxxProject/src/components/layout/Aside.vue
        String asideVueBasePath = basePath + projectName.toLowerCase() + "/src/components/layout/";
        String asideVueContent = this.generateAsideVue(projectById);
        String asideVueFileName = "Aside.vue";
        String asideVueLocation = asideVueBasePath + asideVueFileName;
        FileWriter asideVueWriter = new FileWriter(asideVueLocation);
        asideVueWriter.write(asideVueContent);

        // 生成 xxxProject/src/components/layout/Footer.vue
        String footerVueBasePath = basePath + projectName.toLowerCase() + "/src/components/layout/";
        String footerVueContent = this.generateFooterVue();
        String footerVueFileName = "Footer.vue";
        String footerVueLocation = footerVueBasePath + footerVueFileName;
        FileWriter footerVueWriter = new FileWriter(footerVueLocation);
        footerVueWriter.write(footerVueContent);

        // 生成 xxxProject/src/components/layout/Header.vue
        String headerVueBasePath = basePath + projectName.toLowerCase() + "/src/components/layout/";
        String headerVueContent = this.generateHeaderVue(projectName);
        String headerVueFileName = "Header.vue";
        String headerVueLocation = headerVueBasePath + headerVueFileName;
        FileWriter headerVueWriter = new FileWriter(headerVueLocation);
        headerVueWriter.write(headerVueContent);

        // 生成 xxxProject/src/components/layout/Main.vue
        String mainVueBasePath = basePath + projectName.toLowerCase() + "/src/components/layout/";
        String mainVueContent = this.generateMainVue();
        String mainVueFileName = "Main.vue";
        String mainVueLocation = mainVueBasePath + mainVueFileName;
        FileWriter mainVueWriter = new FileWriter(mainVueLocation);
        mainVueWriter.write(mainVueContent);

        // 生成 xxxProject/src/views/Login.vue
        String loginVueBasePath = basePath + projectName.toLowerCase() + "/src/views/";
        String loginVueContent = this.generateLoginVue();
        String loginVueFileName = "Login.vue";
        String loginVueLocation = loginVueBasePath + loginVueFileName;
        FileWriter loginVueWriter = new FileWriter(loginVueLocation);
        loginVueWriter.write(loginVueContent);

        // 生成 xxxProject/src/assets/images/login.png
        String loginPngBasePath = basePath + projectName.toLowerCase() + "/src/assets/images/";
        String loginPngFileName = "login.png";
        String loginPngLocation = loginPngBasePath + loginPngFileName;
        URL loginPngUrl = this.getClass().getResource("/static/img/login.png");
        String loginPngPath = loginPngUrl.getPath();
        InputStream loginPngInputStream = new FileInputStream(loginPngPath);
        FileWriter loginPngWriter = new FileWriter(loginPngLocation);
        loginPngWriter.writeFromStream(loginPngInputStream);

        // 生成 xxxProject/src/assets/images/avatar.png
        String avatarPngBasePath = basePath + projectName.toLowerCase() + "/src/assets/images/";
        String avatarPngFileName = "avatar.jpg";
        String avatarPngLocation = avatarPngBasePath + avatarPngFileName;
        URL avatarPngUrl = this.getClass().getResource("/static/img/avatar.jpg");
        String avatarPngPath = avatarPngUrl.getPath();
        InputStream avatarPngInputStream = new FileInputStream(avatarPngPath);
        FileWriter avatarPngWriter = new FileWriter(avatarPngLocation);
        avatarPngWriter.writeFromStream(avatarPngInputStream);


        // 生成 xxxProject/src/views/404.vue
        String _404VueBasePath = basePath + projectName.toLowerCase() + "/src/views/";
        String _404VueContent = this.generate404Vue();
        String _404VueFileName = "404.vue";
        String _404VueLocation = _404VueBasePath + _404VueFileName;
        FileWriter _404VueWriter = new FileWriter(_404VueLocation);
        _404VueWriter.write(_404VueContent);

        // ideaboss-front/src/views/users/User.vue
        // ideaboss-front/src/views/users/UserService.js
        for (Domain domain : allDomains) {
            String domainName = domain.getDomainName();
            // generate Vue
            String domainVueBasePath = basePath + projectName.toLowerCase() + "/src/views/" + domainName + "/";
            String domainVueContent = this.generateDomainVue(domain);
            String domainVueFileName = domainName.toLowerCase() + ".vue";
            String domainVueLocation = domainVueBasePath + domainVueFileName;
            FileWriter domainVueWriter = new FileWriter(domainVueLocation);
            domainVueWriter.write(domainVueContent);

            // generate JS
            String domainJsBasePath = basePath + projectName.toLowerCase() + "/src/views/" + domainName + "/";
            String domainJsContent = this.generateServiceJs(domain);
            String domainJsFileName = domainName.toLowerCase() + "Service.js";
            String domainJsLocation = domainJsBasePath + domainJsFileName;
            FileWriter domainJsWriter = new FileWriter(domainJsLocation);
            domainJsWriter.write(domainJsContent);

        }

    }


    private String generateRouterJs(List<Domain> allDomains) {
        String childrens = "";
        String imports = "";
        for (Domain domain : allDomains) {
            String domainName = domain.getDomainName();
            String firstDomain = domainName.substring(0, 1);
            String lastDomain = domainName.substring(1, domainName.length());
            String domainFirstUpperdcase = firstDomain + lastDomain;
            String oneChild = "      {\n" +
                    "        path: \"" + domainName.toLowerCase() + "\",\n" +
                    "        name: \"" + domainFirstUpperdcase + "\",\n" +
                    "        component: " + domainFirstUpperdcase + ",\n" +
                    "      },\n";
            childrens += oneChild;
            String oneImport = "import " + domainFirstUpperdcase + " from \"@/views/"
                    + domainName.toLowerCase() + "/" + domainFirstUpperdcase + ".vue\";\n";
            imports += oneImport;

        }


        String routerContent = "import { createWebHistory, createRouter } from \"vue-router\";\n" +
                "\n" +
                "import Main from \"@/components/layout/Main.vue\";\n" +
                "import NotFound from \"@/views/404.vue\";\n" +
                "import Login from \"@/views/Login.vue\";\n" +
                imports +
                "/**\n" +
                " *  Path - the URL path where this route can be found.\n" +
                " *  Name - An optional name to use when we link to this route.\n" +
                " *  Component - Which component to load when this route is called.\n" +
                " */\n" +
                "\n" +
                "const routes = [\n" +
                "  {\n" +
                "    path: \"/\",\n" +
                "    name: \"Home\",\n" +
                "    component: Main,\n" +
                "    children: [\n" +
                childrens +
                "    ],\n" +
                "  },\n" +
                "  {\n" +
                "    path: \"/login\",\n" +
                "    name: \"Login\",\n" +
                "    component: Login,\n" +
                "  },\n" +
                "  {\n" +
                "    path: \"/:catchAll(.*)\",\n" +
                "    component: NotFound,\n" +
                "  },\n" +
                "];\n" +
                "\n" +
                "const router = createRouter({\n" +
                "  // history: createWebHistory(\"/ideabossfront/dist/\"),// 当访问路径为 ip:port/ideabossfornt/dist/ 时配置\n" +
                "  history: createWebHistory(),\n" +
                "  routes,\n" +
                "});\n" +
                "\n" +
                "export default router;\n";
        return routerContent;
    }

    @Override
    public String generateDomainVue(Domain domainById) {
        String vuePageTable = this.generatePageList(domainById);
        String vueAdd = this.generateAddForm(domainById);
        String vueInfo = this.generateInfo(domainById);
        String vueJs = this.generateDomainJs(domainById);
        return vuePageTable + vueAdd + vueInfo + vueJs;
    }

    private String generate404Vue() {
        String _404vue = "<template>\n" +
                "  <div class=\"wscn-http404-container\">\n" +
                "    <div class=\"wscn-http404\">\n" +
                "      \n" +
                "      <div class=\"bullshit\">\n" +
                "        <div class=\"bullshit__oops\">404错误!</div>\n" +
                "        <div class=\"bullshit__headline\">\n" +
                "          {{ message }}\n" +
                "        </div>\n" +
                "        <div class=\"bullshit__info\">\n" +
                "          对不起，您正在寻找的页面不存在。尝试检查URL的错误，然后按浏览器上的刷新按钮或尝试在我们的应用程序中找到其他内容。\n" +
                "        </div>\n" +
                "        <router-link to=\"/\" class=\"bullshit__return-home\">\n" +
                "          返回首页\n" +
                "        </router-link>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "export default {\n" +
                "  name: \"notFoud\",\n" +
                "  data() {\n" +
                "    return {\n" +
                "      message: \"没东西了\",\n" +
                "      loading: false,\n" +
                "      rules: {},\n" +
                "    };\n" +
                "  },\n" +
                "  methods: {},\n" +
                "};\n" +
                "</script>\n" +
                "\n" +
                "<style lang=\"scss\" scoped>\n" +
                ".wscn-http404-container {\n" +
                "  transform: translate(-50%, -50%);\n" +
                "  position: absolute;\n" +
                "  top: 40%;\n" +
                "  left: 50%;\n" +
                "}\n" +
                ".wscn-http404 {\n" +
                "  position: relative;\n" +
                "  width: 1200px;\n" +
                "  padding: 0 50px;\n" +
                "  overflow: hidden;\n" +
                "  .pic-404 {\n" +
                "    position: relative;\n" +
                "    float: left;\n" +
                "    width: 600px;\n" +
                "    overflow: hidden;\n" +
                "    &__parent {\n" +
                "      width: 100%;\n" +
                "    }\n" +
                "    &__child {\n" +
                "      position: absolute;\n" +
                "      &.left {\n" +
                "        width: 80px;\n" +
                "        top: 17px;\n" +
                "        left: 220px;\n" +
                "        opacity: 0;\n" +
                "        animation-name: cloudLeft;\n" +
                "        animation-duration: 2s;\n" +
                "        animation-timing-function: linear;\n" +
                "        animation-fill-mode: forwards;\n" +
                "        animation-delay: 1s;\n" +
                "      }\n" +
                "      &.mid {\n" +
                "        width: 46px;\n" +
                "        top: 10px;\n" +
                "        left: 420px;\n" +
                "        opacity: 0;\n" +
                "        animation-name: cloudMid;\n" +
                "        animation-duration: 2s;\n" +
                "        animation-timing-function: linear;\n" +
                "        animation-fill-mode: forwards;\n" +
                "        animation-delay: 1.2s;\n" +
                "      }\n" +
                "      &.right {\n" +
                "        width: 62px;\n" +
                "        top: 100px;\n" +
                "        left: 500px;\n" +
                "        opacity: 0;\n" +
                "        animation-name: cloudRight;\n" +
                "        animation-duration: 2s;\n" +
                "        animation-timing-function: linear;\n" +
                "        animation-fill-mode: forwards;\n" +
                "        animation-delay: 1s;\n" +
                "      }\n" +
                "      @keyframes cloudLeft {\n" +
                "        0% {\n" +
                "          top: 17px;\n" +
                "          left: 220px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "        20% {\n" +
                "          top: 33px;\n" +
                "          left: 188px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        80% {\n" +
                "          top: 81px;\n" +
                "          left: 92px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        100% {\n" +
                "          top: 97px;\n" +
                "          left: 60px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "      }\n" +
                "      @keyframes cloudMid {\n" +
                "        0% {\n" +
                "          top: 10px;\n" +
                "          left: 420px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "        20% {\n" +
                "          top: 40px;\n" +
                "          left: 360px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        70% {\n" +
                "          top: 130px;\n" +
                "          left: 180px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        100% {\n" +
                "          top: 160px;\n" +
                "          left: 120px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "      }\n" +
                "      @keyframes cloudRight {\n" +
                "        0% {\n" +
                "          top: 100px;\n" +
                "          left: 500px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "        20% {\n" +
                "          top: 120px;\n" +
                "          left: 460px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        80% {\n" +
                "          top: 180px;\n" +
                "          left: 340px;\n" +
                "          opacity: 1;\n" +
                "        }\n" +
                "        100% {\n" +
                "          top: 200px;\n" +
                "          left: 300px;\n" +
                "          opacity: 0;\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  .bullshit {\n" +
                "    position: relative;\n" +
                "    float: left;\n" +
                "    width: 300px;\n" +
                "    padding: 30px 0;\n" +
                "    overflow: hidden;\n" +
                "    &__oops {\n" +
                "      font-size: 32px;\n" +
                "      font-weight: bold;\n" +
                "      line-height: 40px;\n" +
                "      color: #1482f0;\n" +
                "      opacity: 0;\n" +
                "      margin-bottom: 20px;\n" +
                "      animation-name: slideUp;\n" +
                "      animation-duration: 0.5s;\n" +
                "      animation-fill-mode: forwards;\n" +
                "    }\n" +
                "    &__headline {\n" +
                "      font-size: 20px;\n" +
                "      line-height: 24px;\n" +
                "      color: #222;\n" +
                "      font-weight: bold;\n" +
                "      opacity: 0;\n" +
                "      margin-bottom: 10px;\n" +
                "      animation-name: slideUp;\n" +
                "      animation-duration: 0.5s;\n" +
                "      animation-delay: 0.1s;\n" +
                "      animation-fill-mode: forwards;\n" +
                "    }\n" +
                "    &__info {\n" +
                "      font-size: 13px;\n" +
                "      line-height: 21px;\n" +
                "      color: grey;\n" +
                "      opacity: 0;\n" +
                "      margin-bottom: 30px;\n" +
                "      animation-name: slideUp;\n" +
                "      animation-duration: 0.5s;\n" +
                "      animation-delay: 0.2s;\n" +
                "      animation-fill-mode: forwards;\n" +
                "    }\n" +
                "    &__return-home {\n" +
                "      display: block;\n" +
                "      float: left;\n" +
                "      width: 110px;\n" +
                "      height: 36px;\n" +
                "      background: #1482f0;\n" +
                "      border-radius: 100px;\n" +
                "      text-align: center;\n" +
                "      color: #ffffff;\n" +
                "      opacity: 0;\n" +
                "      font-size: 14px;\n" +
                "      line-height: 36px;\n" +
                "      cursor: pointer;\n" +
                "      animation-name: slideUp;\n" +
                "      animation-duration: 0.5s;\n" +
                "      animation-delay: 0.3s;\n" +
                "      animation-fill-mode: forwards;\n" +
                "    }\n" +
                "    @keyframes slideUp {\n" +
                "      0% {\n" +
                "        transform: translateY(60px);\n" +
                "        opacity: 0;\n" +
                "      }\n" +
                "      100% {\n" +
                "        transform: translateY(0);\n" +
                "        opacity: 1;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "</style>\n";
        return _404vue;
    }

    private String generateLoginVue() {
        String loginContent = "<template>\n" +
                "  <div class=\"bg-white h-full w-full p-0 m-0\">\n" +
                "    <section class=\"pt-20 px-10 md:px-20 md:py-40 sm:h-full xl:py-60\">\n" +
                "      <!-- shadow-md  max-w-sm 最大宽度为小设备 -->\n" +
                "      <div class=\"lg:columns-2\">\n" +
                "        <div class=\"text-center\">\n" +
                "          <div class=\"p-3\">\n" +
                "            <h1 class=\"text-gray-700 text-2xl p-3 md:pt-0 lg:text-3xl\">\n" +
                "              IdeaBoss 全栈开发脚手架\n" +
                "            </h1>\n" +
                "            <p class=\"text-gray-500\">\n" +
                "              精简的前后端分离全栈开发框架，精简、专注于业务领域、快速实现创意想法。\n" +
                "            </p>\n" +
                "            <img\n" +
                "              class=\"hidden md:p-5 md:block mx-auto md:w-96\"\n" +
                "              src=\"@/assets/images/login.png\"\n" +
                "              alt=\"\"\n" +
                "            />\n" +
                "          </div>\n" +
                "          <div class=\"p-3\">\n" +
                "            <form action=\"\" class=\"text-left p-2 text-base leading-7 mt-10\">\n" +
                "              <div class=\"my-3\">\n" +
                "                <label class=\"block text-gray-700 text-sm font-bold mb-2\"\n" +
                "                  >用户名</label\n" +
                "                >\n" +
                "                <input\n" +
                "                  placeholder=\"请输入邮箱或手机号\"\n" +
                "                  v-model=\"signInRequest.email\"\n" +
                "                  class=\"border rounded w-full py-2 px-4 focus:shadow-outline outline-gray-700\"\n" +
                "                />\n" +
                "              </div>\n" +
                "              <div class=\"my-3 mt-9\">\n" +
                "                <label class=\"block text-gray-700 text-sm font-bold mb-2\"\n" +
                "                  >密码</label\n" +
                "                >\n" +
                "                <input\n" +
                "                  placeholder=\"请输入密码\"\n" +
                "                  type=\"password\"\n" +
                "                  v-model=\"signInRequest.password\"\n" +
                "                  class=\"border rounded w-full py-2 px-4 focus:shadow-outline outline-gray-700\"\n" +
                "                />\n" +
                "              </div>\n" +
                "              <div class=\"my-3\">\n" +
                "                <input type=\"checkbox\" name=\"remember\" value=\"On\" />\n" +
                "                <label class=\"p-2\">Remember me</label>\n" +
                "              </div>\n" +
                "              <div class=\"my-3\">\n" +
                "                <router-link to=\"/\">\n" +
                "                  <button\n" +
                "                    class=\"bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded w-full\"\n" +
                "                  >\n" +
                "                    登录\n" +
                "                  </button>\n" +
                "                </router-link>\n" +
                "              </div>\n" +
                "            </form>\n" +
                "            <p class=\"my-3 text-gray-500\">\n" +
                "              <a href=\"#\" class=\"\">忘记密码?</a>\n" +
                "              &nbsp;\n" +
                "              <a href=\"#\" class=\"\">没有账户?</a>\n" +
                "            </p>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <h5 class=\"text-gray-300 p-5 text-center\">\n" +
                "        全站港出品&nbsp; &copy;2022\n" +
                "      </h5>\n" +
                "    </section>\n" +
                "  </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "import Header from \"@/components/layout/Header.vue\";\n" +
                "export default {\n" +
                "  components: {\n" +
                "    Header,\n" +
                "  },\n" +
                "  data() {\n" +
                "    return {\n" +
                "      // minheight: window.screen.height,\n" +
                "      userSignUpForm: {\n" +
                "        email: \"\",\n" +
                "        name: \"\",\n" +
                "        password1: \"\",\n" +
                "        password2: \"\",\n" +
                "      },\n" +
                "      signUpRequest: {\n" +
                "        username: \"william\",\n" +
                "        email: \"xxx@qq.com\",\n" +
                "        password: \"123\",\n" +
                "        role: [\"ROLE_USER\"],\n" +
                "      },\n" +
                "      signInRequest: {\n" +
                "        email: \"xxx@qq.com\",\n" +
                "        password: \"123\",\n" +
                "      },\n" +
                "    };\n" +
                "  },\n" +
                "  methods: {},\n" +
                "};\n" +
                "</script>\n" +
                "\n" +
                "<style type=\"scss\" scoped>\n" +
                "body {\n" +
                "  background-color: white;\n" +
                "}\n" +
                "/* @import \"@/assets/css/login.css\"; */\n" +
                "</style>\n";
        return loginContent;
    }

    private String generateMainVue() {
        String mainVue = "<template>\n" +
                "  <div class=\"p-0 m-0 w-full\">\n" +
                "    <el-container class=\"p-0 m-0 w-full\">\n" +
                "      <el-header class=\"p-0 m-0 w-full\">\n" +
                "        <Header></Header>\n" +
                "      </el-header>\n" +
                "      <el-container class=\"min-h-screen p-0 m-0\">\n" +
                "        <el-aside class=\"w-fit\">\n" +
                "          <Aside></Aside>\n" +
                "        </el-aside>\n" +
                "        <el-container class=\"p-0 m-0 w-full\">\n" +
                "          <el-main class=\"bg-slate-100\">\n" +
                "            <router-view></router-view>\n" +
                "          </el-main>\n" +
                "        </el-container>\n" +
                "      </el-container>\n" +
                "    </el-container>\n" +
                "  </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "import Aside from \"@/components/layout/Aside.vue\";\n" +
                "import Header from \"@/components/layout/Header.vue\";\n" +
                "import Footer from \"@/components/layout/Footer.vue\";\n" +
                "export default {\n" +
                "  components: {\n" +
                "    Aside,\n" +
                "    Header,\n" +
                "    Footer,\n" +
                "  },\n" +
                "};\n" +
                "</script>\n" +
                "<style type=\"scss\" scoped>\n" +
                ".el-footer {\n" +
                "  padding: 0px !important;\n" +
                "  margin: 0px !important;\n" +
                "  height: 48px !important;\n" +
                "}\n" +
                ".el-aside {\n" +
                "  margin: 0 !important;\n" +
                "  /* max-width: 200px; */\n" +
                "  /* max-width: 60px; */\n" +
                "}\n" +
                "</style>\n";
        return mainVue;
    }

    private String generateHeaderVue(String projectName) {
        String header = "<template>\n" +
                "  <div\n" +
                "    class=\"w-full bg-white flex items-center justify-between flex-wrap p-1 shadow\"\n" +
                "  >\n" +
                "    <h1\n" +
                "      class=\"font-semibold text-xl tracking-tight p-3 hover:cursor-pointer ml-5\"\n" +
                "      @click=\"clickIdeaBoss\"\n" +
                "    >\n" +
                "      " + projectName + "\n" +
                "    </h1>\n" +
                "\n" +
                "    <div class=\"flex items-center mr-3 sm:mr-5 md:mr-8 lg:mr-10\">\n" +
                "      <img\n" +
                "        class=\"w-8 h-8 rounded-full mr-3\"\n" +
                "        src=\"@/assets/images/avatar.jpg\"\n" +
                "        alt=\"Johnny Williams\"\n" +
                "      />\n" +
                "      <div class=\"text-sm mr-3 hover:cursor-pointer\">\n" +
                "        <p class=\"text-gray-900\">智逸</p>\n" +
                "      </div>\n" +
                "      <p\n" +
                "        class=\"text-gray-400 hover:text-gray-700 hover:cursor-pointer pt-1\"\n" +
                "        @click=\"logout\"\n" +
                "      >\n" +
                "        <el-icon><right /></el-icon>\n" +
                "      </p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "import { ElMessageBox } from \"element-plus\";\n" +
                "// import store from \"@/store/store.js\";\n" +
                "// https://element-plus.gitee.io/zh-CN/component/message-box.html\n" +
                "export default {\n" +
                "  data() {\n" +
                "    return {\n" +
                "      isCollapse: this.$store.state.isCollapse, // TODO 设置导航是否展开(下一版本)\n" +
                "    };\n" +
                "  },\n" +
                "  methods: {\n" +
                "    logout() {\n" +
                "      ElMessageBox.confirm(\"确定注销并退出系统吗？\", \"提示\", {\n" +
                "        confirmButtonText: \"确定\",\n" +
                "        cancelButtonText: \"取消\",\n" +
                "        type: \"warning\", //\n" +
                "      })\n" +
                "        .then(() => {\n" +
                "          // 移除当前登录信息和 cookie 信息；跳转到登录页\n" +
                "\n" +
                "          // store.dispatch(\"LogOut\").then(() => {\n" +
                "          location.href = \"/login\";\n" +
                "          // });\n" +
                "          alert(\"已经退出\");\n" +
                "        })\n" +
                "        .catch(() => {});\n" +
                "    },\n" +
                "    clickIdeaBoss() {\n" +
                "      this.$store.commit(\"changeIsCollapse\");\n" +
                "    },\n" +
                "  },\n" +
                "};\n" +
                "</script>\n" +
                "\n" +
                "<style lang=\"scss\" scoped></style>\n";
        return header;
    }

    private String generateFooterVue() {
        String footer = "<template>\n" +
                "  <footer class=\"text-center border-t-0 bg-white p-0 mb-0\">\n" +
                "    <div class=\"\">\n" +
                "      <p class=\"text-sm py-3 text-gray-500\">Copyright &copy; 2021 Ideaworks</p>\n" +
                "    </div>\n" +
                "  </footer>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "export default {};\n" +
                "</script>\n" +
                "\n" +
                "<style></style>\n";
        return footer;
    }

    private String generateAsideVue(Project project) {
        String manyMenu = "";
        List<Domain> allDomains = domainService.getAllDomains(project.getId());

        for (int i = 0; i < allDomains.size(); i++) {
            String domainNameCN = allDomains.get(i).getDomainNameCN();
            String domainName = allDomains.get(i).getDomainName();
            String oneMenu =
                    "    <el-sub-menu index=\"" + (i + 1) + "\" class=\"\">\n" +
                            "      <template #title>\n" +
                            "        <el-icon><message /></el-icon>\n" +
                            "        <span>" + domainNameCN + "</span>\n" +
                            "      </template>\n" +
                            "      <router-link to=\"" + domainName.toLowerCase() + "\">\n" +
                            "        <el-menu-item index=\"" + (i + 1) + "-1\" class=\"\">" + domainNameCN + "</el-menu-item>\n" +
                            "      </router-link>\n" +
                            "    </el-sub-menu>\n" +
                            "\n";
            manyMenu += oneMenu;
        }


        String asideVue = "<template>\n" +
                "  <el-menu\n" +
                "    default-active=\"1-1\"\n" +
                "    class=\"h-full maxWidth shadow\"\n" +
                "    :class=\"!this.$store.state.isCollapse && 'asideNav'\"\n" +
                "    :collapse=\"this.$store.state.isCollapse\"\n" +
                "    :collapse-transition=\"true\"\n" +
                "    :unique-opened=\"true\"\n" +
                "  >\n" + manyMenu +
                "  </el-menu>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "export default {\n" +
                "  data() {\n" +
                "    return {\n" +
                "      // TODO 动态菜单（从路由接口中获取）\n" +
                "      menuList: [\n" +
                "        {\n" +
                "          name: \"一级菜单1\",\n" +
                "          id: \"1\",\n" +
                "          child: [{ name: \"二级菜单1-1\", id: \"1-1\" }],\n" +
                "        },\n" +
                "      ],\n" +
                "    };\n" +
                "  },\n" +
                "  methods: {},\n" +
                "};\n" +
                "</script>\n" +
                "\n" +
                "<style type=\"scss\" scoped>\n" +
                ".asideNav {\n" +
                "  width: 200px;\n" +
                "}\n" +
                ".maxWidth {\n" +
                "  max-width: 200px;\n" +
                "}\n" +
                "</style>\n";
        return asideVue;
    }

    private String generateStoreJs() {
        String storeJs = "import { createStore } from \"vuex\";\n" +
                "\n" +
                "// 创建一个新的 store 实例\n" +
                "const store = createStore({\n" +
                "  // 声明变量\n" +
                "  state() {\n" +
                "    return {\n" +
                "      isCollapse: false, //侧边栏是否坍缩\n" +
                "    };\n" +
                "  },\n" +
                "  // 修改变量\n" +
                "  mutations: {\n" +
                "    changeIsCollapse(state) {\n" +
                "      state.isCollapse = !state.isCollapse;\n" +
                "    },\n" +
                "  },\n" +
                "});\n" +
                "export default store;\n";
        return storeJs;
    }

    private String generateRequestJs(String backEndPort) {
        String requestJS = "import axios from \"axios\";\n" +
                "import { ElMessage, ElMessageBox } from \"element-plus\";\n" +
                "// import store from \"@/store\";\n" +
                "\n" +
                "// create an axios instance\n" +
                "const instance = axios.create({\n" +
                "  //   baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url\n" +
                "  baseURL: \"http://localhost:" + backEndPort + "/\",\n" +
                "\n" +
                "  timeout: 5000,\n" +
                "  headers: {\n" +
                "    \"Content-Type\": \"application/json\",\n" +
                "  },\n" +
                "});\n" +
                "\n" +
                "instance.interceptors.request.use(\n" +
                "  (config) => {\n" +
                "    // 在发送请求之前设置请求头\n" +
                "    config.headers.Authorization = \"Bearer \" + localStorage.getItem(\"token\");\n" +
                "    return config;\n" +
                "  },\n" +
                "  (error) => {\n" +
                "    if (error.response.status == 401) {\n" +
                "      ElMessage({\n" +
                "        message: \"请进行登录\",\n" +
                "        type: \"error\",\n" +
                "        duration: 5 * 1000,\n" +
                "      });\n" +
                "    }\n" +
                "    console.log(error); // for debug\n" +
                "    return Promise.reject(error);\n" +
                "  }\n" +
                ");\n" +
                "\n" +
                "instance.interceptors.response.use(\n" +
                "  (response) => {\n" +
                "    let res = response;\n" +
                "    if (res.status !== 200) {\n" +
                "      ElMessage({\n" +
                "        message: res.message || \"Error\",\n" +
                "        type: \"error\",\n" +
                "        duration: 5 * 1000,\n" +
                "      });\n" +
                "\n" +
                "      // 401: Token expired;\n" +
                "      if (res.status === 401) {\n" +
                "        ElMessageBox.confirm(\n" +
                "          \"你已经登出，你可以点击取消继续留在当前页，或者重新登录\",\n" +
                "          \"确认退出\",\n" +
                "          {\n" +
                "            confirmButtonText: \"重新登录\",\n" +
                "            cancelButtonText: \"取消\",\n" +
                "            type: \"warning\",\n" +
                "          }\n" +
                "        ).then(() => {\n" +
                "          // 进行页面跳转和其他处理\n" +
                "        });\n" +
                "      }\n" +
                "      return Promise.reject(new Error(res.message || \"Error\"));\n" +
                "    } else {\n" +
                "      return response;\n" +
                "    }\n" +
                "  },\n" +
                "  (error) => {\n" +
                "    console.log(\"err\" + error);\n" +
                "    ElMessage({\n" +
                "      message: error.message,\n" +
                "      type: \"error\",\n" +
                "      duration: 5 * 1000,\n" +
                "    });\n" +
                "    return Promise.reject(error);\n" +
                "  }\n" +
                ");\n" +
                "\n" +
                "export default instance;\n";
        return requestJS;
    }

    private String generateAppVue() {
        String appVue = "<template>\n" +
                "  <router-view />\n" +
                "</template>\n";
        return appVue;
    }

    private String generateIndexCss() {
        String indexCss = "/* 当前 ./src/index.css 引入 tailwind */\n" +
                "@tailwind base;\n" +
                "@tailwind components;\n" +
                "@tailwind utilities;\n" +
                "/* 覆盖 element ui 样式 */\n" +
                ":root {\n" +
                "  --el-color-primary: #64748b;\n" +
                "  --el-color-primary-rgb: 64, 158, 255;\n" +
                "  --el-color-success-rgb: 103, 194, 58;\n" +
                "  --el-color-warning-rgb: 230, 162, 60;\n" +
                "  --el-color-danger-rgb: 245, 108, 108;\n" +
                "  --el-color-error-rgb: 245, 108, 108;\n" +
                "  --el-color-info-rgb: 144, 147, 153;\n" +
                "  --el-color-primary-light-1: #1f2937;\n" +
                "  --el-color-primary-light-2: #374151;\n" +
                "  --el-color-primary-light-3: #4b5563;\n" +
                "  --el-color-primary-light-4: #6b7280;\n" +
                "  --el-color-primary-light-5: #9ca3af;\n" +
                "  --el-color-primary-light-6: #d1d5db;\n" +
                "  --el-color-primary-light-7: #e5e7eb;\n" +
                "  --el-color-primary-light-8: #f3f4f6;\n" +
                "  --el-color-primary-light-9: #f9fafb;\n" +
                "  --el-color-primary-dark-2: #337ecc;\n" +
                "  --el-color-success: #67c23a;\n" +
                "  --el-color-success-light-3: #95d475;\n" +
                "  --el-color-success-light-5: #b3e19d;\n" +
                "  --el-color-success-light-7: #d1edc4;\n" +
                "  --el-color-success-light-8: #e1f3d8;\n" +
                "  --el-color-success-light-9: #f0f9eb;\n" +
                "  --el-color-success-dark-2: #529b2e;\n" +
                "  --el-color-warning: #e6a23c;\n" +
                "  --el-color-warning-light-3: #eebe77;\n" +
                "  --el-color-warning-light-5: #f3d19e;\n" +
                "  --el-color-warning-light-7: #f8e3c5;\n" +
                "  --el-color-warning-light-8: #faecd8;\n" +
                "  --el-color-warning-light-9: #fdf6ec;\n" +
                "  --el-color-warning-dark-2: #b88230;\n" +
                "  --el-color-danger: #f56c6c;\n" +
                "  --el-color-danger-light-3: #f89898;\n" +
                "  --el-color-danger-light-5: #fab6b6;\n" +
                "  --el-color-danger-light-7: #fcd3d3;\n" +
                "  --el-color-danger-light-8: #fde2e2;\n" +
                "  --el-color-danger-light-9: #fef0f0;\n" +
                "  --el-color-danger-dark-2: #c45656;\n" +
                "  --el-color-error: #f56c6c;\n" +
                "  --el-color-error-light-3: #f89898;\n" +
                "  --el-color-error-light-5: #fab6b6;\n" +
                "  --el-color-error-light-7: #fcd3d3;\n" +
                "  --el-color-error-light-8: #fde2e2;\n" +
                "  --el-color-error-light-9: #fef0f0;\n" +
                "  --el-color-error-dark-2: #c45656;\n" +
                "  --el-color-info: #909399;\n" +
                "  --el-color-info-light-3: #b1b3b8;\n" +
                "  --el-color-info-light-5: #c8c9cc;\n" +
                "  --el-color-info-light-7: #dedfe0;\n" +
                "  --el-color-info-light-8: #e9e9eb;\n" +
                "  --el-color-info-light-9: #f4f4f5;\n" +
                "  --el-color-info-dark-2: #73767a;\n" +
                "  --el-bg-color: #f5f7fa;\n" +
                "  --el-border-width-base: 1px;\n" +
                "  --el-border-style-base: solid;\n" +
                "}\n";
        return indexCss;
    }

    private String generateMainJs() {
        String mainJs = "import { createApp } from \"vue\";\n" +
                "import App from \"./App.vue\";\n" +
                "import ElementPlus from \"element-plus\";\n" +
                "import * as Icons from \"@element-plus/icons-vue\"; // element-plus/icon 组件注册\n" +
                "import \"element-plus/dist/index.css\";\n" +
                "import \"./index.css\";\n" +
                "import router from \"./router.js\"; // router 引入\n" +
                "import Store from \"./store/store.js\"; // 状态引入\n" +
                "// 自定义组件注册\n" +
                "\n" +
                "const app = createApp(App);\n" +
                "\n" +
                "// 注册 element-plus/icon 全局组件\n" +
                "Object.keys(Icons).forEach((key) => {\n" +
                "  app.component(key, Icons[key]);\n" +
                "});\n" +
                "\n" +
                "// 引入其他组件\n" +
                "app.use(ElementPlus);\n" +
                "app.use(router);\n" +
                "app.use(Store);\n" +
                "\n" +
                "app.mount(\"#app\");\n";
        return mainJs;
    }

    private String generatePostcssConfigJs() {
        String postcssConfigJsContent = "// 引入 taiwind 配置\n" +
                "module.exports = {\n" +
                "  plugins: {\n" +
                "    tailwindcss: {},\n" +
                "    autoprefixer: {},\n" +
                "  },\n" +
                "};\n";
        return postcssConfigJsContent;
    }

    private String generateTailwindConfigJs() {
        String tailwindConfigJs = "module.exports = {\n" +
                "  purge: [\"./index.html\", \"./src/**/*.{vue,js,ts,jsx,tsx}\"],\n" +
                "  darkMode: false,\n" +
                "  content: [],\n" +
                "  theme: {\n" +
                "    extend: {},\n" +
                "  },\n" +
                "  plugins: [],\n" +
                "};\n";
        return tailwindConfigJs;
    }

    private String generateViteConfigJs(String port) {
        String viteConfigJs = "import { defineConfig } from \"vite\";\n" +
                "import vue from \"@vitejs/plugin-vue\";\n" +
                "import path from \"path\";\n" +
                "\n" +
                "// https://vitejs.dev/config/\n" +
                "export default defineConfig({\n" +
                "  plugins: [vue()],\n" +
                "  resolve: {\n" +
                "    alias: {\n" +
                "      \"@\": path.resolve(__dirname, \"src\"),\n" +
                "      components: path.resolve(__dirname, \"src/components\"),\n" +
                "    },\n" +
                "  },\n" +
                "\n" +
                "  // base: \"https://www.ideaworks.club/ideabossfront/dist\",\n" +
                "  // 项目打包后的访问路径 ./ 为 80 端口，如上是将打包后的内容放到 /ideabossfront/dist 文件夹下\n" +
                "  base: \"./\",\n" +
                "  server: {\n" +
                "    // host: \"0.0.0.0\",\n" +
                "    port: " + port + ", // 项目运行端口 \n" +
                "  },\n" +
                "});\n";
        return viteConfigJs;
    }

    private String generateReadMe() {
        String readMeContent = "# xxx 框架";
        return readMeContent;
    }

    private String generatePackageJson(String projectName) {
        String packageJson = "{\n" +
                "  \"name\": \"" + projectName + "\",\n" +
                "  \"private\": true,\n" +
                "  \"version\": \"0.0.0\",\n" +
                "  \"scripts\": {\n" +
                "    \"dev\": \"vite\",\n" +
                "    \"build\": \"vite build\",\n" +
                "    \"preview\": \"vite preview\"\n" +
                "  },\n" +
                "  \"dependencies\": {\n" +
                "    \"@element-plus/icons-vue\": \"^1.1.1\",\n" +
                "    \"axios\": \"^0.26.1\",\n" +
                "    \"element-plus\": \"^2.0.5\",\n" +
                "    \"vue\": \"^3.2.25\",\n" +
                "    \"vue-router\": \"^4.0.13\",\n" +
                "    \"vuex\": \"^4.0.2\"\n" +
                "  },\n" +
                "  \"devDependencies\": {\n" +
                "    \"@vitejs/plugin-vue\": \"^2.2.0\",\n" +
                "    \"autoprefixer\": \"^10.4.2\",\n" +
                "    \"postcss\": \"^8.4.8\",\n" +
                "    \"sass\": \"^1.49.9\",\n" +
                "    \"tailwindcss\": \"^3.0.23\",\n" +
                "    \"vite\": \"^2.8.0\"\n" +
                "  }\n" +
                "}\n";
        return packageJson;
    }

    private String generateIndex(String projectName) {
        String indexContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <link rel=\"icon\" href=\"/favicon.ico\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>" + projectName + "</title>\n" +
                "  </head>\n" +
                "  <body class=\"w-full h-full text-slate-600\">\n" +
                "    <div id=\"app\" class=\"\"></div>\n" +
                "    <script type=\"module\" src=\"/src/main.js\"></script>\n" +
                "  </body>\n" +
                "</html>";
        return indexContent;
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
