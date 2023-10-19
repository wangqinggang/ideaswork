package cn.ideaswork.ideacoder.domain.coder.project;


import cn.ideaswork.ideacoder.domain.coder.domain.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
  Project saveProject(Project project);

  List<Project> getAllProjects();

  Project getProjectById(String id);

  Project updateProjectById(Project project, String id);

  void deleteProjectById(String id);

  Boolean isProjectExist(String id);

  Page<Project> getProjectPageListByCondition(Project project, Pageable pageable);

    List<Project> getAllProjectsByUserId(String userid);
}
