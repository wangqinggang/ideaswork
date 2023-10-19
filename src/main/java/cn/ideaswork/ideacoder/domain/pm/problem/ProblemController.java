package cn.ideaswork.ideacoder.domain.pm.problem;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(
        tags = "问题 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/problems")
@CrossOrigin()
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @PostMapping
    @ApiOperation("添加问题")
    public Problem saveProblem(@RequestBody Problem problem) {
        problem.setId(UUID.randomUUID().toString());
        problem.setCreatetime(new Date());
        return problemService.saveProblem(problem);
    }

    @GetMapping
    @ApiOperation("获取 问题列表")
    public List<Problem> getProblems() {
        return problemService.getAllProblems();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 问题")
    public Problem getProblem(@PathVariable("id") final String id) {
        return problemService.getProblemById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新问题")
    public Problem updateProblem(@RequestBody Problem problem, @PathVariable("id") final String id) {
        return problemService.updateProblemById(problem, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除问题")
    public void deleteProblemById(@PathVariable("id") final String id) {
        problemService.deleteProblemById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看问题是否存在")
    public Boolean isExistProblem(@PathVariable("id") final String id) {
        return problemService.isProblemExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Problem> getPageByCondition(ProblemDTO problemDTO,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return problemService.getPageByCondition(problemDTO, pageable);
    }

    @GetMapping("/getList")
    @ApiOperation("分页条件查询")
    public List<Problem> getListByCondition(ProblemDTO problemDTO) {
        return problemService.getListByCondition(problemDTO);
    }

}