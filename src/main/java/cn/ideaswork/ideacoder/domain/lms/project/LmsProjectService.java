package cn.ideaswork.ideacoder.domain.lms.project;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LmsProjectService {
  LmsProject saveProject(LmsProject project);

  List<LmsProject> getAllProjects();

  LmsProject getProjectById(final String id);

  LmsProject updateProjectById(LmsProject project, final String id);

  void deleteProjectById(final String id);

  Boolean isProjectExist(final String id);

  Page<LmsProject> getPageByCondition(LmsProjectDTO projectDTO, Pageable pageable);

  List<LmsProject> getListByCondition(LmsProjectDTO projectDTO);
}
