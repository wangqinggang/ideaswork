package cn.ideaswork.ideacoder.domain.lms.project;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.List;

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
public class LmsProjectServiceImpl implements LmsProjectService {
    @Autowired
    private LmsProjectDao lmsProjectDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public LmsProject saveProject(final LmsProject project) {
        return lmsProjectDao.save(project);
    }

    @Override
    public List<LmsProject> getAllProjects() {
        return lmsProjectDao.findAll();
    }

    @Override
    public LmsProject getProjectById(final String id) {
        return lmsProjectDao.findById(id).orElse(new LmsProject());
    }

    @Override
    @Transactional
    public LmsProject updateProjectById(final LmsProject project, final String id) {
        LmsProject projectDb = lmsProjectDao.findById(id).orElse(new LmsProject());
        BeanUtils.copyProperties(project, projectDb);
        return lmsProjectDao.save(projectDb);
    }

    @Override
    @Transactional
    public void deleteProjectById(final String id) {
        lmsProjectDao.deleteById(id);
    }

    @Override
    public Boolean isProjectExist(final String id) {
        return lmsProjectDao.existsById(id);
    }

    @Override
    public Page<LmsProject> getPageByCondition(final LmsProjectDTO projectDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(projectDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(projectDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(projectDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(projectDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFzrid())) {
            Criteria criteria = Criteria.where("fzrid").is(projectDTO.getFzrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFzrName())) {
            Criteria criteria = Criteria.where("fzrName").regex(projectDTO.getFzrName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getContent())) {
            Criteria criteria = Criteria.where("content").is(projectDTO.getContent());
            query.addCriteria(criteria);
        }
        if (projectDTO.getSpentTime() != null) {
            Criteria criteria = Criteria.where("spentTime").is(projectDTO.getSpentTime());
            query.addCriteria(criteria);
        }
        if (projectDTO.getNeedTime() != null) {
            Criteria criteria = Criteria.where("needTime").is(projectDTO.getNeedTime());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(projectDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getMilestone())) {
            Criteria criteria = Criteria.where("milestone").is(projectDTO.getMilestone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFjName())) {
            Criteria criteria = Criteria.where("fjName").regex(projectDTO.getFjName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFjUrl())) {
            Criteria criteria = Criteria.where("fjUrl").is(projectDTO.getFjUrl());
            query.addCriteria(criteria);
        }
        if (projectDTO.getStartDate() != null) {
            Criteria criteria = Criteria.where("startDate").gte(projectDTO.getStartDate());
            query.addCriteria(criteria);
        }
        if (projectDTO.getEndDate() != null) {
            Criteria criteria = Criteria.where("endDate").gte(projectDTO.getEndDate());
            query.addCriteria(criteria);
        }
        Sort sort = pageable.getSort();
        query.with(sort);
        return this.listToPage(mongoTemplate.find(query, LmsProject.class), pageable);
    }

    @Override
    public List<LmsProject> getListByCondition(final LmsProjectDTO projectDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(projectDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(projectDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(projectDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(projectDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFzrid())) {
            Criteria criteria = Criteria.where("fzrid").is(projectDTO.getFzrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFzrName())) {
            Criteria criteria = Criteria.where("fzrName").regex(projectDTO.getFzrName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(projectDTO.getContent());
            query.addCriteria(criteria);
        }
        if (projectDTO.getSpentTime() != null) {
            Criteria criteria = Criteria.where("spentTime").is(projectDTO.getSpentTime());
            query.addCriteria(criteria);
        }
        if (projectDTO.getNeedTime() != null) {
            Criteria criteria = Criteria.where("needTime").is(projectDTO.getNeedTime());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(projectDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getMilestone())) {
            Criteria criteria = Criteria.where("milestone").is(projectDTO.getMilestone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFjName())) {
            Criteria criteria = Criteria.where("fjName").regex(projectDTO.getFjName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(projectDTO.getFjUrl())) {
            Criteria criteria = Criteria.where("fjUrl").is(projectDTO.getFjUrl());
            query.addCriteria(criteria);
        }
        if (projectDTO.getStartDate() != null) {
            Criteria criteria = Criteria.where("startDate").gte(projectDTO.getStartDate());
            query.addCriteria(criteria);
        }
        if (projectDTO.getEndDate() != null) {
            Criteria criteria = Criteria.where("endDate").gte(projectDTO.getEndDate());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, LmsProject.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public LmsProject mapToEntity(final LmsProjectDTO projectDTO, final LmsProject project) {
        BeanUtils.copyProperties(projectDTO, project);
        return project;
    }

    public LmsProjectDTO mapToDTO(final LmsProject project, final LmsProjectDTO projectDTO) {
        BeanUtils.copyProperties(project, projectDTO);
        return projectDTO;
    }
}