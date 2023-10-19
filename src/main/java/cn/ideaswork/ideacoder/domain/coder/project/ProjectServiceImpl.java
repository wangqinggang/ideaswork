package cn.ideaswork.ideacoder.domain.coder.project;

import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(String id) {
        return projectRepository.findById(id).orElse(new Project());
    }

    @Override
    public Project updateProjectById(Project project, String id) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProjectById(String id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Boolean isProjectExist(String id) {
        return projectRepository.existsById(id);
    }

    @Override
    public Page<Project> getProjectPageListByCondition(Project project, Pageable pageable) {
        Query query = new Query();
        if (StringUtils.isNotBlank(project.getId())) {
            Criteria criteria =  Criteria.where("id").is(project.getId());
            query.addCriteria(criteria);
        }
        if (StringUtils.isNotBlank(project.getEnname())) {
            Criteria criteria =  Criteria.where("enname").is(project.getEnname());
            query.addCriteria(criteria);
        }
        if (StringUtils.isNotBlank(project.getStatus())) {
            query.addCriteria(Criteria.where("status").is(project.getStatus()));
        }


//        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);
        List<Project> projects1 = mongoTemplate.find(query, Project.class);
//        PageImpl<Project> usersPage = new PageImpl<Project>(projects, pageable, projects.size());
        return SysTools.listToPage(projects1,pageable);
    }

    @Override
    public List<Project> getAllProjectsByUserId(String userid) {
        return  projectRepository.findByUserId(userid);
    }


}
