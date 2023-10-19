package cn.ideaswork.ideacoder.domain.pm.task;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Task saveTask(final Task task) {
        return taskDao.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }

    @Override
    public Task getTaskById(final String id) {
        return taskDao.findById(id).orElse(new Task());
    }

    @Override
    @Transactional
    public Task updateTaskById(final Task task, final String id) {
        Task taskDb = taskDao.findById(id).orElse(new Task());
        BeanUtils.copyProperties(task, taskDb);
        return taskDao.save(taskDb);
    }

    @Override
    @Transactional
    public void deleteTaskById(final String id) {
        taskDao.deleteById(id);
    }

    @Override
    public Boolean isTaskExist(final String id) {
        return taskDao.existsById(id);
    }

    @Override
    public Page<Task> getPageByCondition(final TaskDTO taskDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(taskDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(taskDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getProjectid())) {
            Criteria criteria = Criteria.where("projectid").is(taskDTO.getProjectid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getStorymapid())) {
            Criteria criteria = Criteria.where("storymapid").is(taskDTO.getStorymapid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getActivityid())) {
            Criteria criteria = Criteria.where("activityid").is(taskDTO.getActivityid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(taskDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getFzrid())) {
            Criteria criteria = Criteria.where("fzrid").is(taskDTO.getFzrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getFzrname())) {
            Criteria criteria = Criteria.where("fzrname").regex(taskDTO.getFzrname());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getTitle())) {
            Criteria criteria = Criteria.where("title").regex(taskDTO.getTitle());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").regex(taskDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getVersion())) {
            if (!taskDTO.getVersion().equals("all")) {
                Criteria criteria = Criteria.where("version").regex(taskDTO.getVersion());
                query.addCriteria(criteria);
            }
        }
        if (taskDTO.getCreateTime() != null) {
            Criteria criteria = Criteria.where("createTime").gte(taskDTO.getCreateTime());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Task.class), pageable);
    }

    @Override
    public List<Task> getListByCondition(final TaskDTO taskDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(taskDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(taskDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getProjectid())) {
            Criteria criteria = Criteria.where("projectid").is(taskDTO.getProjectid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getStorymapid())) {
            Criteria criteria = Criteria.where("storymapid").is(taskDTO.getStorymapid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getActivityid())) {
            Criteria criteria = Criteria.where("activityid").is(taskDTO.getActivityid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(taskDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getFzrid())) {
            Criteria criteria = Criteria.where("fzrid").is(taskDTO.getFzrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getFzrname())) {
            Criteria criteria = Criteria.where("fzrname").regex(taskDTO.getFzrname());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getTitle())) {
            Criteria criteria = Criteria.where("title").regex(taskDTO.getTitle());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").regex(taskDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(taskDTO.getVersion())) {
            if (!taskDTO.getVersion().equals("all")) {
                Criteria criteria = Criteria.where("version").regex(taskDTO.getVersion());
                query.addCriteria(criteria);
            }
        }
        if (taskDTO.getCreateTime() != null) {
            Criteria criteria = Criteria.where("createTime").gte(taskDTO.getCreateTime());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Task.class);
    }

    @Override
    public Boolean updateTasks(List<Task> tasks, String type) {
        String status = "";
        if (type.equals("0")) {
            status = "Backlog";
        } else if (type.equals("1")) {
            status = "In Progress";

        } else if (type.equals("2")) {
            status = "Review";
        } else {
            status = "Done";
        }
        for (Task task : tasks) {
            task.setStatus(status);
        }
        taskDao.saveAll(tasks);
        return true;
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        BeanUtils.copyProperties(taskDTO, task);
        return task;
    }

    public TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        BeanUtils.copyProperties(task, taskDTO);
        return taskDTO;
    }
}