package cn.ideaswork.ideacoder.domain.pm.task;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
  Task saveTask(Task task);

  List<Task> getAllTasks();

  Task getTaskById(final String id);

  Task updateTaskById(Task task, final String id);

  void deleteTaskById(final String id);

  Boolean isTaskExist(final String id);

  Page<Task> getPageByCondition(TaskDTO taskDTO, Pageable pageable);

  List<Task> getListByCondition(TaskDTO taskDTO);

  Boolean updateTasks(List<Task> tasks, String type);
}
