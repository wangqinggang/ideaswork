package cn.ideaswork.ideacoder.domain.pm.problem;


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
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemDao problemDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Problem saveProblem(final Problem problem) {
        return problemDao.save(problem);
    }

    @Override
    public List<Problem> getAllProblems() {
        return problemDao.findAll();
    }

    @Override
    public Problem getProblemById(final String id) {
        return problemDao.findById(id).orElse(new Problem());
    }

    @Override
    @Transactional
    public Problem updateProblemById(final Problem problem, final String id) {
        Problem problemDb = problemDao.findById(id).orElse(new Problem());
        BeanUtils.copyProperties(problem, problemDb);
        return problemDao.save(problemDb);
    }

    @Override
    @Transactional
    public void deleteProblemById(final String id) {
        problemDao.deleteById(id);
    }

    @Override
    public Boolean isProblemExist(final String id) {
        return problemDao.existsById(id);
    }

    @Override
    public Page<Problem> getPageByCondition(final ProblemDTO problemDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(problemDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(problemDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(problemDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(problemDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPersonaid())) {
            Criteria criteria = Criteria.where("personaid").is(problemDTO.getPersonaid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPersonaName())) {
            Criteria criteria = Criteria.where("personName").is(problemDTO.getPersonaName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(problemDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getProductName())) {
            Criteria criteria = Criteria.where("productName").is(problemDTO.getProductName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPainspot())) {
            Criteria criteria = Criteria.where("painspot").regex(problemDTO.getPainspot());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getReason())) {
            Criteria criteria = Criteria.where("reason").regex(problemDTO.getReason());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getEnjoyment())) {
            Criteria criteria = Criteria.where("enjoyment").regex(problemDTO.getEnjoyment());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getSolution())) {
            Criteria criteria = Criteria.where("solution").regex(problemDTO.getSolution());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getOpportunity())) {
            Criteria criteria = Criteria.where("opportunity").regex(problemDTO.getOpportunity());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getBz())) {
            Criteria criteria = Criteria.where("bz").regex(problemDTO.getBz());
            query.addCriteria(criteria);
        }
        if (problemDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").gte(problemDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Problem.class), pageable);
    }

    @Override
    public List<Problem> getListByCondition(final ProblemDTO problemDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(problemDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(problemDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(problemDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(problemDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPersonaid())) {
            Criteria criteria = Criteria.where("personaid").is(problemDTO.getPersonaid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPersonaName())) {
            Criteria criteria = Criteria.where("personName").is(problemDTO.getPersonaName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(problemDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getProductName())) {
            Criteria criteria = Criteria.where("productName").is(problemDTO.getProductName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getPainspot())) {
            Criteria criteria = Criteria.where("painspot").is(problemDTO.getPainspot());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getReason())) {
            Criteria criteria = Criteria.where("reason").is(problemDTO.getReason());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getEnjoyment())) {
            Criteria criteria = Criteria.where("enjoyment").is(problemDTO.getEnjoyment());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getSolution())) {
            Criteria criteria = Criteria.where("solution").is(problemDTO.getSolution());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getOpportunity())) {
            Criteria criteria = Criteria.where("opportunity").is(problemDTO.getOpportunity());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(problemDTO.getBz())) {
            Criteria criteria = Criteria.where("bz").is(problemDTO.getBz());
            query.addCriteria(criteria);
        }
        if (problemDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").is(problemDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Problem.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Problem mapToEntity(final ProblemDTO problemDTO, final Problem problem) {
        BeanUtils.copyProperties(problemDTO, problem);
        return problem;
    }

    public ProblemDTO mapToDTO(final Problem problem, final ProblemDTO problemDTO) {
        BeanUtils.copyProperties(problem, problemDTO);
        return problemDTO;
    }
}