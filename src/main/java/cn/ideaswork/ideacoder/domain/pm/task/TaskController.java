package cn.ideaswork.ideacoder.domain.pm.task;

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
        tags = "任务 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/tasks")
@CrossOrigin()
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    @ApiOperation("添加任务")
    public Task saveTask(@RequestBody Task task) {
        task.setId(UUID.randomUUID().toString());
        if (task.getExpectedTime() == null) {
            task.setExpectedTime(0);
        }
        if (task.getNeedTime() == null) {
            task.setNeedTime(0);
        }
        if (task.getSpendTime() == null) {
            task.setSpendTime(0);
        }
        return taskService.saveTask(task);
    }

    @GetMapping
    @ApiOperation("获取 任务列表")
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 任务")
    public Task getTask(@PathVariable("id") final String id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新任务")
    public Task updateTask(@RequestBody Task task, @PathVariable("id") final String id) {
        if (task.getExpectedTime() == null) {
            task.setExpectedTime(0);
        }
        if (task.getNeedTime() == null) {
            task.setNeedTime(0);
        }
        if (task.getSpendTime() == null) {
            task.setSpendTime(0);
        }
        return taskService.updateTaskById(task, id);
    }

    @PutMapping("/updateType/{type}")
    @ApiOperation("更新任务列表的 status 属性 0 Backlog 1 Inprogress 2 Review 3 Done")
    public Boolean updateTask(@RequestBody List<Task> tasks, @PathVariable("type") final String type) {
        return taskService.updateTasks(tasks, type);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除任务")
    public void deleteTaskById(@PathVariable("id") final String id) {
        taskService.deleteTaskById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看任务是否存在")
    public Boolean isExistTask(@PathVariable("id") final String id) {
        return taskService.isTaskExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Task> getPageByCondition(TaskDTO taskDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return taskService.getPageByCondition(taskDTO, pageable);
    }

    @GetMapping("/getList")
    @ApiOperation("分页条件查询")
    public List<Task> getListByCondition(TaskDTO taskDTO) {
        return taskService.getListByCondition(taskDTO);
    }
}