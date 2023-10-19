package cn.ideaswork.ideacoder;

import cn.ideaswork.ideacoder.domain.coder.project.Project;
import com.alibaba.fastjson.JSON;
import com.mongodb.client.model.geojson.Point;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class IdeacoderApplicationTests {
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void contextLoads() {
        testmodel testmodel = new testmodel();
        testmodel.setAge(2);
        testmodel.setName("william");
        testmodelNested testmodelNested = new testmodelNested();
        testmodelNested.setAge1(11);
        testmodelNested.setName1("name1xxx");
        List<testmodelNested> lists = new ArrayList<>();
        lists.add(testmodelNested);
        testmodel.setTestmodelNested(testmodelNested);
        testmodel.setTestmodelNesteds(lists);
        IdeacoderApplicationTests.testmodel test = mongoTemplate.insert(testmodel, "dddcoder");
        System.out.println(JSON.toJSONString(test.getTestmodelNesteds()));
//        System.out.println(JSON.toJSONString(test));
    }

    @Data
    @ToString
    class testmodel {
        Integer age;
        String name;
        testmodelNested testmodelNested;
        List<testmodelNested> testmodelNesteds;
    }

    @Data
    @ToString
    class testmodelNested {
        Integer age1;
        String name1;
    }

    @Test
    void testMongo() {
//        testmodel testmodel = new testmodel();
//        testmodel.setAge(2);
//        testmodel.setName("william");
//        IdeacoderApplicationTests.testmodel test = mongoTemplate.insert(testmodel, "dddcoder");
//        System.out.println(test);
        Project project = new Project();
        project.setEnname("");

        Query query = new Query();
        if (StringUtils.isNotBlank(project.getEnname())) {
            query.addCriteria(Criteria.where("enname").is(project.getEnname()));
        }
        if (StringUtils.isNotBlank(project.getStatus())) {
            query.addCriteria(Criteria.where("status").is(project.getStatus()));
        }
//        Criteria.where("status").
        Pageable pageable = PageRequest.of(0, 10);


        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);

        Page<Project> projectPage = new PageImpl<Project>(projects, pageable, projects.size());
        System.out.println(JSON.toJSONString(projectPage));
        System.out.println(projectPage.getTotalPages());
        System.out.println(projectPage.getContent().size());
        System.out.println(projects.size());
        projects.forEach(one -> System.out.println(one));
    }

    @Test
    void testMongoExampleMacher() {
        Project project = new Project();
        project.setEnname("");

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
                .withIgnorePaths("pageNum", "pageSize"); //忽略属性，不参与查询

        //创建实例
        Example<Project> example = Example.of(project, matcher);
        Pageable pageable = PageRequest.of(0, 10);

    }

    @Test
    void testMongoQueryBuilders() {
        Project project = new Project();
        project.setEnname("");

        Query query = new Query();
        if (StringUtils.isNotBlank(project.getEnname())) {
            query.addCriteria(Criteria.where("enname").is(project.getEnname()));
        }
        if (StringUtils.isNotBlank(project.getStatus())) {
            query.addCriteria(Criteria.where("status").is(project.getStatus()));
        }
//        Criteria.where("status").
        Pageable pageable = PageRequest.of(0, 10);


        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);

        Page<Project> projectPage = new PageImpl<Project>(projects, pageable, projects.size());
        System.out.println(JSON.toJSONString(projectPage));
        System.out.println(projectPage.getTotalPages());
        System.out.println(projectPage.getContent().size());
        System.out.println(projects.size());
        projects.forEach(one -> System.out.println(one));
    }

}
