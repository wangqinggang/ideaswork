package cn.ideaswork.ideacoder.domain.lms.comment;

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
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Comment saveComment(final Comment comment) {
        return commentDao.save(comment);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentDao.findAll();
    }

    @Override
    public Comment getCommentById(final String id) {
        return commentDao.findById(id).orElse(new Comment());
    }

    @Override
    @Transactional
    public Comment updateCommentById(final Comment comment, final String id) {
        Comment commentDb = commentDao.findById(id).orElse(new Comment());
        BeanUtils.copyProperties(comment, commentDb);
        return commentDao.save(commentDb);
    }

    @Override
    @Transactional
    public void deleteCommentById(final String id) {
        commentDao.deleteById(id);
    }

    @Override
    public Boolean isCommentExist(final String id) {
        return commentDao.existsById(id);
    }

    @Override
    public Page<Comment> getPageByCondition(final CommentDTO commentDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(commentDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(commentDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getPlrid())) {
            Criteria criteria = Criteria.where("plrid").is(commentDTO.getPlrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getPlrname())) {
            Criteria criteria = Criteria.where("plrname").is(commentDTO.getPlrname());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getAvatarUrl())) {
            Criteria criteria = Criteria.where("avatarUrl").is(commentDTO.getAvatarUrl());
            query.addCriteria(criteria);
        }
        if (commentDTO.getScore() != null) {
            if (commentDTO.getScore() != 0) {
                Criteria criteria = Criteria.where("score").is(commentDTO.getScore());
                query.addCriteria(criteria);
            }
        }
        if (!StringUtils.isEmpty(commentDTO.getContent())) {
            Criteria criteria = Criteria.where("content").is(commentDTO.getContent());
            query.addCriteria(criteria);
        }
        if (commentDTO.getIsPublic() != null) {
            Criteria criteria = Criteria.where("isPublic").is(commentDTO.getIsPublic());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Comment.class), pageable);
    }

    @Override
    public List<Comment> getListByCondition(final CommentDTO commentDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(commentDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(commentDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getPlrid())) {
            Criteria criteria = Criteria.where("plrid").is(commentDTO.getPlrid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getPlrname())) {
            Criteria criteria = Criteria.where("plrname").is(commentDTO.getPlrname());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getAvatarUrl())) {
            Criteria criteria = Criteria.where("avatarUrl").is(commentDTO.getAvatarUrl());
            query.addCriteria(criteria);
        }
        if (commentDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(commentDTO.getScore());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(commentDTO.getContent())) {
            Criteria criteria = Criteria.where("content").is(commentDTO.getContent());
            query.addCriteria(criteria);
        }
        if (commentDTO.getIsPublic() != null) {
            Criteria criteria = Criteria.where("isPublic").is(commentDTO.getIsPublic());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Comment.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        BeanUtils.copyProperties(commentDTO, comment);
        return comment;
    }

    public CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        BeanUtils.copyProperties(comment, commentDTO);
        return commentDTO;
    }
}