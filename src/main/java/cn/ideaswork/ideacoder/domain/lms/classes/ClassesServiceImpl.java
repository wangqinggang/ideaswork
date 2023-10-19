package cn.ideaswork.ideacoder.domain.lms.classes;

import org.apache.commons.lang3.StringUtils;
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
public class ClassesServiceImpl implements ClassesService {
    @Autowired
    private ClassesDao classesDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Classes saveClasses(final Classes classes) {
        return classesDao.save(classes);
    }

    @Override
    public List<Classes> getAllClassess() {
        return classesDao.findAll();
    }

    @Override
    public Classes getClassesById(final String id) {
        return classesDao.findById(id).orElse(new Classes());
    }

    @Override
    @Transactional
    public Classes updateClassesById(final Classes classes, final String id) {
        Classes classesDb = classesDao.findById(id).orElse(new Classes());
        BeanUtils.copyProperties(classes, classesDb);
        return classesDao.save(classesDb);
    }

    @Override
    @Transactional
    public void deleteClassesById(final String id) {
        classesDao.deleteById(id);
    }

    @Override
    public Boolean isClassesExist(final String id) {
        return classesDao.existsById(id);
    }

    @Override
    public Page<Classes> getPageByCondition(final ClassesDTO classesDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(classesDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(classesDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(classesDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(classesDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getCourseName())) {
            Criteria criteria = Criteria.where("courseName").is(classesDTO.getCourseName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getTeacherName())) {
            Criteria criteria = Criteria.where("teacherName").is(classesDTO.getTeacherName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(classesDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getJd())) {
            Criteria criteria = Criteria.where("jd").is(classesDTO.getJd());
            query.addCriteria(criteria);
        }
        if (classesDTO.getNd() != null) {
            Criteria criteria = Criteria.where("nd").is(classesDTO.getNd());
            query.addCriteria(criteria);
        }
        if (classesDTO.getKbsj() != null) {
            Criteria criteria = Criteria.where("kbsj").gte(classesDTO.getKbsj());
            query.addCriteria(criteria);
        }
        if (classesDTO.getJbsj() != null) {
            Criteria criteria = Criteria.where("jbsj").lte(classesDTO.getJbsj());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Classes.class), pageable);
    }

    @Override
    public List<Classes> getListByCondition(final ClassesDTO classesDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(classesDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(classesDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(classesDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(classesDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getCourseName())) {
            Criteria criteria = Criteria.where("courseName").is(classesDTO.getCourseName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getTeacherName())) {
            Criteria criteria = Criteria.where("teacherName").is(classesDTO.getTeacherName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(classesDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(classesDTO.getJd())) {
            Criteria criteria = Criteria.where("jd").is(classesDTO.getJd());
            query.addCriteria(criteria);
        }
        if (classesDTO.getNd() != null) {
            Criteria criteria = Criteria.where("nd").is(classesDTO.getNd());
            query.addCriteria(criteria);
        }
        if (classesDTO.getKbsj() != null) {
            Criteria criteria = Criteria.where("kbsj").gte(classesDTO.getKbsj());
            query.addCriteria(criteria);
        }
        if (classesDTO.getJbsj() != null) {
            Criteria criteria = Criteria.where("jbsj").lte(classesDTO.getJbsj());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Classes.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Classes mapToEntity(final ClassesDTO classesDTO, final Classes classes) {
        BeanUtils.copyProperties(classesDTO, classes);
        return classes;
    }

    public ClassesDTO mapToDTO(final Classes classes, final ClassesDTO classesDTO) {
        BeanUtils.copyProperties(classes, classesDTO);
        return classesDTO;
    }
}