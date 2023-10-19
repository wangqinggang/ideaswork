package cn.ideaswork.ideacoder.domain.lms.calender;

import cn.ideaswork.ideacoder.domain.lms.classes.Classes;
import cn.ideaswork.ideacoder.domain.lms.classes.ClassesDTO;
import cn.ideaswork.ideacoder.domain.lms.classes.ClassesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Api(tags = "日历 API")
@RequestMapping("/calender")
public class CalenderController {


    @GetMapping
    @ApiOperation("获取 42 宫格月份")
    public List<String> get42Day() {


        return null;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 班级")
    public Classes getClasses(@PathVariable("id") final String id) {
        return null;
    }

}