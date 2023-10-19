package cn.ideaswork.ideacoder.application.javacode;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainField;
import cn.ideaswork.ideacoder.domain.coder.domain.DomainService;
import cn.ideaswork.ideacoder.domain.coder.project.Project;
import cn.ideaswork.ideacoder.domain.coder.project.ProjectService;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JavaMybatisPlusServiceImpl implements JavaMybatisPlusCodeService {
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

        // 单主键生成
        for (DomainField column : domainFieldList) {
            if (column.getIsKey()) {
                FieldSpec idFieldSpec = getIdFieldSpecDomain(column);
                domainFieldSpecList.add(idFieldSpec);
            }
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

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + StrUtil.lowerFirst(domainName), classTypeSpec).build();
        return javaFile.toString();
    }

    private List<AnnotationSpec> getDomainTypeSpecAnnotationSpecs(String tableName, String tableNameCN) {
        List<AnnotationSpec> domainAnnotationSpecList = new ArrayList<>();


        domainAnnotationSpecList.add(AnnotationSpec.builder(Data.class).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(NoArgsConstructor.class).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(AllArgsConstructor.class).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(TableName.class).addMember("value", "$S", tableName).build());
        // 添加 @Schema
        domainAnnotationSpecList.add(AnnotationSpec.builder(Schema.class).addMember("description", "$S", tableNameCN).build());
        // 添加 @ApiModel TODO
//        domainAnnotationSpecList.add(AnnotationSpec.builder(ApiModel.class).addMember("value", "$S", domainNameCN).addMember("description", "$S", domainNameCN).build());
        domainAnnotationSpecList.add(AnnotationSpec.builder(Accessors.class).addMember("chain", "$L", true).build());
        return domainAnnotationSpecList;
    }

    private FieldSpec getIdFieldSpecDomain(DomainField column) {
        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
//				fieldAnnotations.add(AnnotationSpec.builder(GeneratedValue.class).addMember("strategy", "auto").build());

        // 添加 @Id
        AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(TableId.class);
        annotationSpec
                .addMember("value", "$S", column.getTableFieldName())
                .addMember("type", "$S", "IdType.AUTO");

        fieldAnnotations.add(annotationSpec.build());

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

        // 添加 @TableName

        if (!column.getFieldName().equals(column.getTableFieldName())) {
            AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(TableField.class);
            annotationSpec.addMember("value", "$S", column.getTableFieldName());
            fieldAnnotations.add(annotationSpec.build());
        }

        // 添加 @DateTimeFormat(pattern = Constants.PATTERN_DATE)
//			    @JsonFormat(pattern = Constants.PATTERN_DATE, timezone = "GMT+8")

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
        DTOClassAnnotations.add(AnnotationSpec.builder(NoArgsConstructor.class).build());

        // 实体的属性列表
        List<DomainField> columns = domain.getDomainFieldList();

        for (DomainField column : columns) {
            List<AnnotationSpec> fieldAnnotations = new ArrayList<>();


            // 添加 @DateTimeFormat(pattern = Constants.PATTERN_DATE)
            //	   @JsonFormat(pattern = Constants.PATTERN_DATE, timezone = "GMT+8")
            if (column.getDataType().equals("Date")) {

                fieldAnnotations.add(AnnotationSpec.builder(DateTimeFormat.class)
                        .addMember("pattern", "$S", "yyyy-MM-dd")
                        .build()
                );

                fieldAnnotations.add(AnnotationSpec.builder(JsonFormat.class)
                        .addMember("pattern", "$S", "yyyy-MM-dd")
                        .addMember("timezone", "$S", "GTM+8")
                        .build());

                FieldSpec DTOClassFieldSpecStart = FieldSpec
                        .builder(getFieldType(column.getDataType()), column.getFieldName() + "Start")
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(Schema.class)
                                .addMember("description", "$S", column.getFieldNameCN() + "Start")
                                .build()
                        )
                        .addAnnotations(fieldAnnotations).build();

                FieldSpec DTOClassFieldSpecEnd = FieldSpec
                        .builder(getFieldType(column.getDataType()), column.getFieldName() + "End")
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(AnnotationSpec.builder(Schema.class)
                                .addMember("description", "$S", column.getFieldNameCN() + "End")
                                .build()
                        )
                        .addAnnotations(fieldAnnotations).build();

                DTOFields.add(DTOClassFieldSpecStart);
                DTOFields.add(DTOClassFieldSpecEnd);

            } else {

                // 添加 @Schema
                fieldAnnotations.add(AnnotationSpec.builder(Schema.class)
                        .addMember("description", "$S", column.getFieldNameCN())
                        .build());

                // 添加 @ApiModel
//            fieldAnnotations.add(AnnotationSpec.builder(ApiModelProperty.class)
//                    .addMember("value", "$S", column.getFieldNameCN())
//                    .build());

                FieldSpec DTOClassFieldSpec = FieldSpec
                        .builder(getFieldType(column.getDataType()), column.getFieldName())
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotations(fieldAnnotations).build();

                DTOFields.add(DTOClassFieldSpec);
            }


        }

        TypeSpec DTOClassTypeSpec = TypeSpec.classBuilder(domainName + "DTO")
                .addFields(DTOFields)
                .addAnnotations(DTOClassAnnotations)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + StrUtil.lowerFirst(domainName), DTOClassTypeSpec).build();
        return javaFile.toString();
    }

    // ------------------------------------------------------------------------------------
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

        ClassName domainClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName);
        ClassName pkClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "PK");

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
        MethodSpec GetByIdMethodSpec = getMethodSpec_Service_GetById(domainName, domainClass, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec UpdateByIdMethodSpec = getMethodSpec_Service_UpdateById(domainName, domainClass, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec deleteByIdMethodSpec = getMethodSpec_Service_DeleteById(domainName, idNum, modifiers, pkParameterSpec, idParameterSpec);
        MethodSpec ConditionPageMethodSpec = getMethodSpec_Service_GetPageByCondition(domainName, packageName, domainClass, modifiers);
        MethodSpec ConditionListMethodSpec = getMethodSpec_Service_GetListByCondition(domainName, packageName, domainClass, modifiers);

        serviceClassMethods.add(saveMethodSpec);
        serviceClassMethods.add(GetByIdMethodSpec);
        serviceClassMethods.add(UpdateByIdMethodSpec);
        serviceClassMethods.add(deleteByIdMethodSpec);
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
                        .builder((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), serviceTypeSpec)
                        .build();

        return javaFile.toString();
    }

    private MethodSpec getMethodSpec_Service_Save(List<Modifier> modifiers, String domainName, ClassName className) {
        List<ParameterSpec> saveMethodParameters = new ArrayList<>();
        saveMethodParameters
                .add(
                        ParameterSpec
                                .builder(className, className.simpleName().toLowerCase())
                                .build()
                );

        return MethodSpec
                .methodBuilder("save" + domainName)
                .addModifiers(modifiers)
                .addParameters(saveMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetById(String domainName, ClassName className, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> getMethodParameters = new ArrayList<>();

        getMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );

        return MethodSpec
                .methodBuilder(
                        "get" + domainName + "ById"
                )
                .addModifiers(modifiers)
                .addParameters(getMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_UpdateById(String domainName, ClassName className, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
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
                .methodBuilder("update" + domainName + "ById")
                .addModifiers(modifiers)
                .addParameters(updateMethodParameters)
                .returns(className)
                .build();
    }

    private MethodSpec getMethodSpec_Service_DeleteById(String domainName, int idNum, List<Modifier> modifiers, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> deleteMethodParameters = new ArrayList<>();

        deleteMethodParameters
                .add(idNum > 1 ? pkParameterSpec : idParameterSpec);

        return MethodSpec
                .methodBuilder("delete" + domainName + "ById")
                .addModifiers(modifiers)
                .addParameters(deleteMethodParameters)
                .returns(int.class)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetPageByCondition(String domainName, String packageName, ClassName className, List<Modifier> modifiers) {
        ClassName listPage = ClassName.get("com.baomidou.mybatisplus.plugins", "Page");
        ClassName condition1 = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "DTO");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listPage, className);

        getPageMethodParameters.add(ParameterSpec.builder(condition1, (domainName).toLowerCase() + "DTO").build());
        getPageMethodParameters.add(ParameterSpec.builder(Integer.class, "pageNum").build());
        getPageMethodParameters.add(ParameterSpec.builder(Integer.class, "pageSize").build());

        return MethodSpec
                .methodBuilder("getPageByCondition")
                .addModifiers(modifiers)
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_Service_GetListByCondition(String domainName, String packageName, ClassName className, List<Modifier> modifiers) {
        ClassName listClass = ClassName.get("java.util", "List");
//        ClassName condition = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName);
        ClassName condition1 = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "DTO");
        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listClass, className);

        getPageMethodParameters
                .add(ParameterSpec.builder(condition1, (domainName).toLowerCase() + "DTO").build());

        return MethodSpec
                .methodBuilder("getListByCondition")
                .addModifiers(modifiers)
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

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

        // 计算属性列表中主键的数量
        int idNum = getIdNum(columns);

        repositoryClassAnnotations
                .add(AnnotationSpec.builder(Repository.class).build());

        ClassName baseMapperClassName = ClassName.get("com.baomidou.mybatisplus.mapper", "BaseMapper");

        ParameterizedTypeName baseMapperSuperinterface = ParameterizedTypeName.get(
                baseMapperClassName,
                ClassName.get((packageName + ".domain." + domainName).trim(), domainName)
        );

        TypeSpec repositorySpec =
                TypeSpec
                        .interfaceBuilder(domainName + "Mapper")
                        .addModifiers(Modifier.PUBLIC)
//                        .addAnnotations(repositoryClassAnnotations)
                        .addSuperinterface(baseMapperSuperinterface)
                        .build();

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + domainName, repositorySpec).build();
        return javaFile.toString();

    }

    private int getIdNum(List<DomainField> columns) {
        List<DomainField> idColumns = columns.stream().filter(DomainField::getIsKey).collect(Collectors.toList());
        return idColumns.size();
    }
    // ------------------------------------------------------------------------------------------------------

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
        ClassName modelClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName);
        ClassName DTOClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "DTO");
        ClassName serviceInterface = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "Service");
        ClassName repositoryClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "Mapper");
        ClassName condition = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "DTO");
        ClassName pkClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "PK");

        // 实体的属性列表
        List<DomainField> columns = domain.getDomainFieldList();

        DomainField columnDomainField = null;
        for (DomainField column : columns) {
            if (column.getIsKey()) {
                columnDomainField = column;
            }
        }
        int idNum = getIdNum(columns);


        TypeName pkTypeName = pkClass.box();

        // 两种主键类型
        ParameterSpec pkParameterSpec = ParameterSpec.builder(pkTypeName, "id").addModifiers(Modifier.FINAL).build();
        ParameterSpec idParameterSpec = ParameterSpec.builder(this.getFieldType(columnDomainField.getDataType()), "id").addModifiers(Modifier.FINAL).build();

        // service 方法的修饰符
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(Modifier.PUBLIC);

        MethodSpec saveMethodSpec = getMethodSpec_ServiceImpl_Save(domainName, modelClass, modifiers);
        MethodSpec GetByIdMethodSpec = getMethodSpec_ServiceImpl_GetById(domainName, modelClass, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec UpdateByIdMethodSpec = getMethodSpec_ServiceImpl_UpdateById(domainName, modelClass, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec deleteByIdMethodSpec = getMethodSpec_ServiceImpl_DeleteById(domainName, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec ConditionListMethodSpec = getMethodSpec_ServiceImpl_GetListByCondition(domainName, modelClass, modifiers, condition);
        MethodSpec ConditionPageMethodSpec = getMethodSpec_ServiceImpl_GetPageByCondition(domainName, modelClass, modifiers, condition);
        MethodSpec getQueryWrapperMethodSpec = getMethodSpec_ServiceImpl_GetQueryWrapper(domainName, modelClass, columns, columnDomainField, modifiers, condition);

        methods.add(saveMethodSpec);
        methods.add(GetByIdMethodSpec);
        methods.add(UpdateByIdMethodSpec);
        methods.add(deleteByIdMethodSpec);
        methods.add(ConditionPageMethodSpec);
        methods.add(ConditionListMethodSpec);
        methods.add(getQueryWrapperMethodSpec);

        // 生成文件并返回
        List<FieldSpec> fields = new ArrayList<>();
        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();

        fieldAnnotations.add(AnnotationSpec.builder(Resource.class).build());

        fields.add(
                FieldSpec
                        .builder(repositoryClass, (StrUtil.lowerFirst(domainName) + "Mapper").trim())
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

        JavaFile javaFile = JavaFile.builder(packageName + ".domain." + StrUtil.lowerFirst(domainName), serviceImplTypeSpec).build();

        String fileString = javaFile.toString();

        return fileString;
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetPageByCondition(String domainName, ClassName modelClass, List<Modifier> modifiers, ClassName condition) {
        ClassName listPage = ClassName.get("com.baomidou.mybatisplus.plugins", "Page");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(listPage, modelClass);
        getPageMethodParameters.add(
                ParameterSpec
                        .builder(condition, (StrUtil.lowerFirst(domainName) + "DTO"))
                        .addModifiers(Modifier.FINAL)
                        .build()
        );
        getPageMethodParameters.add(ParameterSpec.builder(Integer.class, "pageNum").build());
        getPageMethodParameters.add(ParameterSpec.builder(Integer.class, "pageSize").build());

        List<AnnotationSpec> getPageByConditionMethodAnnotationSpecs = new ArrayList<>();
        getPageByConditionMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());


        return MethodSpec
                .methodBuilder("getPageByCondition")
                .addModifiers(modifiers)
                .addAnnotations(getPageByConditionMethodAnnotationSpecs)
                .addStatement("$T<$T> queryWrapper = new $T<>()", Wrapper.class, modelClass, EntityWrapper.class)
                .addStatement("$T<$T> page = new $T<>(pageNum,pageSize)", listPage, modelClass, listPage)
                .addCode("if (" + StrUtil.lowerFirst(domainName) + "DTO" + " == null){" +
                        "return page.setRecords(" + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectPage(page,queryWrapper));" +
                        "}\n")
                .addStatement(
                        "return page.setRecords(" + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectPage(page,this.getQueryWrapper(queryWrapper , " + StrUtil.lowerFirst(domainName) + "DTO" + ")))"
                )
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetListByCondition(String domainName, ClassName modelClass, List<Modifier> modifiers, ClassName condition) {
        ClassName list = ClassName.get("java.util", "List");
        ClassName listPage = ClassName.get("com.baomidou.mybatisplus.plugins", "Page");

        List<ParameterSpec> getPageMethodParameters = new ArrayList<>();

        TypeName PageClasses = ParameterizedTypeName.get(list, modelClass);
        getPageMethodParameters.add(
                ParameterSpec
                        .builder(condition, (StrUtil.lowerFirst(domainName) + "DTO"))
                        .addModifiers(Modifier.FINAL)
                        .build()
        );

        List<AnnotationSpec> getPageByConditionMethodAnnotationSpecs = new ArrayList<>();
        getPageByConditionMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        return MethodSpec
                .methodBuilder("getListByCondition")
                .addModifiers(modifiers)
                .addAnnotations(getPageByConditionMethodAnnotationSpecs)
                .addStatement("$T<$T> queryWrapper = new $T<>()", Wrapper.class, modelClass, EntityWrapper.class)
                .addStatement(
                        "return " + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectList(this.getQueryWrapper(queryWrapper ," + StrUtil.lowerFirst(domainName) + "DTO))"
                )
                .addParameters(getPageMethodParameters)
                .returns(PageClasses)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_UpdateById(String domainName, ClassName modelClass, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> updateMethodParameters = new ArrayList<>();

        // 要更新的新实体
        updateMethodParameters.add(
                ParameterSpec
                        .builder(modelClass, StrUtil.lowerFirst(domainName))
                        .addModifiers(Modifier.FINAL).build()
        );

        updateMethodParameters.add(
                idNum > 1 ? pkParameterSpec : idParameterSpec
        );
        List<AnnotationSpec> updateByIdMethodAnnotationSpecs = new ArrayList<>();
        updateByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        updateByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());

        return MethodSpec
                .methodBuilder(
                        "update"
                                + domainName + "ById"
                )
                .addAnnotations(updateByIdMethodAnnotationSpecs)
                .addModifiers(modifiers)
//                .addStatement("$T " + StrUtil.lowerFirst(domainName) + "Db = baseMapper" + ".selectById(id)", modelClass)
//                .addStatement("BeanUtils.copyProperties(" + StrUtil.lowerFirst(domainName) + "," + StrUtil.lowerFirst(domainName) + "Db)")
                .addStatement(StrUtil.lowerFirst(domainName) + "Mapper" + ".updateById(" + StrUtil.lowerFirst(domainName) + ")")
                .addStatement("return " + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectById(id)")
                .addParameters(updateMethodParameters)
                .returns(modelClass)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetById(String domainName, ClassName modelClass, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> getMethodParameters = new ArrayList<>();

        getMethodParameters
                .add(
                        idNum > 1 ? pkParameterSpec : idParameterSpec
                );

        List<AnnotationSpec> getByIdMethodAnnotationSpecs = new ArrayList<>();
        getByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());

        return MethodSpec
                .methodBuilder(
                        "get" + domainName + "ById")
                .addModifiers(modifiers)
                .addAnnotations(getByIdMethodAnnotationSpecs)
                .addStatement("return " + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectById(id)")
                .addParameters(getMethodParameters)
                .returns(modelClass)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_Save(String domainName, ClassName modelClass, List<Modifier> modifiers) {
        List<ParameterSpec> saveMethodParameters = new ArrayList<>();

        saveMethodParameters
                .add(
                        ParameterSpec
                                .builder(modelClass, StrUtil.lowerFirst(domainName))
                                .addModifiers(Modifier.FINAL)
                                .build()
                );
        List<AnnotationSpec> saveMethodAnnotationSpecs = new ArrayList<>();
        saveMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        saveMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());


        return MethodSpec
                .methodBuilder("save" + domainName)
                .addModifiers(modifiers)
                .addAnnotations(saveMethodAnnotationSpecs)
                .returns(modelClass)
                .addStatement(StrUtil.lowerFirst(domainName) + "Mapper" + ".insert(" + StrUtil.lowerFirst(domainName) + ")")
                .addStatement("return " + StrUtil.lowerFirst(domainName) + "Mapper" + ".selectById(" + StrUtil.lowerFirst(domainName) + ".getId())")
                .addParameters(saveMethodParameters)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_DeleteById(String domainName, List<Modifier> modifiers, int idNum, ParameterSpec pkParameterSpec, ParameterSpec idParameterSpec) {
        List<ParameterSpec> deleteMethodParameters = new ArrayList<>();

        deleteMethodParameters
                .add(
                        idNum > 1 ? pkParameterSpec : idParameterSpec
                );
        List<AnnotationSpec> deleteByIdMethodAnnotationSpecs = new ArrayList<>();
        deleteByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Override.class).build());
        deleteByIdMethodAnnotationSpecs.add(AnnotationSpec.builder(Transactional.class).build());

        return MethodSpec
                .methodBuilder(
                        "delete" + domainName + "ById"
                )
                .addModifiers(modifiers)
                .addAnnotations(deleteByIdMethodAnnotationSpecs)
                .addStatement(" return " + StrUtil.lowerFirst(domainName) + "Mapper"
                        + ".deleteById(id)"
                )
                .addParameters(deleteMethodParameters)
                .returns(int.class)
                .build();
    }

    private MethodSpec getMethodSpec_ServiceImpl_GetQueryWrapper(String domainName, ClassName modelClass, List<DomainField> columns, DomainField columnDomainField, List<Modifier> modifiers, ClassName condition) {
        ClassName queryWrapper = ClassName.get("com.baomidou.mybatisplus.mapper", "Wrapper");
//        ClassName predicateName = ClassName.get("javax.persistence.criteria", "Predicate");
//        ClassName javaListName = ClassName.get("java.util", "List");
//        ClassName arryListName = ClassName.get("java.util", "ArrayList");

        TypeName queryWrapperName = ParameterizedTypeName.get(queryWrapper, modelClass);

        int idNum = getIdNum(columns);
        StringBuilder codeBlockString = new StringBuilder();
        for (DomainField column : columns) {
            String typeString = column.getDataType();
            if (!column.getIsKey()) {
                if (typeString.equals("String")) {
                    codeBlockString
                            .append("if(!StringUtils.isEmpty(")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("()")
                            .append(")){\n")
                            .append("\tqueryWrapper.like(\"")
                            .append(column.getTableFieldName())
                            .append("\",")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("()")
                            .append("); \n").append("}\n");
                } else if (typeString.equals("Date")) {
                    codeBlockString
                            .append("if(")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("Start()").append("!=null){\n")
                            .append("\tqueryWrapper.ge(\"")
                            .append(column.getTableFieldName())
                            .append("\",")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("Start()")
                            .append("); \n")
                            .append("}\n");

                    codeBlockString
                            .append("if(")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("End()").append("!=null){\n")
                            .append("\tqueryWrapper.le(\"")
                            .append(column.getTableFieldName())
                            .append("\",")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("End()")
                            .append("); \n")
                            .append("}\n");
                } else {
                    codeBlockString
                            .append("if(")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("()").append("!=null){\n")
                            .append("\tqueryWrapper.eq(\"")
                            .append(column.getTableFieldName())
                            .append("\",")
                            .append(StrUtil.lowerFirst(domainName))
                            .append("DTO.get")
                            .append(StrUtil.upperFirst(column.getFieldName()))
                            .append("()")
                            .append("); \n")
                            .append("}\n");
                }
            } else {
                codeBlockString
                        .append("if(")
                        .append(StrUtil.lowerFirst(domainName))
                        .append("DTO.get")
                        .append(StrUtil.upperFirst(column.getFieldName()))
                        .append("()").append("!=null){\n")
                        .append("\tqueryWrapper.eq(\"")
                        .append(column.getTableFieldName())
                        .append("\",")
                        .append(StrUtil.lowerFirst(domainName))
                        .append("DTO.get")
                        .append(StrUtil.upperFirst(column.getFieldName()))
                        .append("()")
                        .append("); \n")
                        .append("}\n");
            }

        }

        ClassName stringUtilsName = ClassName.get("org.apache.commons.lang3", "StringUtils");

        StringBuilder idcodeBlockString = new StringBuilder();
        StringBuilder singleIdcodeBlockString = new StringBuilder();

        singleIdcodeBlockString
                .append("if(!StringUtils.isEmpty(")
                .append(StrUtil.lowerFirst(domainName))
                .append("DTO.get").append(columnDomainField.getFieldName().substring(0, 1).toUpperCase())
                .append(columnDomainField.getFieldName().substring(1))
                .append("())){ \n")
                .append("\tqueryWrapper.eq(\"")
                .append(columnDomainField.getTableFieldName()).append("\",")
                .append(StrUtil.lowerFirst(domainName))
                .append("DTO.get")
                .append(columnDomainField.getFieldName().substring(0, 1).toUpperCase())
                .append(columnDomainField.getFieldName().substring(1)).append("()").append("); \n")
                .append("}\n");

        idcodeBlockString
//                .append(singleIdcodeBlockString)
                .append(codeBlockString)
                .append("return queryWrapper;\n");

        return MethodSpec
                .methodBuilder("getQueryWrapper")
                .addModifiers(modifiers)
                .addCode(String.valueOf(idcodeBlockString))
                .addParameter(ParameterSpec.builder(queryWrapperName, "queryWrapper").build())
                .addParameter(ParameterSpec.builder(condition, (StrUtil.lowerFirst(domainName) + "DTO")).addModifiers(Modifier.FINAL).build())
                .returns(queryWrapperName)
                .build();
    }

    // -----------------------------------------------------------------------------------------------------------------
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
        ClassName modelClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName);
        ClassName dTOClass = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "DTO");
        ClassName serviceInterface = ClassName.get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "Service");


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
                .get((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), domainName + "PK");

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
        MethodSpec getByIdMethodSpec = getMethodSpec_Controller_GetById(domainName, domainNameCN, modelClass, columnDomainField, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec updateMethodSpec = getMethodSpec_UpdateById(domainName, domainNameCN, modelClass, modifiers, idNum, pkParameterSpec, idParameterSpec);
        MethodSpec deleteMethodSpec = getMethodSpec_Controller_DeleteById(domainName, domainNameCN, modifiers, idNum, pkParameterSpec, idParameterSpec);

        methods.add(saveMethodSpec);
        methods.add(getByIdMethodSpec);
        methods.add(updateMethodSpec);
        methods.add(deleteMethodSpec);

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
                                .builder(dTOClass, StrUtil.lowerFirst(domainName) + "DTO")
                                .build()
                );


        List<AnnotationSpec> apiImplicitParamList = new ArrayList<>();

        ClassName ApiImplicitParamClass = ClassName.get("io.swagger.annotations", "ApiImplicitParam");
        AnnotationSpec apiImplicitParam1 = AnnotationSpec
                .builder(ApiImplicitParamClass)
                .addMember("name", "\"" + "pageNum" + "\"")
                .addMember("value", "\"" + "页数" + "\"")
                .addMember("required", "false")
                .addMember("paramType", "\"" + "query" + "\"")
                .addMember("dataTypeClass", "Integer.class")
                .addMember("defaultValue", "\"" + "1" + "\"")
                .build();

        AnnotationSpec apiImplicitParam2 = AnnotationSpec
                .builder(ApiImplicitParam.class)
                .addMember("name", "\"" + "pageSize" + "\"")
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

        AnnotationSpec requestParamsPageNum = AnnotationSpec
                .builder(RequestParam.class)
                .addMember("value", "\"pageNum\"")
                .addMember("required", "true")
                .addMember("defaultValue", "\"1\"")
                .build();

        AnnotationSpec requestParamsPageSize = AnnotationSpec
                .builder(RequestParam.class)
                .addMember("value", "\"pageSize\"")
                .addMember("required", "true")
                .addMember("defaultValue", "\"10\"")
                .build();

        getConditionPageMethodParameters.add(ParameterSpec
                .builder(Integer.class, "pageNum")
                .addAnnotation(requestParamsPageNum)
                .build()
        );

        getConditionPageMethodParameters.add(
                ParameterSpec.builder(Integer.class, "pageSize")
                        .addAnnotation(requestParamsPageSize)
                        .build()
        );

        ClassName listPage = ClassName.get("com.baomidou.mybatisplus.plugins", "Page");
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
                                ("return " + (StrUtil.lowerFirst(domainName)) + "Service" + "."
                                        + "getPageByCondition("
                                        + StrUtil.lowerFirst(domainName) + "DTO , pageNum, pageSize)"
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
                                .addMember("value", "\"/" + StrUtil.lowerFirst(domainName) + "s\"")
                                .build()
                );

        List<AnnotationSpec> fieldAnnotations = new ArrayList<>();
        fieldAnnotations.add(AnnotationSpec.builder(Autowired.class).build());

        List<FieldSpec> fields = new ArrayList<>();

        fields.add(
                FieldSpec
                        .builder(serviceInterface, ((StrUtil.lowerFirst(domainName)) + "Service").trim())
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

        JavaFile javaFile = JavaFile.builder((packageName + ".domain." + StrUtil.lowerFirst(domainName)).trim(), serviceImplTypeSpec).build();
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
                                ("return " + (StrUtil.lowerFirst(entityName))
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
                                .builder(modelClass, StrUtil.lowerFirst(entityName))
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
                                "return " + (StrUtil.lowerFirst(entityName))
                                        + "Service" + "." + "update" + entityName
                                        + "ById(" + StrUtil.lowerFirst(entityName) + ",id)"
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
                        .returns(int.class)
                        .addStatement(
                                ("return " + (StrUtil.lowerFirst(entityName)) + "Service" + "."
                                        + "delete" + entityName + "ById(id)"
                                ).trim()
                        )
                        .build();
        return deleteMethodSpec;
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
                                .builder(modelClass, StrUtil.lowerFirst(entityName))
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
//                        .addStatement(StrUtil.lowerFirst(entityName) + ".setId($T.randomUUID().toString())", UUID.class)
                        .addStatement(
                                ("return " + (StrUtil.lowerFirst(entityName))
                                        + "Service" + "." + "save" + entityName
                                        + "(" + StrUtil.lowerFirst(entityName)
                                        + ")").trim()
                        )
                        .returns(modelClass)
                        .build();
        return saveMethodSpec;
    }

    public <T> Class<T> getFieldType(String dataType) {
        Class<T> type = null;
        switch (dataType) {
            case "String":
                type = (Class<T>) String.class;
                break;
            case "text":
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

    @Override
    public String generateMapperXml(Domain domain) {
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
}
