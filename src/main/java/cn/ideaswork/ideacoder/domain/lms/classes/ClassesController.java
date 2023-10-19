package cn.ideaswork.ideacoder.domain.lms.classes;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Api(tags = "班级 API")
@RequestMapping("/classess")
public class ClassesController {
    @Autowired
    ClassesService classesService;

    @PostMapping
    @ApiOperation("添加班级")
    public Classes saveClasses(@RequestBody Classes classes) {
        classes.setId(UUID.randomUUID().toString());
        return classesService.saveClasses(classes);
    }

    @GetMapping
    @ApiOperation("获取 班级列表")
    public List<Classes> getClassess() {
        return classesService.getAllClassess();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 班级")
    public Classes getClasses(@PathVariable("id") final String id) {
        return classesService.getClassesById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新班级")
    public Classes updateClasses(@RequestBody Classes classes, @PathVariable("id") final String id) {
        return classesService.updateClassesById(classes, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除班级")
    public void deleteClassesById(@PathVariable("id") final String id) {
        classesService.deleteClassesById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看班级是否存在")
    public Boolean isExistClasses(@PathVariable("id") final String id) {
        return classesService.isClassesExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Classes> getPageByCondition(ClassesDTO classesDTO,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return classesService.getPageByCondition(classesDTO, pageable);
    }
}