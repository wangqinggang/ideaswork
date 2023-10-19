package cn.ideaswork.ideacoder.domain.vms.topic;

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
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao topicDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Topic saveTopic(final Topic topic) {
        return topicDao.save(topic);
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicDao.findAll();
    }

    @Override
    public Topic getTopicById(final String id) {
        return topicDao.findById(id).orElse(new Topic());
    }

    @Override
    @Transactional
    public Topic updateTopicById(final Topic topic, final String id) {
        Topic topicDb = topicDao.findById(id).orElse(new Topic());
        BeanUtils.copyProperties(topic, topicDb);
        return topicDao.save(topicDb);
    }

    @Override
    @Transactional
    public void deleteTopicById(final String id) {
        topicDao.deleteById(id);
    }

    @Override
    public Boolean isTopicExist(final String id) {
        return topicDao.existsById(id);
    }

    @Override
    public Page<Topic> getPageByCondition(final TopicDTO topicDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(topicDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(topicDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(topicDTO.getUserId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(topicDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getTarget())) {
            Criteria criteria = Criteria.where("target").regex(topicDTO.getTarget());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getCustom())) {
            Criteria criteria = Criteria.where("custom").regex(topicDTO.getCustom());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getOutline())) {
            Criteria criteria = Criteria.where("outline").regex(topicDTO.getOutline());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").regex(topicDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (topicDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(topicDTO.getPxh());
            query.addCriteria(criteria);
        }
        if (topicDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").gte(topicDTO.getCjsj());
            query.addCriteria(criteria);
        }
        if (topicDTO.getIsFinished() != null) {
            Criteria criteria = Criteria.where("isFinished").is(topicDTO.getIsFinished());
            query.addCriteria(criteria);
        }
        Sort sort = pageable.getSort();
        query.with(sort);
        return this.listToPage(mongoTemplate.find(query, Topic.class), pageable);
    }

    @Override
    public List<Topic> getListByCondition(final TopicDTO topicDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(topicDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(topicDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(topicDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(topicDTO.getUserId());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(topicDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(topicDTO.getName());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(topicDTO.getTarget())) {
            Criteria criteria = Criteria.where("target").regex(topicDTO.getTarget());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(topicDTO.getCustom())) {
            Criteria criteria = Criteria.where("custom").regex(topicDTO.getCustom());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(topicDTO.getOutline())) {
            Criteria criteria = Criteria.where("outline").regex(topicDTO.getOutline());
            query.addCriteria(criteria);
        }

        if (topicDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(topicDTO.getPxh());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(topicDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").regex(topicDTO.getStatus());
            query.addCriteria(criteria);
        }

        if (topicDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").gte(topicDTO.getCjsj());
            query.addCriteria(criteria);
        }

        if (topicDTO.getIsFinished() != null) {
            Criteria criteria = Criteria.where("isFinished").is(topicDTO.getIsFinished());
            query.addCriteria(criteria);
        }

        return mongoTemplate.find(query, Topic.class);
    }

    @Override
    public Integer getTopicNumByUserId(String id) {
        return topicDao.countByUserId(id);
    }

    @Override
    public Integer checktopicCount(String userId) {
        return topicDao.countByUserId(userId);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Topic mapToEntity(final TopicDTO topicDTO, final Topic topic) {
        BeanUtils.copyProperties(topicDTO, topic);
        return topic;
    }

    public TopicDTO mapToDTO(final Topic topic, final TopicDTO topicDTO) {
        BeanUtils.copyProperties(topic, topicDTO);
        return topicDTO;
    }
}