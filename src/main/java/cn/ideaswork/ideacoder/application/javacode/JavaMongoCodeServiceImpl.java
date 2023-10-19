package cn.ideaswork.ideacoder.application.javacode;

import cn.hutool.core.io.file.FileWriter;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainField;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JavaMongoCodeServiceImpl implements JavaMongoCodeService {
    @Autowired
    ProjectService projectService;

    @Autowired
    DomainService domainService;

    @Override
    public String generateDomain(Domain domain) {
        String domainName = domain.getDomainName();
        String tableName = domain.getTableName();
        String domainNameCN = domain.getDomainNameCN();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;


        // 构建 domain 类的注解
        List<AnnotationSpec> domainAnnotationSpecList = getDomainTypeSpecAnnotationSpecs(tableName, domainNameCN);

        // 构建 domain 类的属性 List
        List<FieldSpec> domainFieldSpecList = new ArrayList<>();
        List<DomainField> domainFieldList = domain.getDomainFieldList();

        // 计算属性列表中主键的数量
        int idNum = getIdNum(domainFieldList);

        if (idNum == 1) {
            // 单主键生成
            for (DomainField column : domainFieldList) {
                if (column.getIsKey()) {
                    FieldSpec idFieldSpec = getIdFieldSpecDomain(column);
                    domainFieldSpecList.add(idFieldSpec);
                }
            }
        } else {
            return "";
        }

        // 其他属性生成：非主键生成
        for (DomainField column : domainFieldList) {
            if (!column.getIsKey()) {
                FieldSpec fieldSpec = getOtherFieldSpecDomain(column);
                domainFieldSpecList.add(fieldSpec);
            }
        }

        TypeSpec classTypeSpec = TypeSpec
                .classBuilder(domainName)
                .addFields(domainFieldSpecList)
                .addAnnotations(domainAnnotationSpecList)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + domainName.toLowerCase(), classTypeSpec).build();
        return javaFile.toString();
    }

    private List<AnnotationSpec> getDomainTypeSpecAnnotationSpecs(String tableName, String tableNameCN) {
        List<AnnotationSpec> domainAnnotationSpecList = new ArrayList<>();

        domainAnnotationSpecList.add(AnnotationSpec.builder(Document.class).addMember("collection", "$S", tableName).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(Data.class).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(NoArgsConstructor.class).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(AllArgsConstructor.class).build());
        // 添加 @Schema
        domainAnnotationSpecList.add(AnnotationSpec.builder(Schema.class).addMember("description", "$S", tableNameCN).build());
        // 添加 @ApiModel TODO
//        domainAnnotationSpecList.add(AnnotationSpec.builder(ApiModel.class).addMember("value", "$S", comment).addMember("description", "$S", comment).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(Accessors.class).addMember("chain", "$L", true).build());
        return domainAnnotationSpecList;
    }

    private FieldSpec getIdFieldSpecDomain(DomainField column) {
        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
//				fieldAnnotations.add(AnnotationSpec.builder(GeneratedValue.class).addMember("strategy", "auto").build());

        // 添加 @Id
        fieldAnnotations.add(AnnotationSpec.builder(Id.class).build());

//         添加 @Schema
        fieldAnnotations.add(AnnotationSpec.builder(Schema.class)
                .addMember("description", "$S", column.getFieldNameCN()).build());
        // 添加 @ApiModelProperty TODO
//        fieldAnnotations.add(AnnotationSpec.builder(ApiModelProperty.class)
//                .addMember("value", "$S", column.getFieldComment()).build());

        return FieldSpec
                .builder(this.getFieldType(column.getDataType()), column.getFieldName())
                .addModifiers(Modifier.PRIVATE)
                .addAnnotations(fieldAnnotations)
                .build();
    }


    private FieldSpec getOtherFieldSpecDomain(DomainField column) {
        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();

        // 添加 @DateTimeFormat(pattern = Constants.PATTERN_DATE)
        //	   @JsonFormat(pattern = Constants.PATTERN_DATE, timezone = "GMT+8")

        if ("Date".equals(column.getDataType())) {
            fieldAnnotations.add(AnnotationSpec.builder(DateTimeFormat.class)
                    .addMember("pattern", "$S", "yyyy-MM-dd HH:mm:ss").build());

            fieldAnnotations.add(AnnotationSpec.builder(JsonFormat.class)
                    .addMember("pattern", "$S", "yyyy-MM-dd HH:mm:ss").addMember("timezone", "$S", "GTM+8").build());
        }

        // 添加 @ApiModelProperty
        fieldAnnotations.add(AnnotationSpec.builder(Schema.class)
                .addMember("description", "$S", column.getFieldNameCN()).build());
//        fieldAnnotations.add(AnnotationSpec.builder(ApiModelProperty.class)
//                .addMember("value", "$S", column.getFieldNameCN()).build());

        return FieldSpec
                .builder(getFieldType(column.getDataType()), column.getFieldName())
                .addModifiers(Modifier.PRIVATE)
                .addAnnotations(fieldAnnotations)
                .build();
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public String generateDTO(Domain domain) {
        List<AnnotationSpec> DTOClassAnnotations = new ArrayList<>();
        List<FieldSpec> DTOFields = new ArrayList<>();

        String domainName = domain.getDomainName();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;

        // 类上的 @Data 注解
        DTOClassAnnotations.add(AnnotationSpec.builder(Data.class).build());
        DTOClassAnnotations.add(AnnotationSpec.builder(Builder.class).build());
        DTOClassAnnotations.add(AnnotationSpec.builder(NoArgsConstructor.class).build());
        DTOClassAnnotations.add(AnnotationSpec.builder(AllArgsConstructor.class).build());

        // 实体的属性列表
        List<DomainField> columns = domain.getDomainFieldList();

        for (DomainField column : columns) {
            List<AnnotationSpec> fieldAnnotations = new ArrayList<>();

            // 添加 @Schema
            fieldAnnotations.add(AnnotationSpec.builder(Schema.class)
                    .addMember("description", "$S", column.getFieldNameCN())
                    .build());
            // 添加 @ApiModel
//            fieldAnnotations.add(AnnotationSpec.builder(ApiModelProperty.class)
//                    .addMember("value", "$S", column.getFieldNameCN())
//                    .build());

            // 添加 @DateTimeFormat(pattern = Constants.PATTERN_DATE)
            //	   @JsonFormat(pattern = Constants.PATTERN_DATE, timezone = "GMT+8")
            if (column.getDataType().equals("Date")) {
                fieldAnnotations.add(
                        AnnotationSpec.builder(DateTimeFormat.class)
                                .addMember("pattern", "$S", "yyyy-MM-dd")
                                .build()
                );

                fieldAnnotations.add(AnnotationSpec.builder(JsonFormat.class)
                        .addMember("pattern", "$S", "yyyy-MM-dd")
                        .addMember("timezone", "$S", "GTM+8")
                        .build());
            }

            FieldSpec DTOClassFieldSpec = FieldSpec
                    .builder(getFieldType(column.getDataType()), column.getFieldName())
                    .addModifiers(Modifier.PRIVATE)
                    .addAnnotations(fieldAnnotations).build();

            DTOFields.add(DTOClassFieldSpec);
        }

        TypeSpec DTOClassTypeSpec = TypeSpec.classBuilder(domainName + "DTO")
                .addFields(DTOFields)
                .addAnnotations(DTOClassAnnotations)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + domainName.toLowerCase(), DTOClassTypeSpec).build();
        return javaFile.toString();
    }

    // ------------------------------------------------------------------------------------------------------------------
    @Override
    public String generateService(Domain domain) {
        List<MethodSpec> serviceClassMethods = new ArrayList<>();
        String domainName = domain.getDomainName();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;

        List<DomainField> columns = domain.getDomainFieldList(); // 获取的实体属性列表

        ClassName domainClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName);
        ClassName pkClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "PK");

        TypeName pkTypeName = pkClass.box();

        List<DomainField> idColumns = columns.stream().filter(DomainField::getIsKey).collect(Collectors.toList());
        int idNum = idColumns.size();

        DomainField columnDomainField = null;
        for (DomainField column : columns) {
            if (column.getIsKey()) {
                columnDomainField = column;
            }
        }
        // 两种主键类型
        ParameterSpec pkParameterSpec = ParameterSpec.builder(pkTypeName, "id").addModifiers(Modifier.FINAL).build();
        assert columnDomainField != null;
        ParameterSpec idParameterSpec = ParameterSpec.builder(this.getFieldType(columnDomainField.getDataType()), "id").addModifiers(Modifier.FINAL).build();


        // service 方法的修饰符
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.ABSTRACT);


        MethodSpec saveMethodSpec = getMethodSpec_Service_Save(modifiers, domainName, domainClass);
        MethodSpec GetAllMethodSpec = getMethodSpec_Service_GetAll(domainName, domainClass, modifiers);
        MethodSpec GetByIdMethodSpec = getMethodSpec_Service_GetById(domainName, domainClass, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec UpdateByIdMethodSpec = getMethodSpec_Service_UpdateById(domainName, domainClass, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec deleteByIdMethodSpec = getMethodSpec_Service_DeleteById(domainName, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec isExitMethodSpec = getMethodSpec_Service_IsExist(domainName, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec ConditionPageMethodSpec = getMethodSpec_Service_GetPageByCondition(domainName, packageName, domainClass, modifiers);
        MethodSpec ConditionListMethodSpec = getMethodSpec_Service_GetListByCondition(domainName, packageName, domainClass, modifiers);

        serviceClassMethods.add(saveMethodSpec);
        serviceClassMethods.add(GetAllMethodSpec);
        serviceClassMethods.add(GetByIdMethodSpec);
        serviceClassMethods.add(UpdateByIdMethodSpec);
        serviceClassMethods.add(deleteByIdMethodSpec);
        serviceClassMethods.add(isExitMethodSpec);
        serviceClassMethods.add(ConditionPageMethodSpec);
        serviceClassMethods.add(ConditionListMethodSpec);

        // 生成文件并返回
        TypeSpec serviceTypeSpec =
                TypeSpec
                        .interfaceBuilder(domainName + "Service")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethods(serviceClassMethods)
                        .build();

        JavaFile javaFile =
                JavaFile
                        .builder((packageName + ".domain." + domainName.toLowerCase()).trim(), serviceTypeSpec)
                        .build();

        return javaFile.toString();
    }

    private MethodSpec getMethodSpec_Service_Save(List<Modifier> modifiers, String entityName, ClassName className) {
        List<ParameterSpec> saveMethodParameters = new ArrayList<>();
        saveMethodParameters
                .add(
                        ParameterSpec
                                .builder(className, className.simpleName().toLowerCase())
                                .build()
                );

        return MethodSpec
                .methodBuilder("save" + entityName)
                .addModifiers(modifiers)
                .addParameters(saveMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetAll(String entityName, ClassName className, List<Modifier> modifiers) {
        ClassName listClass = ClassName.get("java.util", "List");
        TypeName ListClassName = ParameterizedTypeName.get(listClass, className);

        return MethodSpec
                .methodBuilder("getAll" + entityName + "s")
                .addModifiers(modifiers)
                .returns(ListClassName)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetById(String entityName, ClassName className, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> getMethodParameters = new ArrayList<>();

        getMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );

        return MethodSpec
                .methodBuilder(
                        "get" + entityName + "ById"
                )
                .addModifiers(modifiers)
                .addParameters(getMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_UpdateById(String entityName, ClassName className, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> updateMethodParameters = new ArrayList<>();

        // 要更新的新实体
        updateMethodParameters
                .add(ParameterSpec
                        .builder(className, className.simpleName().toLowerCase())
                        .build());

        // 要更新的实体名
        updateMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );

        return MethodSpec
                .methodBuilder("update" + entityName + "ById")
                .addModifiers(modifiers)
                .addParameters(updateMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_DeleteById(String entityName, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> deleteMethodParameters = new ArrayList<>();

        deleteMethodParameters
                .add(idNum > 1 ? pkParameterSpec : idParameterSpec);

        return MethodSpec
                .methodBuilder("delete" + entityName + "ById")
                .addModifiers(modifiers)
                .addParameters(deleteMethodParameters)
                .returns(void.class)
                .build();
    }

    private MethodSpec getMethodSpec_Service_IsExist(String entityName, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> isExitMethodParameters = new ArrayList<>();

        isExitMethodParameters
                .add(idNum > 1 ? pkParameterSpec : idParameterSpec);

        return MethodSpec
                .methodBuilder("is" + entityName + "Exist")
                .addModifiers(modifiers)
                .addParameters(isExitMethodParameters)
                .returns(Boolean.class)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetPageByCondition(String entityName, String packageName, ClassName className, List<Modifier> modifiers) {
        ClassName listPage = ClassName.get("org.springframework.data.domain", "Page");
        ClassName condition1 = ClassName.get((packageName + ".domain." + entityName.toLowerCase()).trim(), entityName + "DTO");
        ClassName pageable = ClassName.get("org.springframework.data.domain", "Pageable");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listPage, className);

        getPageMethodParameters
                .add(
                        ParameterSpec.builder(condition1, (entityName).toLowerCase() + "DTO").build()
                );
        getPageMethodParameters
                .add(
                        ParameterSpec.builder(pageable, "pageable").build()
                );

        return MethodSpec
                .methodBuilder("getPageByCondition")
                .addModifiers(modifiers)
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetListByCondition(String entityName, String packageName, ClassName className, List<Modifier> modifiers) {
        ClassName listClass = ClassName.get("java.util", "List");
//        ClassName condition = ClassName.get((packageName + ".domain." + entityName.toLowerCase()).trim(), entityName);
        ClassName condition1 = ClassName.get((packageName + ".domain." + entityName.toLowerCase()).trim(), entityName + "DTO");
        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listClass, className);

        getPageMethodParameters
                .add(ParameterSpec.builder(condition1, (entityName).toLowerCase() + "DTO").build());

        return MethodSpec
                .methodBuilder("getListByCondition")
                .addModifiers(modifiers)
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }


    // -------------------------------------------------------------------------------------------------------------------


    @Override
    public String generateRepository(Domain domain) {
        List<AnnotationSpec> repositoryClassAnnotations = new ArrayList<>();

        String domainName = domain.getDomainName();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;

        List<DomainField> columns = domain.getDomainFieldList(); // 获取的实体属性列表

        // 计算属性列表中主键的数
        repositoryClassAnnotations
                .add(AnnotationSpec.builder(Repository.class).build());

        ClassName mongoClassName = ClassName.get("org.springframework.data.mongodb.repository", "MongoRepository");
        ClassName stringClass = ClassName.get("java.lang", "String");// 主键变量目前默认为 String 类型

        ParameterizedTypeName mongoPk = ParameterizedTypeName.get(
                mongoClassName,
                ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName),
                stringClass
        );

        TypeSpec repositorySpec =
                TypeSpec
                        .interfaceBuilder(domainName + "Dao")
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotations(repositoryClassAnnotations)
                        .addSuperinterface(mongoPk)
                        .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + domainName.toLowerCase(), repositorySpec).build();
        return javaFile.toString();

    }

    private int getIdNum(List<DomainField> columns) {
        List<DomainField> idColumns = columns.stream().filter(DomainField::getIsKey).collect(Collectors.toList());
        return idColumns.size();
    }

    // ------------------------------------------------------------------------------------------------------------------
    @Override
    public String generateServiceImpl(Domain domain) {
        List<MethodSpec> methods = new ArrayList<>();

        String domainName = domain.getDomainName();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;

        // 实体类的类名
        ClassName modelClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName);
        ClassName DTOClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "DTO");
        ClassName serviceInterface = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "Service");
        ClassName repositoryClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "Dao");
        ClassName condition = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "DTO");
        ClassName mongoTemplateClass = ClassName.get("org.springframework.data.mongodb.core", "MongoTemplate");

        // 实体的属性列表
        List<DomainField> columns = domain.getDomainFieldList();

        DomainField columnDomainField = null;
        for (DomainField column : columns) {
            if (column.getIsKey()) {
                columnDomainField = column;
            }
        }
        int idNum = getIdNum(columns);

        // 两种主键类型
        ParameterSpec idParameterSpec = ParameterSpec.builder(this.getFieldType(columnDomainField.getDataType()), "id").addModifiers(Modifier.FINAL).build();

        // service 方法的修饰符
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PUBLIC);

        MethodSpec saveMethodSpec = getMethodSpec_ServiceImpl_Save(domainName, modelClass, modifiers);
        MethodSpec GetAllMethodSpec = getMethodSpec_ServiceImpl_GetAll(domainName, modelClass, modifiers);
        MethodSpec GetByIdMethodSpec = getMethodSpec_ServiceImpl_GetById(domainName, modelClass, modifiers, idNum, idParameterSpec);
        MethodSpec UpdateByIdMethodSpec = getMethodSpec_ServiceImpl_UpdateById(domainName, modelClass, modifiers, idNum, idParameterSpec);
        MethodSpec deleteByIdMethodSpec = getMethodSpec_ServiceImpl_DeleteById(domainName, modifiers, idNum, idParameterSpec);
        MethodSpec isExitMethodSpec = getMethodSpec_ServiceImpl_IsExist(domainName, modifiers, idNum, idParameterSpec);


        MethodSpec ConditionListMethodSpec = getMethodSpec_ServiceImpl_GetListByCondition(domain, modelClass, modifiers, condition);
        MethodSpec ConditionPageMethodSpec = getMethodSpec_ServiceImpl_GetPageByCondition(domain, modelClass, modifiers, condition);
        MethodSpec listToPageMethodSpec = getMethodSpec_ServiceImpl_ListToPageMethodSpec(domain, modelClass, modifiers, condition);
        MethodSpec mapToEntityMethodSpec = getMethodSpec_ServiceImpl_MapToEntity(domainName, modelClass, DTOClass, modifiers);
        MethodSpec mapToDTOMethodSpec = getMethodSpec_ServiceImpl_MapToDTO(domainName, modelClass, DTOClass, modifiers);

        methods.add(saveMethodSpec);
        methods.add(GetAllMethodSpec);
        methods.add(GetByIdMethodSpec);
        methods.add(UpdateByIdMethodSpec);
        methods.add(deleteByIdMethodSpec);
        methods.add(isExitMethodSpec);
        methods.add(ConditionPageMethodSpec);
        methods.add(ConditionListMethodSpec);
        methods.add(listToPageMethodSpec);
        methods.add(mapToEntityMethodSpec);
        methods.add(mapToDTOMethodSpec);

        // 生成文件并返回
        List<FieldSpec> fields = new ArrayList<>();
        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();

        fieldAnnotations.add(AnnotationSpec.builder(Autowired.class).build());

        fields.add(
                FieldSpec
                        .builder(
                                repositoryClass,
                                (domainName.toLowerCase() + "Dao").trim()
                        )
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotations(fieldAnnotations)
                        .build()
        );
        fields.add(
                FieldSpec
                        .builder(
                                mongoTemplateClass,
                                "mongoTemplate"
                        )
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotations(fieldAnnotations)
                        .build()
        );


        List<AnnotationSpec> classAnnotationSpecs = new ArrayList<>();
        classAnnotationSpecs.add(AnnotationSpec.builder(Service.class).build());

        TypeSpec serviceImplTypeSpec =
                TypeSpec
                        .classBuilder(domainName + "ServiceImpl")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethods(methods)
                        .addAnnotations(classAnnotationSpecs)
                        .addFields(fields)
                        .addSuperinterface(serviceInterface)
                        .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + domainName.toLowerCase(), serviceImplTypeSpec).build();

        String fileString = javaFile.toString();

        return fileString;
    }

    private MethodSpec getMethodSpec_ServiceImpl_ListToPageMethodSpec(Domain domain, ClassName modelClass, List<Modifier> modifiers, ClassName condition) {
        /**
         *   public <T> Page<T> listToPage(List<T> list, Pageable pageable) {
         *     int start = (int)pageable.getOffset();
         *     int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
         *     return new PageImpl<T>(list.subList(start, end), pageable, list.size());
         *   }
         */

        ClassName PageImplClass = ClassName.get("org.springframework.data.domain", "PageImpl");
        ClassName PageClass = ClassName.get("org.springframework.data.domain", "Page");
        ClassName pageableClass = ClassName.get("org.springframework.data.domain", "Pageable");
        ClassName objectClass = ClassName.get("java.lang.Object", "Object");
        TypeVariableName tClassName = TypeVariableName.get("T");
        TypeName pageTClass = ParameterizedTypeName.get(PageClass, tClassName);
//        ParameterizedTypeName listModelClass = ParameterizedTypeName.get(ClassName.get(List.class), modelClass);
        ParameterizedTypeName listTClass = ParameterizedTypeName.get(ClassName.get(List.class), tClassName);
        List<ParameterSpec> listToPageMethodParameterSpec = new ArrayList<>();

        // 要更新的新实体
        listToPageMethodParameterSpec.add(
                ParameterSpec
                        .builder(listTClass, "list")
                        .addModifiers(Modifier.FINAL).build()
        );

        listToPageMethodParameterSpec.add(
                ParameterSpec.builder(pageableClass, "pageable").build()
        );

        List<AnnotationSpec> ListToPageMethodSpec = new ArrayList<>();


        return MethodSpec
                .methodBuilder("listToPage")
                .addAnnotations(ListToPageMethodSpec)
                .addModifiers(modifiers)
                .addStatement("int start = (int)pageable.getOffset()")
                .addStatement("int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize())")
                .addStatement("return new $T<T>(list.subList(start, end), pageable, list.size())", PageImplClass)
                .addParameters(listToPageMethodParameterSpec)
                .returns(pageTClass)
                .build();

    }

    private MethodSpec getMethodSpec_ServiceImpl_GetPageByCondition(Domain domain, ClassName modelClass, List<Modifier> modifiers, ClassName condition) {
        ClassName listPage = ClassName.get("org.springframework.data.domain", "Page");
        ClassName pageable = ClassName.get("org.springframework.data.domain", "Pageable");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listPage, modelClass);
        getPageMethodParameters.add(
                ParameterSpec
                        .builder(condition, (domain.getDomainName().toLowerCase() + "DTO"))
                        .addModifiers(Modifier.FINAL)
                        .build()
        );
        getPageMethodParameters
                .add(
                        ParameterSpec
                                .builder(pageable, "pageable")
                                .addModifiers(Modifier.FINAL)
                                .build()
                );

        List<AnnotationSpec> getPageByConditionMethodAnnotationSpecs = new ArrayList<>();
        getPageByConditionMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        /*
        * Query query = new Query();
        if (StringUtils.isNotBlank(project.getEnname())) {
            query.addCriteria(Criteria.where("enname").is(project.getEnname()));
        }
        if (StringUtils.isNotBlank(project.getStatus())) {
            query.addCriteria(Criteria.where("status").is(project.getStatus()));
        }
        * */
        ClassName className = ClassName.get("org.springframework.data.mongodb.core.query", "Query");
        TypeName classNameBox = className.box();
        CodeBlock getPageByConditionCodeBlock = CodeBlock
                .builder()
                .add("$T query = new $T()", classNameBox, classNameBox)
                .build();

        StringBuilder codeBlockString = new StringBuilder();

        String domainName = domain.getDomainName();
        for (DomainField column : domain.getDomainFieldList()) {
            String typeString = column.getDataType();
            if (typeString.equals("String")) {
                codeBlockString
                        .append("if(!StringUtils.isEmpty(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1)).append("()").append(")){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").is(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            } else if (typeString.equals("Date")) {
                codeBlockString
                        .append("if(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()").append("!=null){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").gte(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            } else {
                codeBlockString
                        .append("if(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()").append("!=null){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").is(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            }
        }
        codeBlockString.append("Sort sort = pageable.getSort();\n")
                .append("query.with(sort);\n");

        return MethodSpec
                .methodBuilder("getPageByCondition")
                .addModifiers(modifiers)
                .addAnnotations(getPageByConditionMethodAnnotationSpecs)
                .addStatement(getPageByConditionCodeBlock)
                .addCode(String.valueOf(codeBlockString))
                .addStatement(
                        "return this.listToPage(mongoTemplate.find(query," + domainName + ".class),pageable)"
                )
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetListByCondition(Domain domain, ClassName modelClass, List<Modifier> modifiers, ClassName condition) {
        ClassName list = ClassName.get("java.util", "List");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();
        String domainName = domain.getDomainName();
        TypeName PageClasses = ParameterizedTypeName.get(list, modelClass);
        getPageMethodParameters.add(
                ParameterSpec
                        .builder(condition, (domainName.toLowerCase() + "DTO"))
                        .addModifiers(Modifier.FINAL)
                        .build()
        );

        List<AnnotationSpec> getPageByConditionMethodAnnotationSpecs = new ArrayList<>();
        getPageByConditionMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        ClassName className = ClassName.get("org.springframework.data.mongodb.core.query", "Query");
        TypeName classNameBox = className.box();
        CodeBlock getPageByConditionCodeBlock = CodeBlock
                .builder()
                .add("$T query = new $T()", classNameBox, classNameBox)
                .build();

        StringBuilder codeBlockString = new StringBuilder();

        for (DomainField column : domain.getDomainFieldList()) {
            String typeString = column.getDataType();
            if (typeString.equals("String")) {
                codeBlockString
                        .append("if(!StringUtils.isEmpty(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1)).append("()").append(")){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").is(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            } else if (typeString.equals("Date")) {
                codeBlockString
                        .append("if(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()").append("!=null){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").gte(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            } else {
                codeBlockString
                        .append("if(")
                        .append(domainName.toLowerCase())
                        .append("DTO.get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()").append("!=null){\n")
                        .append("\tCriteria criteria =  Criteria.where(\"")
                        .append(column.getFieldName())
                        .append("\").is(")
                        .append(domainName.toLowerCase() + "DTO")
                        .append(".get")
                        .append(column.getFieldName().substring(0, 1).toUpperCase())
                        .append(column.getFieldName().substring(1))
                        .append("()")
                        .append("); \n").append("\tquery.addCriteria(criteria);\n}")
                        .append("\n");
            }
        }


        return MethodSpec
                .methodBuilder("getListByCondition")
                .addModifiers(modifiers)
                .addAnnotations(getPageByConditionMethodAnnotationSpecs)
                .addStatement(getPageByConditionCodeBlock)
                .addCode(String.valueOf(codeBlockString))
                .addStatement(
                        "return mongoTemplate" + ".find(query, " + domainName + ".class)"
                )
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_UpdateById(String entityName, ClassName modelClass, List<Modifier> modifiers, int idNum, ParameterSpec idParameterSpec) {
        List<ParameterSpec> updateMethodParameters = new ArrayList<>();

        // 要更新的新实体
        updateMethodParameters.add(
                ParameterSpec
                        .builder(modelClass, entityName.toLowerCase())
                        .addModifiers(Modifier.FINAL).build()
        );

        updateMethodParameters.add(
                idParameterSpec
        );
        List<AnnotationSpec> updateByIdMethodAnnotationSpecs = new ArrayList<>();
        updateByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        updateByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());

        return MethodSpec
                .methodBuilder(
                        "update"
                                + entityName + "ById"
                )
                .addAnnotations(updateByIdMethodAnnotationSpecs)
                .addModifiers(modifiers)
                .addStatement("$T " + entityName.toLowerCase() + "Db = " + entityName.toLowerCase()
                        + "Dao" + ".findById(id).orElse(new " + entityName + "())", modelClass)
                .addStatement("BeanUtils.copyProperties(" + entityName.toLowerCase() + "," + entityName.toLowerCase() + "Db)")

                .addStatement("return " + entityName.toLowerCase()
                        + "Dao.save(" + entityName.toLowerCase() + "Db)"
                )
                .addParameters(updateMethodParameters)
                .returns(modelClass)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetById(String entityName, ClassName modelClass, List<Modifier> modifiers, int idNum, ParameterSpec idParameterSpec) {
        List<ParameterSpec> getMethodParameters = new ArrayList<>();

        getMethodParameters
                .add(idParameterSpec);

        List<AnnotationSpec> getByIdMethodAnnotationSpecs = new ArrayList<>();
        getByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        return MethodSpec
                .methodBuilder(
                        "get" + entityName + "ById")
                .addModifiers(modifiers)
                .addAnnotations(getByIdMethodAnnotationSpecs)
                .addStatement(
                        "return " + entityName.toLowerCase() + "Dao"
                                + ".findById(id).orElse(new " + entityName + "())"
                )
                .addParameters(getMethodParameters)
                .returns(modelClass)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetAll(String entityName, ClassName modelClass, List<Modifier> modifiers) {
        ClassName list = ClassName.get("java.util", "List");

        // List<model>
        TypeName modelClasses = ParameterizedTypeName.get(list, modelClass);

        ClassName collectorsName = ClassName.get("java.util.stream", "Collectors");//java.util.stream.Collectors;

        List<AnnotationSpec> getALlMethodAnnotationSpecs = new ArrayList<>();
        getALlMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        return MethodSpec
                .methodBuilder("getAll" + entityName + "s")
                .addModifiers(modifiers)
                .addAnnotations(getALlMethodAnnotationSpecs)
                .addStatement(
                        "return "
                                + entityName.toLowerCase()
                                + "Dao.findAll()"
                )
                .returns(modelClasses)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_Save(String entityName, ClassName modelClass, List<Modifier> modifiers) {
        List<ParameterSpec> saveMethodParameters = new ArrayList<>();

        saveMethodParameters
                .add(
                        ParameterSpec
                                .builder(modelClass, entityName.toLowerCase())
                                .addModifiers(Modifier.FINAL)
                                .build()
                );
        List<AnnotationSpec> saveMethodAnnotationSpecs = new ArrayList<>();
        saveMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        saveMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());

        return MethodSpec
                .methodBuilder("save" + entityName)
                .addModifiers(modifiers)
                .addAnnotations(saveMethodAnnotationSpecs)
                .returns(modelClass)
                .addStatement(
                        "return " + entityName.toLowerCase() + "Dao"
                                + ".save("
                                + entityName.toLowerCase()
                                + ")"
                )
                .addParameters(saveMethodParameters)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_MapToEntity(String entityName, ClassName modelClass, ClassName DTOClass, List<Modifier> modifiers) {
        ClassName BeanUtilsName = ClassName.get("org.springframework.beans", "BeanUtils");
        TypeName dtoClassTypeName = ClassName.get("", entityName + "DTO").box();
        return MethodSpec
                .methodBuilder("mapToEntity")
                .addModifiers(modifiers)
                .addStatement(
                        "$T.copyProperties("
                                + entityName.toLowerCase() + "DTO" + ","
                                + entityName.toLowerCase() + ")",
                        BeanUtilsName
                )
                .addStatement("return " + entityName.toLowerCase())
                .returns(modelClass)
                .addParameter(
                        ParameterSpec
                                .builder(dtoClassTypeName, entityName.toLowerCase() + "DTO")
                                .addModifiers(Modifier.FINAL)
                                .build()
                )
                .addParameter(
                        ParameterSpec
                                .builder(modelClass, entityName.toLowerCase())
                                .addModifiers(Modifier.FINAL)
                                .build()
                )
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_IsExist(String entityName, List<Modifier> modifiers, int idNum, ParameterSpec idParameterSpec) {

        List<ParameterSpec> isExitMethodParameters = new ArrayList<>();
        isExitMethodParameters
                .add(
                        idParameterSpec
                );

        List<AnnotationSpec> isExistMethodAnnotationSpecs = new ArrayList<>();
        isExistMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        return MethodSpec
                .methodBuilder("is" + entityName + "Exist")
                .addModifiers(modifiers)
                .addAnnotations(isExistMethodAnnotationSpecs)
                .addStatement(
                        "return "
                                + entityName.toLowerCase()
                                + "Dao"
                                + ".existsById(id)"
                )
                .addParameters(isExitMethodParameters)
                .returns(Boolean.class)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_DeleteById(String entityName, List<Modifier> modifiers, int idNum, ParameterSpec idParameterSpec) {
        List<ParameterSpec> deleteMethodParameters = new ArrayList<>();

        deleteMethodParameters
                .add(
                        idParameterSpec
                );
        List<AnnotationSpec> deleteByIdMethodAnnotationSpecs = new ArrayList<>();
        deleteByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        deleteByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());

        return MethodSpec
                .methodBuilder(
                        "delete" + entityName + "ById"
                )
                .addModifiers(modifiers)
                .addAnnotations(deleteByIdMethodAnnotationSpecs)
                .addStatement(
                        entityName.toLowerCase()
                                + "Dao"
                                + ".deleteById(id)"
                )
                .addParameters(deleteMethodParameters)
                .returns(void.class)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_MapToDTO(String entityName, ClassName modelClass, ClassName DTOClass, List<Modifier> modifiers) {
        ClassName BeanUtilsName = ClassName.get("org.springframework.beans", "BeanUtils");
        TypeName dtoClassTypeName = ClassName.get("", entityName + "DTO").box();
        return MethodSpec
                .methodBuilder("mapToDTO")
                .addModifiers(modifiers)
                .addStatement(
                        "$T.copyProperties("
                                + entityName.toLowerCase() + ","
                                + entityName.toLowerCase() + "DTO)",
                        BeanUtilsName
                )
                .addStatement("return " + entityName.toLowerCase() + "DTO")
                .returns(dtoClassTypeName)
                .addParameter(
                        ParameterSpec
                                .builder(modelClass, entityName.toLowerCase())
                                .addModifiers(Modifier.FINAL)
                                .build()
                )
                .addParameter(
                        ParameterSpec.builder(dtoClassTypeName, entityName.toLowerCase() + "DTO")
                                .addModifiers(Modifier.FINAL)
                                .build()
                )
                .build();
    }

    // ------------------------------------------------------------------------------------------------------------------
    @Override
    public String generateController(Domain domain) {
        List<MethodSpec> methods = new ArrayList<>();
        String domainName = domain.getDomainName();
        String domainNameCN = domain.getDomainNameCN();
        String projectId = domain.getProjectId();

        Project projectById = projectService.getProjectById(projectId);
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();
        String packageName = packageGroup + "." + artifact;

        String apiTag = domain.getDomainNameCN() + " API"; // Controller 接口说明 API（tags） 注解用


        // 实体类的类名
        ClassName modelClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName);
        ClassName dTOClass = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "DTO");
        ClassName serviceInterface = ClassName.get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "Service");


        // 实体的属性列表
        List<DomainField> columns = domain.getDomainFieldList();

        DomainField columnDomainField = null;
        for (DomainField column : columns) {
            if (column.getIsKey()) {
                columnDomainField = column;
            }
        }

        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PUBLIC);

        int idNum = getIdNum(columns);

        ClassName pkClass = ClassName
                .get((packageName + ".domain." + domainName.toLowerCase()).trim(), domainName + "PK");

        TypeName pkTypeName = pkClass.box();

        // 两种主键类型
        ParameterSpec pkParameterSpec = ParameterSpec.builder(pkTypeName, "id")
                .addModifiers(Modifier.FINAL)
                .addAnnotation(
                        AnnotationSpec.builder(PathVariable.class)
                                .addMember("value",
                                        "\"id\"")
                                .build()
                )
                .build();

        ParameterSpec idParameterSpec = ParameterSpec.builder(
                this.getFieldType(columnDomainField.getDataType())
                , "id")
                .addAnnotation(
                        AnnotationSpec.builder(PathVariable.class)
                                .addMember("value",
                                        "\"id\"")
                                .build()
                )
                .addModifiers(Modifier.FINAL).build();

        MethodSpec saveMethodSpec = getMethodSpec_Controller_Save(domainName, domainNameCN, modelClass, modifiers);
        MethodSpec getMethodSpec = getMethodSpec_Controller_GetAll(domainName, domainNameCN, modelClass, modifiers);
        MethodSpec getByIdMethodSpec = getMethodSpec_Controller_GetById(domainName, domainNameCN, modelClass, columnDomainField, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec updateMethodSpec = getMethodSpec_UpdateById(domainName, domainNameCN, modelClass, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec deleteMethodSpec = getMethodSpec_Controller_DeleteById(domainName, domainNameCN, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec isExistMethodSpec = getMethodSpec_Controller_IsExist(domainName, domainNameCN, modifiers, idNum, pkParameterSpec, idParameterSpec);

        methods.add(saveMethodSpec);
        methods.add(getMethodSpec);
        methods.add(getByIdMethodSpec);
        methods.add(updateMethodSpec);
        methods.add(deleteMethodSpec);
        methods.add(isExistMethodSpec);

        //------------------ getConditionPage
        List<AnnotationSpec> getConditionPageMethodAnnotationSpecs = new ArrayList<>();
        getConditionPageMethodAnnotationSpecs
                .add(
                        AnnotationSpec.builder(GetMapping.class)
                                .addMember("value", "\"/" + "getPageList" + "\"")
                                .build()
                );

        getConditionPageMethodAnnotationSpecs.add(
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "\"" + "分页条件查询" + "\"")
                        .build()
        );

        List<ParameterSpec> getConditionPageMethodParameters = new ArrayList<>();

        getConditionPageMethodParameters
                .add(
                        ParameterSpec
                                .builder(dTOClass, domainName.toLowerCase() + "DTO")
                                .build()
                );


        List<AnnotationSpec> apiImplicitParamList = new ArrayList<>();

        ClassName ApiImplicitParamClass = ClassName.get("io.swagger.annotations", "ApiImplicitParam");

        AnnotationSpec apiImplicitParam1 = AnnotationSpec
                .builder(ApiImplicitParam.class)
                .addMember("name", "\"" + "page" + "\"")
                .addMember("value", "\"" + "页数" + "\"")
                .addMember("required", "false")
                .addMember("paramType", "\"" + "query" + "\"")
                .addMember("dataTypeClass", "Integer.class")
                .addMember("defaultValue", "\"" + "1" + "\"")
                .build();

        AnnotationSpec apiImplicitParam2 = AnnotationSpec
                .builder(ApiImplicitParam.class)
                .addMember("name", "\"" + "size" + "\"")
                .addMember("value", "\"" + "每页条数" + "\"")
                .addMember("required", "false")
                .addMember("paramType", "\"" + "query" + "\"")
                .addMember("dataTypeClass", "Integer.class")
                .addMember("defaultValue", "\"" + "20" + "\"")
                .build();
        apiImplicitParamList.add(apiImplicitParam1);
        apiImplicitParamList.add(apiImplicitParam2);

        AnnotationSpec ApiImplicitParamsAnoSpec = AnnotationSpec
                .builder(ApiImplicitParams.class)
                .addMember("value", apiImplicitParamList.toString()
                        .replace("[", "{")
                        .replace("]", "}")
                        .replace("io.swagger.annotations.", "")
                )
                .build();


//         @PageableDefault(value = 1,size = 20)
        AnnotationSpec pagebleAnoSpec = AnnotationSpec
                .builder(PageableDefault.class)
                .addMember("value", "1")
                .addMember("size", "20")
                .build();


        getConditionPageMethodParameters
                .add(
                        ParameterSpec
                                .builder(Pageable.class, "pageable")
                                .addAnnotation(pagebleAnoSpec)

                                .build()
                );

        ClassName listPage = ClassName.get("org.springframework.data.domain", "Page");
        TypeName PageClassesDTO = ParameterizedTypeName.get(listPage, modelClass);

        MethodSpec getConditionPageMethodSpec =
                MethodSpec
                        .methodBuilder(("getPageByCondition").trim())
                        .addAnnotations(getConditionPageMethodAnnotationSpecs)
                        .addAnnotation(ApiImplicitParamsAnoSpec)
                        .addModifiers(modifiers)
                        .addParameters(getConditionPageMethodParameters)
                        .returns(PageClassesDTO)
                        .addStatement(
                                ("return " + (domainName.toLowerCase()) + "Service" + "."
                                        + "getPageByCondition("
                                        + domainName.toLowerCase() + "DTO ,pageable)"
                                ).trim()
                        )
                        .build();

        methods.add(getConditionPageMethodSpec);


        // ----------------CONTROLLER CLASS CREATION----------------------

        List<AnnotationSpec> classAnnotations = new ArrayList<>();
        classAnnotations.add(AnnotationSpec.builder(RestController.class).build());
        classAnnotations.add(
                AnnotationSpec
                        .builder(Api.class)
                        .addMember("tags", "\"" + apiTag + "\"")
                        .build()
        );
        classAnnotations
                .add(
                        AnnotationSpec
                                .builder(RequestMapping.class)
                                .addMember("value", "\"/" + domainName.toLowerCase() + "s\"")
                                .build()
                );

        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
        fieldAnnotations.add(AnnotationSpec.builder(Autowired.class).build());

        List<FieldSpec> fields = new ArrayList<>();

        fields.add(
                FieldSpec
                        .builder(serviceInterface, ((domainName.toLowerCase()) + "Service").trim())
                        .addAnnotations(fieldAnnotations)
                        .build()
        );


        TypeSpec serviceImplTypeSpec =
                TypeSpec
                        .classBuilder(domainName + "Controller")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethods(methods)
                        .addAnnotations(classAnnotations)
                        .addFields(fields)
                        .build();

        JavaFile javaFile = JavaFile.builder((packageName + ".domain." + domainName.toLowerCase()).trim(), serviceImplTypeSpec).build();
        return javaFile.toString();
    }

    private MethodSpec getMethodSpec_Controller_GetById(String entityName, String comment, ClassName modelClass, DomainField columnEntityField, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<AnnotationSpec> getMethodAnnotationSpecs = new ArrayList<>();

        getMethodAnnotationSpecs
                .add(
                        AnnotationSpec.builder(GetMapping.class)
                                .addMember("value", "\"/" + "{id}" + "\"").build()
                );

        AnnotationSpec getbyidApiAnnotationSpec =
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "$S", "根据主键获取一条 " + comment)
                        .build();

        getMethodAnnotationSpecs.add(getbyidApiAnnotationSpec);

        List<ParameterSpec> getMethodParameters = new ArrayList<>();

        assert columnEntityField != null;

        getMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );

        MethodSpec getByIdMethodSpec =
                MethodSpec
                        .methodBuilder(("get" + entityName).trim())
                        .addAnnotations(getMethodAnnotationSpecs)
                        .addModifiers(modifiers)
                        .addParameters(getMethodParameters)
                        .addStatement(
                                ("return " + (entityName.toLowerCase())
                                        + "Service" + "." + "get" + entityName
                                        + "ById(id)").trim()
                        )
                        .returns(modelClass)
                        .build();
        return getByIdMethodSpec;
    }

    private MethodSpec getMethodSpec_UpdateById(String entityName, String comment, ClassName modelClass, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<AnnotationSpec> updateMethodAnnotationSpecs = new ArrayList<>();

        updateMethodAnnotationSpecs
                .add(
                        AnnotationSpec.builder(PutMapping.class)
                                .addMember("value", "\"/" + "{id}" + "\"")
                                .build()
                )
        ;

        AnnotationSpec updateByIdApiAnnotationSpec =
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "$S", "根据主键更新" + comment)
                        .build();

        updateMethodAnnotationSpecs.add(updateByIdApiAnnotationSpec);


        List<ParameterSpec> updateMethodParameters = new ArrayList<>();

        updateMethodParameters
                .add(
                        ParameterSpec
                                .builder(modelClass, entityName.toLowerCase())
                                .addAnnotation(
                                        AnnotationSpec.builder(RequestBody.class).build()
                                ).build()
                );


        updateMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );

        MethodSpec updateMethodSpec =
                MethodSpec
                        .methodBuilder(("update" + entityName).trim())
                        .addAnnotations(updateMethodAnnotationSpecs)
                        .addModifiers(modifiers)
                        .addParameters(updateMethodParameters)
                        .returns(modelClass)
                        .addStatement(
                                "return " + (entityName.toLowerCase())
                                        + "Service" + "." + "update" + entityName
                                        + "ById(" + entityName.toLowerCase() + ",id)"
                        )
                        .build();
        return updateMethodSpec;
    }

    private MethodSpec getMethodSpec_Controller_DeleteById(String entityName, String comment, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<AnnotationSpec> deleteMethodAnnotationSpecs = new ArrayList<>();
        deleteMethodAnnotationSpecs.add(
                AnnotationSpec
                        .builder(DeleteMapping.class)
                        .addMember("value", "\"/" + "{id}" + "\"")
                        .build()
        );

        AnnotationSpec deleteByIdApiAnnotationSpec =
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "$S", "根据主键 id 删除" + comment)
                        .build();

        deleteMethodAnnotationSpecs.add(deleteByIdApiAnnotationSpec);

        List<ParameterSpec> deleteMethodParameters = new ArrayList<>();

        deleteMethodParameters
                .add(
                        idNum > 1 ? pkParameterSpec : idParameterSpec
                );

        MethodSpec deleteMethodSpec =
                MethodSpec.methodBuilder(("delete" + entityName + "ById").trim())
                        .addAnnotations(deleteMethodAnnotationSpecs)
                        .addParameters(deleteMethodParameters)
                        .addModifiers(modifiers)
                        .returns(void.class)
                        .addStatement(
                                ((entityName.toLowerCase()) + "Service" + "."
                                        + "delete" + entityName + "ById(id)"
                                ).trim()
                        )
                        .build();
        return deleteMethodSpec;
    }

    private MethodSpec getMethodSpec_Controller_IsExist(String entityName, String comment, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<AnnotationSpec> isExistMethodAnnotationSpecs = new ArrayList<>();

        isExistMethodAnnotationSpecs
                .add(AnnotationSpec.builder(GetMapping.class)
                        .addMember("value", "\"/" + "isExist/{id}" + "\"")
                        .build());

        AnnotationSpec isExistApiAnnotationSpec =
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "$S", "根据主键 id 查看" + comment + "是否存在")
                        .build();

        isExistMethodAnnotationSpecs.add(isExistApiAnnotationSpec);

        List<ParameterSpec> isExistMethodParameters = new ArrayList<>();

        isExistMethodParameters
                .add(
                        idNum > 1 ? pkParameterSpec : idParameterSpec
                );

        MethodSpec isExistMethodSpec =
                MethodSpec
                        .methodBuilder(("isExist" + entityName).trim())
                        .addAnnotations(isExistMethodAnnotationSpecs)
                        .addModifiers(modifiers)
                        .addParameters(isExistMethodParameters)
                        .addStatement(
                                ("return " + (entityName.toLowerCase()) + "Service" + "."
                                        + "is" + entityName + "Exist(id)"
                                ).trim()
                        )
                        .returns(Boolean.class)
                        .build();
        return isExistMethodSpec;
    }

    private MethodSpec getMethodSpec_Controller_GetAll(String entityName, String comment, ClassName modelClass, List<Modifier> modifiers) {
        List<AnnotationSpec> getAllMethodAnnotationSpecs = new ArrayList<>();

        getAllMethodAnnotationSpecs
                .add(AnnotationSpec.builder(GetMapping.class)
//                        .addMember("value", "\"/" + "" + "\"")
                                .build()
                );

        AnnotationSpec getapiAnnotationSpec =
                AnnotationSpec
                        .builder(ApiOperation.class)
                        .addMember("value", "$S", "获取 " + comment + "列表")
                        .build();

        getAllMethodAnnotationSpecs.add(getapiAnnotationSpec);


        ClassName list = ClassName.get("java.util", "List");
        TypeName modelClasses = ParameterizedTypeName.get(list, modelClass);

        return MethodSpec
                .methodBuilder(("get" + entityName + "s").trim())
                .addAnnotations(getAllMethodAnnotationSpecs)
                .addModifiers(modifiers)
                .addStatement(
                        ("return " + (entityName.toLowerCase()) + "Service"
                                + ("." + "getAll" + entityName + "s" + "(")
                                + ")").trim()
                )
                .returns(modelClasses)
                .build();
    }

    private MethodSpec getMethodSpec_Controller_Save(String entityName, String comment, ClassName modelClass, List<Modifier> modifiers) {
        List<AnnotationSpec> saveMethodAnnotationSpecs = new ArrayList<>();

        saveMethodAnnotationSpecs
                .add(AnnotationSpec.builder(PostMapping.class)
//                        .addMember("value", "\"/" + "" + "\"")
                                .build()
                );

        AnnotationSpec saveapiAnnotationSpec =
                AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "添加" + comment)
                        .build();

        saveMethodAnnotationSpecs.add(saveapiAnnotationSpec);

        List<ParameterSpec> saveMethodParameters = new ArrayList<>();

        saveMethodParameters
                .add(
                        ParameterSpec
                                .builder(modelClass, entityName.toLowerCase())
                                .addAnnotation(
                                        AnnotationSpec.builder(RequestBody.class).build()
                                )
                                .build()
                );

        MethodSpec saveMethodSpec =
                MethodSpec
                        .methodBuilder(("save" + entityName).trim())
                        .addParameters(saveMethodParameters)
                        .addAnnotations(saveMethodAnnotationSpecs)
                        .addModifiers(modifiers)
                        .addStatement(entityName.toLowerCase() + ".setId($T.randomUUID().toString())", UUID.class)
                        .addStatement(
                                ("return " + (entityName.toLowerCase())
                                        + "Service" + "." + "save" + entityName
                                        + "(" + entityName.toLowerCase()
                                        + ")").trim()
                        )
                        .returns(modelClass)
                        .build();
        return saveMethodSpec;
    }

    // ------------------------------------------------------------------------------------------------------------------
    @Override
    public void generateJavaProject(String projectId, String basePath) {
        // basePath =  sysPath + projectId + "/"
        Project projectById = projectService.getProjectById(projectId);
        String[] split = projectById.getPackageGroup().split("\\.");
        String packageLocationName = split[0] + "/" + split[1] + "/" + projectById.getArtifact();
        String packageName = projectById.getPackageGroup() + "." + projectById.getArtifact();
        String projectName = projectById.getEnname();
        String uppercaseProjectName = "";
        String firstLtr = projectName.substring(0, 1);
        String restLtrs = projectName.substring(1, projectName.length());
        uppercaseProjectName = firstLtr.toUpperCase() + restLtrs;

        List<Domain> allDomains = domainService.getAllDomains(projectId);

        // xxx/projectId/projectName/src/main/java/packageLocationName/domain/xxx.java


        // 生成 Application 启动类
        String ApplicationBasePath = basePath + projectName.toLowerCase() + "/src/main/java/" + packageLocationName + "/";
        String ApplicationContent = this.generateApplication(packageName, uppercaseProjectName);
        String applicationFileName = uppercaseProjectName + "Application.java";
        String ApplicationLoacation = ApplicationBasePath + applicationFileName;
        FileWriter ApplicationWriter = new FileWriter(ApplicationLoacation);
        ApplicationWriter.write(ApplicationContent);

        // 生成 application.properties
        String propertiesBasePath = basePath + projectName.toLowerCase() + "/src/main/resources/";
        String propertiesContent = this.generateProperties(projectId);
        String propertiesFileName = "application.properties";
        String propertiesLoacation = propertiesBasePath + propertiesFileName;
        FileWriter propertiesWriter = new FileWriter(propertiesLoacation);
        propertiesWriter.write(propertiesContent);

        // 生成 ApplicationTests
        String ApplicationTestsBasePath = basePath + projectName.toLowerCase() + "/src/test/java/" + packageLocationName + "/";
        String ApplicationTestsContent = this.generateApplicationTests(packageName, uppercaseProjectName);
        String ApplicationTestsFileName = uppercaseProjectName + "ApplicationTests.java";
        String ApplicationTestsLoacation = ApplicationTestsBasePath + ApplicationTestsFileName;
        FileWriter ApplicationTestsWriter = new FileWriter(ApplicationTestsLoacation);
        ApplicationTestsWriter.write(ApplicationTestsContent);

        // 生成 pom.xml
        String pomBasePath = basePath + projectName.toLowerCase() + "/";
        String pomContent = this.generatePom(projectId, uppercaseProjectName);
        String pomFileName = "pom.xml";
        String pomLoacation = pomBasePath + pomFileName;
        FileWriter pomWriter = new FileWriter(pomLoacation);
        pomWriter.write(pomContent);

        // 生成 README.md
        String readmeBasePath = basePath + projectName.toLowerCase() + "/";
        String readmeContent = "Readme 文件内容";
        String readmeFileName = "README.md";
        String readmeLoacation = readmeBasePath + readmeFileName;
        FileWriter readmeWriter = new FileWriter(readmeLoacation);
        readmeWriter.write(readmeContent);

        // 生成 application/  presentation/ infrastructure
        String threeFilePath = basePath + projectName.toLowerCase() + "/src/main/java/" + packageLocationName + "/";
        String applicationInfoContent = "应用层：放置领域聚合根或跨包含多个领域服务的 Service 与 ServiceImpl。";
        String presentationInfoContent = "表示层：前端获取数据使用，放置跨多个领域服务获取数据的 Controller";
        String infrastructureInfoContent = "基础架构层：放置基础服务如各种配置、工具类、枚举、mybatis、elasticsearch 等中间件";
        String domainInfoContent = "领域层：放置各种领域对象，具备领域对象的基础 CRUD 功能";
        String threeFileName = "info.txt";
        String applicationFileLoacation = threeFilePath + "application/" + threeFileName;
        String presentationFileLoacation = threeFilePath + "presentation/" + threeFileName;
        String infrastructureFileLoacation = threeFilePath + "infrastructure/" + threeFileName;
        String domainInfoFileLoacation = threeFilePath + "domain/" + threeFileName;
        FileWriter applicationWriter = new FileWriter(applicationFileLoacation);
        FileWriter presentationWriter = new FileWriter(presentationFileLoacation);
        FileWriter infrastructureWriter = new FileWriter(infrastructureFileLoacation);
        FileWriter domainPathWriter = new FileWriter(domainInfoFileLoacation);
        applicationWriter.write(applicationInfoContent);
        presentationWriter.write(presentationInfoContent);
        infrastructureWriter.write(infrastructureInfoContent);
        domainPathWriter.write(domainInfoContent);


        String domainBasePath = basePath + projectName.toLowerCase() + "/src/main/java/" + packageLocationName + "/domain/";

        for (Domain domain : allDomains) {
            String domainName = domain.getDomainName();


            // Domain 生成
            String domainPath = domainBasePath + domainName.toLowerCase() + "/";
            String domainFileName = domainName + ".java";
            String domainLoacation = domainPath + domainFileName;
            String domainContent = this.generateDomain(domain);
            FileWriter domainWriter = new FileWriter(domainLoacation);
            domainWriter.write(domainContent);

            // DTO 生成
            String DTOPath = domainBasePath + domainName.toLowerCase() + "/";
            String DTOFileName = domainName + "DTO.java";
            String DTOLoacation = DTOPath + DTOFileName;
            String DTOContent = this.generateDTO(domain);
            FileWriter DTOWriter = new FileWriter(DTOLoacation);
            DTOWriter.write(DTOContent);

            // service 生成
            String servicePath = domainBasePath + domainName.toLowerCase() + "/";
            String serviceFileName = domainName + "Service.java";
            String serviceLoacation = servicePath + serviceFileName;
            String serviceContent = this.generateService(domain);
            FileWriter serviceWriter = new FileWriter(serviceLoacation);
            serviceWriter.write(serviceContent);

            // serviceImpl 生成
            String serviceImplPath = domainBasePath + domainName.toLowerCase() + "/";
            String serviceImplFileName = domainName + "ServiceImpl.java";
            String serviceImplLoacation = serviceImplPath + serviceImplFileName;
            String serviceImplContentOrigin = this.generateServiceImpl(domain);
            String serviceImplContent = this.getCompleteServiceImpl(serviceImplContentOrigin);
            FileWriter serviceImplWriter = new FileWriter(serviceImplLoacation);
            serviceImplWriter.write(serviceImplContent);

            // controller 生成
            String controllerPath = domainBasePath + domainName.toLowerCase() + "/";
            String controllerFileName = domainName + "Controller.java";
            String controllerLoacation = controllerPath + controllerFileName;
            String controllerContent = this.getCompleteController(this.generateController(domain));
            FileWriter controllerWriter = new FileWriter(controllerLoacation);
            controllerWriter.write(controllerContent);

            // repository 生成
            String repositoryPath = domainBasePath + domainName.toLowerCase() + "/";
            String repositoryFileName = domainName + "Dao.java";
            String repositoryLoacation = repositoryPath + repositoryFileName;
            String repositoryContent = this.generateRepository(domain);
            FileWriter repositoryWriter = new FileWriter(repositoryLoacation);
            repositoryWriter.write(repositoryContent);

        }


    }

    // --------------------------------------------------------------------------------------------

    private String generatePom(String projectId, String upperFirstprojectName) {
        Project projectById = projectService.getProjectById(projectId);
        String enname = projectById.getEnname();
        String cnname = projectById.getCnname();
        String javaVersion = projectById.getJavaVersion();
        String packageGroup = projectById.getPackageGroup();
        String artifact = projectById.getArtifact();


        String pomContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>" + packageGroup + "</groupId>\n" +
                "    <artifactId>" + artifact + "</artifactId>\n" +
                "    <version>0.0.1-SNAPSHOT</version>\n" +
                "    <name>" + enname + "</name>\n" +
                "    <description>" + cnname + "</description>\n" +
                "\n" +
                "    <properties>\n" +
                "        <java.version>" + javaVersion + "</java.version>\n" +
                "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
                "        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\n" +
                "        <spring-boot.version>2.4.1</spring-boot.version>\n" +
                "    </properties>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-data-mongodb</artifactId>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-web</artifactId>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>com.alibaba</groupId>\n" +
                "            <artifactId>fastjson</artifactId>\n" +
                "            <version>1.2.72</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>cn.hutool</groupId>\n" +
                "            <artifactId>hutool-all</artifactId>\n" +
                "            <version>5.7.22</version>\n" +
                "        </dependency>\n" +
                "\n" +
                "        <dependency>\n" +
                "            <groupId>org.projectlombok</groupId>\n" +
                "            <artifactId>lombok</artifactId>\n" +
                "            <optional>true</optional>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>com.github.xiaoymin</groupId>\n" +
                "            <artifactId>knife4j-spring-boot-starter</artifactId>\n" +
                "            <version>3.0.3</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <dependencyManagement>\n" +
                "        <dependencies>\n" +
                "            <dependency>\n" +
                "                <groupId>org.springframework.boot</groupId>\n" +
                "                <artifactId>spring-boot-dependencies</artifactId>\n" +
                "                <version>${spring-boot.version}</version>\n" +
                "                <type>pom</type>\n" +
                "                <scope>import</scope>\n" +
                "            </dependency>\n" +
                "        </dependencies>\n" +
                "    </dependencyManagement>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.apache.maven.plugins</groupId>\n" +
                "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                "                <version>3.8.1</version>\n" +
                "                <configuration>\n" +
                "                    <source>11</source>\n" +
                "                    <target>11</target>\n" +
                "                    <encoding>UTF-8</encoding>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>org.springframework.boot</groupId>\n" +
                "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                "                <version>2.4.1</version>\n" +
                "                <configuration>\n" +
                "                    <mainClass>" + packageGroup + "." + artifact + "." + upperFirstprojectName + "Application</mainClass>\n" +
                "                </configuration>\n" +
                "                <executions>\n" +
                "                    <execution>\n" +
                "                        <id>repackage</id>\n" +
                "                        <goals>\n" +
                "                            <goal>repackage</goal>\n" +
                "                        </goals>\n" +
                "                    </execution>\n" +
                "                </executions>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "\n" +
                "</project>\n";
        return pomContent;
    }

    @Override
    public String getCompleteServiceImpl(String file) {
        String[] split = file.split("\n");
        List<String> serviceImplNoDTO = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (split[i].contains("Page<T>")) {
                String[] splitLine = split[i].split("Page<T>");
                String whole = "  public <T> Page<T>" + splitLine[1];
                split[i] = whole;
            }
            serviceImplNoDTO.add(split[i]);
            if (i == 1) {
                serviceImplNoDTO.add("import org.apache.commons.lang3.StringUtils;");
                serviceImplNoDTO.add("import org.springframework.data.mongodb.core.query.Criteria;");
            }
        }
        String collect = serviceImplNoDTO.stream().collect(Collectors.joining("\n"));
        return collect;
    }

    @Override
    public String getCompleteController(String file) {
        String[] split = file.split("\n");
        List<String> controllerLines = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            controllerLines.add(split[i]);
            if (i == 1) {
                controllerLines.add("import io.swagger.annotations.ApiImplicitParam;");
            }
        }
        String collect = controllerLines.stream().collect(Collectors.joining("\n"));
        return collect;
    }

    @Override
    public String generateProperties(String projectId) {
        Project projectById = projectService.getProjectById(projectId);
        String enname = projectById.getEnname();
        String backEndPort = projectById.getBackEndPort();

        String propertiesContent = "# 应用名称\n" +
                "spring.application.name=" + enname + "\n" +
                "\n" +
                "# 应用服务 WEB 访问端口\n" +
                "server.port=" + backEndPort + "\n" +
                "spring.data.mongodb.uri=mongodb://USER:PASSWORD@ADDRESS:PORT\n" +
                "spring.data.mongodb.database=" + enname + "\n" +
                "\n";
        return propertiesContent;
    }

    @Override
    public String generateApplicationTests(String packageName, String upperFirstprojectName) {

        String ApplicationTestContent = "package " + packageName + ";\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import org.springframework.boot.test.context.SpringBootTest;\n" +
                "\n" +
                "@SpringBootTest\n" +
                "class " + upperFirstprojectName + "ApplicationTests {\n" +
                "    @Test\n" +
                "    void contextLoads() {\n" +
                "    }\n" +
                "}\n";
        return ApplicationTestContent;
    }

    @Override
    public String generateApplication(String packageName, String upperFirstprojectName) {

        return "package " + packageName + ";\n" +
                "\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                "\n" +
                "@SpringBootApplication\n" +
                "public class " + upperFirstprojectName + "Application {\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(" + upperFirstprojectName + "Application.class, args);\n" +
                "    }\n" +
                "\n" +
                "}";
    }

    // ------------------------------------------------------------------------------------------------------------------


    public <T> Class<T> getFieldType(String dataType) {
        Class<T> type = null;
        switch (dataType) {
            case "String":
                type = (Class<T>) String.class;
                break;
            case "text":
                type = (Class<T>) String.class;
                break;
            case "Text":
                type = (Class<T>) String.class;
                break;
            case "char":
                type = (Class<T>) char.class;
                break;
            case "Integer":
                type = (Class<T>) Integer.class;
                break;
            case "Double":
                type = (Class<T>) Double.class;
                break;
            case "Long":
                type = (Class<T>) Long.class;
                break;
            case "Byte":
                type = (Class<T>) Byte.class;
                break;
            case "byte[]":
                type = (Class<T>) byte[].class;
                break;
            case "Float":
                type = (Class<T>) Float.class;
                break;
            case "Date":
                type = (Class<T>) Date.class;
                break;
            case "Boolean":
                type = (Class<T>) Boolean.class;
                break;
            case "BigDecimal":
                type = (Class<T>) BigDecimal.class;
                break;
        }
        return type;
    }

}
