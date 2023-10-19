package cn.ideaswork.ideacoder.domain.pm.storymap;

import cn.ideaswork.ideacoder.domain.pm.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
public class StorymapServiceImpl implements StorymapService {
    @Autowired
    private StorymapDao storymapDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Storymap saveStorymap(final Storymap storymap) {
        return storymapDao.save(storymap);
    }

    @Override
    public List<Storymap> getAllStorymaps() {
        return storymapDao.findAll();
    }

    @Override
    public Storymap getStorymapById(final String id) {
        return storymapDao.findById(id).orElse(new Storymap());
    }

    @Override
    @Transactional
    public Storymap updateStorymapById(final Storymap storymap, final String id) {
        Storymap storymapDb = storymapDao.findById(id).orElse(new Storymap());
        BeanUtils.copyProperties(storymap, storymapDb);
        return storymapDao.save(storymapDb);
    }

    @Override
    @Transactional
    public void deleteStorymapById(final String id) {
        storymapDao.deleteById(id);
    }

    @Override
    public Boolean isStorymapExist(final String id) {
        return storymapDao.existsById(id);
    }

    @Override
    public Page<Storymap> getPageByCondition(final StorymapDTO storymapDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(storymapDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(storymapDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(storymapDTO.getUserid());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(storymapDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(storymapDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getReleaseid())) {
            Criteria criteria = Criteria.where("releaseid").is(storymapDTO.getReleaseid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").is(storymapDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (storymapDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").is(storymapDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, Storymap.class), pageable);
    }

    @Override
    public List<Storymap> getListByCondition(final StorymapDTO storymapDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(storymapDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(storymapDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(storymapDTO.getUserid());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(storymapDTO.getProductid())) {
            Criteria criteria = Criteria.where("productid").is(storymapDTO.getProductid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getReleaseid())) {
            Criteria criteria = Criteria.where("releaseid").is(storymapDTO.getReleaseid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(storymapDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(storymapDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (storymapDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").is(storymapDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Storymap.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Storymap mapToEntity(final StorymapDTO storymapDTO, final Storymap storymap) {
        BeanUtils.copyProperties(storymapDTO, storymap);
        return storymap;
    }

    public StorymapDTO mapToDTO(final Storymap storymap, final StorymapDTO storymapDTO) {
        BeanUtils.copyProperties(storymap, storymapDTO);
        return storymapDTO;
    }
}