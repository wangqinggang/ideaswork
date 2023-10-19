package cn.ideaswork.ideacoder.domain.pm.storymap;

import cn.ideaswork.ideacoder.domain.pm.task.Task;
import cn.ideaswork.ideacoder.domain.pm.task.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(
        tags = "用户故事地图 API"
)
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/storymaps")
@CrossOrigin
public class StorymapController {
    @Autowired
    StorymapService storymapService;

    @Autowired
    TaskService taskService;

    @PostMapping
    @ApiOperation("添加用户故事地图")
    public Storymap saveStorymap(@RequestBody Storymap storymap) {
        String storymapId = UUID.randomUUID().toString();
        storymap.setId(storymapId)
                .setCreatetime(new Date())
                .setUserid("001");
        List<UserGoal> goals = storymap.getGoals();
        if (goals == null) {
            UserGoal userGoal = new UserGoal();
            userGoal.setId(UUID.randomUUID().toString())
                    .setModify(false)
                    .setUserid("001")
                    .setTitle("第一个用户目标")
                    .setCreatetime(new Date());
            UserActivity userActivity = new UserActivity();
            String activityid = UUID.randomUUID().toString();
            userActivity.setId(activityid)
                    .setEndTasks(new ArrayList<>())
                    .setMiddleTasks(new ArrayList<>())
                    .setMvpTasks(new ArrayList<>())
                    .setModifyTitle(false)
                    .setModifyIntro(false)
                    .setCreatetime(new Date())
                    .setTitle("第一个用户活动")
                    .setIntro("活动简介");
            Task task = new Task();
            task.setId(UUID.randomUUID().toString())
                    .setCreateTime(new Date())
                    .setStorymapid(storymapId)
                    .setStatus("Backlog")
                    .setDocumentUrl("https://www.ideaworks.club")
                    .setFzrid("001")
                    .setFzrname("admin")
                    .setActivityid(activityid)
                    .setParentTaskId("")
                    .setModifyTitle(false)
                    .setModifyIntro(false)
                    .setTitle("任务标题")
                    .setIntro("第一个活动任务描述")
                    .setVersion("unscheduled"); // TODO 还有 mvp middle end
            List<Task> tasks = new ArrayList<>();
            tasks.add(task);
            userActivity.setChildTasks(tasks);
            List<UserActivity> userActivities = new ArrayList<>();
            userActivities.add(userActivity);
            userGoal.setActivities(userActivities);
            List<UserGoal> userGoals = new ArrayList<>();
            userGoals.add(userGoal);
            storymap.setGoals(userGoals);
            taskService.saveTask(task);
        } else {
            goals.forEach(goal -> {
                goal.setCreatetime(new Date())
                        .setId(UUID.randomUUID().toString())
                        .setUserid("001");
                List<UserActivity> activities = goal.getActivities();
                activities.forEach(activity -> {
                    String activityId = UUID.randomUUID().toString();
                    activity.setCreatetime(new Date())
                            .setModifyTitle(false)
                            .setModifyIntro(false)
                            .setId(activityId)
                            .setMvpTasks(new ArrayList<>())
                            .setMiddleTasks(new ArrayList<>())
                            .setEndTasks(new ArrayList<>());
                    List<Task> childTasks = activity.getChildTasks();
                    childTasks.forEach(childTask -> {
                        childTask.setActivityid(activityId)
                                .setCreateTime(new Date())
                                .setFzrname("")
                                .setId(UUID.randomUUID().toString())
                                .setParentTaskId("").setStatus("0")
                                .setStorymapid(storymap.getId());
                        taskService.saveTask(childTask);
                    });
                });
            });
        }
        return storymapService.saveStorymap(storymap);
    }

    @GetMapping
    @ApiOperation("获取 用户故事地图列表")
    public List<Storymap> getStorymaps() {
        long start = System.currentTimeMillis();
        List<Storymap> allStorymaps = storymapService.getAllStorymaps();
        long end = System.currentTimeMillis();
        System.out.println((end - start)/1000);
        return allStorymaps;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 用户故事地图")
    public Storymap getStorymap(@PathVariable("id") final String id) {
        return storymapService.getStorymapById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新用户故事地图")
    public Storymap updateStorymap(@RequestBody Storymap storymap,
                                   @PathVariable("id") final String id) {
        storymap.setId(id);
        List<UserGoal> goals = storymap.getGoals();
        if (goals == null) {

        } else {
            goals.forEach(goal -> {
                goal.setUserid("001");
                List<UserActivity> activities = goal.getActivities();
                if (activities == null) {
                } else {
                    activities.forEach(activity -> {
                        if(activity.getId() == null){
                            activity.setId(UUID.randomUUID().toString());
                        }
                        List<Task> childTasks = activity.getChildTasks();
                        List<Task> mvpTasks = activity.getMvpTasks();
                        List<Task> middleTasks = activity.getMiddleTasks();
                        List<Task> endTasks = activity.getEndTasks();

                        if (childTasks.size() > 0) {
                            childTasks.forEach(childTask -> {
                                if (StringUtils.isBlank(childTask.getId())) {
                                    childTask.setId(UUID.randomUUID().toString());
                                }
                                if(StringUtils.isBlank(childTask.getStorymapid())){
                                    childTask.setStorymapid(storymap.getId());
                                }
                                if(StringUtils.isBlank(childTask.getActivityid())){
                                    childTask.setActivityid(activity.getId());
                                }
                                childTask.setVersion("unscheduled");
                                Boolean taskExist = taskService.isTaskExist(childTask.getId());
                                if (taskExist) {
                                    taskService.updateTaskById(childTask, childTask.getId());
                                } else {
                                    taskService.saveTask(childTask);
                                }
                            });
                        }
                        if (mvpTasks.size() > 0) {
                            mvpTasks.forEach(mvpTask -> {
                                if (StringUtils.isBlank(mvpTask.getId())) {
                                    mvpTask.setId(UUID.randomUUID().toString());
                                }
                                if(StringUtils.isBlank(mvpTask.getStorymapid())){
                                    mvpTask.setStorymapid(storymap.getId());
                                }
                                if(StringUtils.isBlank(mvpTask.getActivityid())){
                                    mvpTask.setActivityid(activity.getId());
                                }
                                mvpTask.setVersion("mvp");
                                Boolean taskExist = taskService.isTaskExist(mvpTask.getId());
                                if (taskExist) {
                                    taskService.updateTaskById(mvpTask, mvpTask.getId());
                                } else {
                                    taskService.saveTask(mvpTask);
                                }
                            });
                        }
                        if (middleTasks.size() > 0) {
                            middleTasks.forEach(middleTask -> {
                                if (StringUtils.isBlank(middleTask.getId())) {
                                    middleTask.setId(UUID.randomUUID().toString());
                                }
                                if(StringUtils.isBlank(middleTask.getStorymapid())){
                                    middleTask.setStorymapid(storymap.getId());
                                }
                                if(StringUtils.isBlank(middleTask.getActivityid())){
                                    middleTask.setActivityid(activity.getId());
                                }
//                                if(StringUtils.isBlank(middleTask.getProjectid())){
//                                    middleTask.setProjectid(storymap.getProductid());
//                                }
                                middleTask.setVersion("middle");
                                Boolean taskExist = taskService.isTaskExist(middleTask.getId());
                                if (taskExist) {
                                    taskService.updateTaskById(middleTask, middleTask.getId());
                                } else {
                                    taskService.saveTask(middleTask);
                                }
                            });
                        }
                        if (endTasks.size() > 0) {
                            endTasks.forEach(endTask -> {
                                if (StringUtils.isBlank(endTask.getId())) {
                                    endTask.setId(UUID.randomUUID().toString());
                                }
                                if(StringUtils.isBlank(endTask.getStorymapid())){
                                    endTask.setStorymapid(storymap.getId());
                                }
                                if(StringUtils.isBlank(endTask.getActivityid())){
                                    endTask.setActivityid(activity.getId());
                                }
                                endTask.setVersion("end");
                                Boolean taskExist = taskService.isTaskExist(endTask.getId());
                                if (taskExist) {
                                    taskService.updateTaskById(endTask, endTask.getId());
                                } else {
                                    taskService.saveTask(endTask);
                                }
                            });
                        }
                    });
                }
            });
        }
        return storymapService.updateStorymapById(storymap, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除用户故事地图")
    public void deleteStorymapById(@PathVariable("id") final String id) {
        storymapService.deleteStorymapById(id);
    }
//    @DeleteMapping("/deleteActivity/{activityId}")
//    @ApiOperation("根据主键 id 删除用户故事地图")
//    public void deleteActivityByActivityId(@PathVariable("activityId") final String activityId) {
//        // 根据 activityId 获取用户故事地图
//        StorymapDTO storymapDTO = new StorymapDTO();
//        storymapDTO.set
//        storymapService.getListByCondition()
//        storymapService.deleteStorymapById(activityId);
//    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看用户故事地图是否存在")
    public Boolean isExistStorymap(@PathVariable("id") final String id) {
        return storymapService.isStorymapExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Storymap> getPageByCondition(StorymapDTO storymapDTO,
                                             @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return storymapService.getPageByCondition(storymapDTO, pageable);
    }

    @GetMapping("/getList")
    @ApiOperation("分页条件查询")
    public List<Storymap> getListByCondition(StorymapDTO storymapDTO) {
        return storymapService.getListByCondition(storymapDTO);
    }

}