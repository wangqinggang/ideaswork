package cn.ideaswork.ideacoder.domain.lms.feedback;

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
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Feedback saveFeedback(final Feedback feedback) {
        return feedbackDao.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackDao.findAll();
    }

    @Override
    public Feedback getFeedbackById(final String id) {
        return feedbackDao.findById(id).orElse(new Feedback());
    }

    @Override
    @Transactional
    public Feedback updateFeedbackById(final Feedback feedback, final String id) {
        Feedback feedbackDb = feedbackDao.findById(id).orElse(new Feedback());
        BeanUtils.copyProperties(feedback, feedbackDb);
        return feedbackDao.save(feedbackDb);
    }

    @Override
    @Transactional
    public void deleteFeedbackById(final String id) {
        feedbackDao.deleteById(id);
    }

    @Override
    public Boolean isFeedbackExist(final String id) {
        return feedbackDao.existsById(id);
    }

    @Override
    public Page<Feedback> getPageByCondition(final FeedbackDTO feedbackDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(feedbackDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(feedbackDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(feedbackDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getEmail())) {
            Criteria criteria = Criteria.where("email").is(feedbackDTO.getEmail());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getPhone())) {
            Criteria criteria = Criteria.where("phone").is(feedbackDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getSubject())) {
            Criteria criteria = Criteria.where("subject").regex(feedbackDTO.getSubject());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(feedbackDTO.getContent());
            query.addCriteria(criteria);
        }
        if (feedbackDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").gte(feedbackDTO.getCjsj());
            query.addCriteria(criteria);
        }

        if (feedbackDTO.getRead() != null) {
            Criteria criteria = Criteria.where("read").is(feedbackDTO.getRead());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Feedback.class), pageable);
    }

    @Override
    public List<Feedback> getListByCondition(final FeedbackDTO feedbackDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(feedbackDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(feedbackDTO.getId());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(feedbackDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(feedbackDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getEmail())) {
            Criteria criteria = Criteria.where("email").is(feedbackDTO.getEmail());
            query.addCriteria(criteria);
        }
        if (feedbackDTO.getPhone() != null) {
            Criteria criteria = Criteria.where("phone").is(feedbackDTO.getPhone());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getSubject())) {
            Criteria criteria = Criteria.where("subject").regex(feedbackDTO.getSubject());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(feedbackDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(feedbackDTO.getContent());
            query.addCriteria(criteria);
        }
        if (feedbackDTO.getRead() != null) {
            Criteria criteria = Criteria.where("read").is(feedbackDTO.getRead());
            query.addCriteria(criteria);
        }
        if (feedbackDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").gte(feedbackDTO.getCjsj());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Feedback.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Feedback mapToEntity(final FeedbackDTO feedbackDTO, final Feedback feedback) {
        BeanUtils.copyProperties(feedbackDTO, feedback);
        return feedback;
    }

    public FeedbackDTO mapToDTO(final Feedback feedback, final FeedbackDTO feedbackDTO) {
        BeanUtils.copyProperties(feedback, feedbackDTO);
        return feedbackDTO;
    }
}