package cn.ideaswork.ideacoder.domain.lms.arrangement;

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
public class ArrangementServiceImpl implements ArrangementService {
    @Autowired
    private ArrangementDao arrangementDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Arrangement saveArrangement(final Arrangement arrangement) {
        return arrangementDao.save(arrangement);
    }

    @Override
    public List<Arrangement> getAllArrangements() {
        return arrangementDao.findAll();
    }

    @Override
    public Arrangement getArrangementById(final String id) {
        return arrangementDao.findById(id).orElse(new Arrangement());
    }

    @Override
    @Transactional
    public Arrangement updateArrangementById(final Arrangement arrangement, final String id) {
        Arrangement arrangementDb = arrangementDao.findById(id).orElse(new Arrangement());
        BeanUtils.copyProperties(arrangement, arrangementDb);
        return arrangementDao.save(arrangementDb);
    }

    @Override
    @Transactional
    public void deleteArrangementById(final String id) {
        arrangementDao.deleteById(id);
    }

    @Override
    public Boolean isArrangementExist(final String id) {
        return arrangementDao.existsById(id);
    }

    @Override
    public Page<Arrangement> getPageByCondition(final ArrangementDTO arrangementDTO,
                                                final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(arrangementDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(arrangementDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getClassesId())) {
            Criteria criteria = Criteria.where("classesId").is(arrangementDTO.getClassesId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getClassesName())) {
            Criteria criteria = Criteria.where("classesName").is(arrangementDTO.getClassesName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getCourseId())) {
            Criteria criteria = Criteria.where("courseId").is(arrangementDTO.getCourseId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getCourseName())) {
            Criteria criteria = Criteria.where("courseName").is(arrangementDTO.getCourseName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getSectionid())) {
            Criteria criteria = Criteria.where("sectionid").is(arrangementDTO.getSectionid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getSectionName())) {
            Criteria criteria = Criteria.where("sectionName").is(arrangementDTO.getSectionName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getTeacherId())) {
            Criteria criteria = Criteria.where("teacherId").is(arrangementDTO.getTeacherId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getTeacherName())) {
            Criteria criteria = Criteria.where("teacherName").is(arrangementDTO.getTeacherName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getRoomId())) {
            Criteria criteria = Criteria.where("roomId").is(arrangementDTO.getRoomId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getRoomName())) {
            Criteria criteria = Criteria.where("roomName").is(arrangementDTO.getRoomName());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getStartTime() != null) {
            Criteria criteria = Criteria.where("startTime").gte(arrangementDTO.getStartTime());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getEndTime() != null) {
            Criteria criteria = Criteria.where("endTime").gte(arrangementDTO.getEndTime());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getDay() != null) {
            Criteria criteria = Criteria.where("day").gte(arrangementDTO.getDay());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(arrangementDTO.getStatus());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Arrangement.class), pageable);
    }

    @Override
    public List<Arrangement> getListByCondition(final ArrangementDTO arrangementDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(arrangementDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(arrangementDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getClassesId())) {
            Criteria criteria = Criteria.where("classesId").is(arrangementDTO.getClassesId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getClassesName())) {
            Criteria criteria = Criteria.where("classesName").is(arrangementDTO.getClassesName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getCourseId())) {
            Criteria criteria = Criteria.where("courseId").is(arrangementDTO.getCourseId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getCourseName())) {
            Criteria criteria = Criteria.where("courseName").is(arrangementDTO.getCourseName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getSectionid())) {
            Criteria criteria = Criteria.where("sectionid").is(arrangementDTO.getSectionid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getSectionName())) {
            Criteria criteria = Criteria.where("sectionName").is(arrangementDTO.getSectionName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getTeacherId())) {
            Criteria criteria = Criteria.where("teacherId").is(arrangementDTO.getTeacherId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getTeacherName())) {
            Criteria criteria = Criteria.where("teacherName").is(arrangementDTO.getTeacherName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getRoomId())) {
            Criteria criteria = Criteria.where("roomId").is(arrangementDTO.getRoomId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getRoomName())) {
            Criteria criteria = Criteria.where("roomName").is(arrangementDTO.getRoomName());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getStartTime() != null) {
            Criteria criteria = Criteria.where("startTime").gte(arrangementDTO.getStartTime());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getEndTime() != null) {
            Criteria criteria = Criteria.where("endTime").gte(arrangementDTO.getEndTime());
            query.addCriteria(criteria);
        }
        if (arrangementDTO.getDay() != null) {
            Criteria criteria = Criteria.where("day").gte(arrangementDTO.getDay());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(arrangementDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(arrangementDTO.getStatus());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Arrangement.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Arrangement mapToEntity(final ArrangementDTO arrangementDTO,
                                   final Arrangement arrangement) {
        BeanUtils.copyProperties(arrangementDTO, arrangement);
        return arrangement;
    }

    public ArrangementDTO mapToDTO(final Arrangement arrangement,
                                   final ArrangementDTO arrangementDTO) {
        BeanUtils.copyProperties(arrangement, arrangementDTO);
        return arrangementDTO;
    }
}