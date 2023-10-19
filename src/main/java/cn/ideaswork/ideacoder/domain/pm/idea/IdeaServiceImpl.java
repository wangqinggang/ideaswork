package cn.ideaswork.ideacoder.domain.pm.idea;

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
public class IdeaServiceImpl implements IdeaService {
    @Autowired
    private IdeaDao ideaDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Idea saveIdea(final Idea idea) {
        return ideaDao.save(idea);
    }

    @Override
    public List<Idea> getAllIdeas() {
        return ideaDao.findAll();
    }

    @Override
    public Idea getIdeaById(final String id) {
        return ideaDao.findById(id).orElse(new Idea());
    }

    @Override
    @Transactional
    public Idea updateIdeaById(final Idea idea, final String id) {
        Idea ideaDb = ideaDao.findById(id).orElse(new Idea());
        BeanUtils.copyProperties(idea, ideaDb);
        return ideaDao.save(ideaDb);
    }

    @Override
    @Transactional
    public void deleteIdeaById(final String id) {
        ideaDao.deleteById(id);
    }

    @Override
    public Boolean isIdeaExist(final String id) {
        return ideaDao.existsById(id);
    }

    @Override
    public Page<Idea> getPageByCondition(final IdeaDTO ideaDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(ideaDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(ideaDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(ideaDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(ideaDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(ideaDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProductName())) {
            Criteria criteria = Criteria.where("productName").is(ideaDTO.getProductName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProblemid())) {
            Criteria criteria = Criteria.where("problemid").is(ideaDTO.getProblemid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProblemName())) {
            Criteria criteria = Criteria.where("problemName").is(ideaDTO.getProblemName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getIdea())) {
            Criteria criteria = Criteria.where("idea").regex(ideaDTO.getIdea());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getCustomer())) {
            Criteria criteria = Criteria.where("customer").regex(ideaDTO.getCustomer());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUseRole())) {
            Criteria criteria = Criteria.where("useRole").regex(ideaDTO.getUseRole());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getMotivation())) {
            Criteria criteria = Criteria.where("motivation").regex(ideaDTO.getMotivation());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getReason())) {
            Criteria criteria = Criteria.where("reason").regex(ideaDTO.getReason());
            query.addCriteria(criteria);
        }
        if (ideaDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").gte(ideaDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Idea.class), pageable);
    }

    @Override
    public List<Idea> getListByCondition(final IdeaDTO ideaDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(ideaDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(ideaDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(ideaDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(ideaDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(ideaDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProductName())) {
            Criteria criteria = Criteria.where("productName").is(ideaDTO.getProductName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProblemid())) {
            Criteria criteria = Criteria.where("problemid").is(ideaDTO.getProblemid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getProblemName())) {
            Criteria criteria = Criteria.where("problemName").is(ideaDTO.getProblemName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getIdea())) {
            Criteria criteria = Criteria.where("idea").is(ideaDTO.getIdea());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getCustomer())) {
            Criteria criteria = Criteria.where("customer").is(ideaDTO.getCustomer());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getUseRole())) {
            Criteria criteria = Criteria.where("useRole").is(ideaDTO.getUseRole());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getMotivation())) {
            Criteria criteria = Criteria.where("motivation").is(ideaDTO.getMotivation());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(ideaDTO.getReason())) {
            Criteria criteria = Criteria.where("reason").is(ideaDTO.getReason());
            query.addCriteria(criteria);
        }
        if (ideaDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").is(ideaDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Idea.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Idea mapToEntity(final IdeaDTO ideaDTO, final Idea idea) {
        BeanUtils.copyProperties(ideaDTO, idea);
        return idea;
    }

    public IdeaDTO mapToDTO(final Idea idea, final IdeaDTO ideaDTO) {
        BeanUtils.copyProperties(idea, ideaDTO);
        return ideaDTO;
    }
}