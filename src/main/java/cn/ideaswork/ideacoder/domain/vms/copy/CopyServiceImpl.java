package cn.ideaswork.ideacoder.domain.vms.copy;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.vms.script.Script;
import cn.ideaswork.ideacoder.domain.vms.script.ScriptDTO;
import cn.ideaswork.ideacoder.domain.vms.script.ScriptService;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.List;
import java.util.stream.Collectors;

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
public class CopyServiceImpl implements CopyService {
    @Autowired
    private CopyDao copyDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ScriptService scriptService;

    @Override
    @Transactional
    public Copy saveCopy(final Copy copy) {
        return copyDao.save(copy);
    }

    @Override
    public List<Copy> getAllCopys() {
        return copyDao.findAll();
    }

    @Override
    public Copy getCopyById(final String id) {
        return copyDao.findById(id).orElse(new Copy());
    }

    @Override
    @Transactional
    public Copy updateCopyById(final Copy copy, final String id) throws Exception {
        Copy copyDb = copyDao.findById(id).orElse(new Copy());
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        copy.setUserId(userId);
        if (StringUtils.isNotBlank(copyDb.getUserId())) {
            // 更新文案数量
            ScriptDTO scriptDTO = new ScriptDTO();
            scriptDTO.setUserId(userId);
            scriptDTO.setCopyId(id);
            List<Script> scriptDTOListByCondition = scriptService.getListByCondition(scriptDTO);
            int size = scriptDTOListByCondition.size();
            if (size > 0) {
                copyDb.setScriptNum(size);
            }
            // 更新文案
            if (copyDb.getUserId().equals(userId)) {
                BeanUtils.copyProperties(copy, copyDb);
                return copyDao.save(copyDb);
            } else {
                throw new Exception("只能修改自己的文案！");
            }
        } else {
            throw new Exception("未找到文案！");
        }
    }

    @Override
    @Transactional
    public void deleteCopyById(final String id) throws Exception {
        Copy copyDb = copyDao.findById(id).orElse(new Copy());
        User loginUser = SysTools.getLoginUser();
        String userId = loginUser.getId();
        if (copyDb.getUserId().equals(userId)) {
            copyDao.deleteById(id);
        } else {
            throw new Exception("只能修改自己的文案！");
        }
    }

    @Override
    public Boolean isCopyExist(final String id) {
        return copyDao.existsById(id);
    }

    @Override
    public Page<Copy> getPageByCondition(final CopyDTO copyDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(copyDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(copyDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(copyDTO.getUserId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getTopicId())) {
            Criteria criteria = Criteria.where("topicId").is(copyDTO.getTopicId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getTopicName())) {
            Criteria criteria = Criteria.where("topicName").regex(copyDTO.getTopicName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getTitle())) {
            Criteria criteria = Criteria.where("title").regex(copyDTO.getTitle());
            query.addCriteria(criteria);
        }
        if (copyDTO.getIntro() != null) {
            Criteria criteria = Criteria.where("intro").regex(copyDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(copyDTO.getContent());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getWgurl())) {
            Criteria criteria = Criteria.where("wgurl").is(copyDTO.getWgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getStatus())) {
            if (!copyDTO.getStatus().equals("全部")) {
                Criteria criteria = Criteria.where("status").is(copyDTO.getStatus());
                query.addCriteria(criteria);
            }
        }
        if (copyDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").is(copyDTO.getCjsj());
            query.addCriteria(criteria);
        }
        if (copyDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(copyDTO.getPxh());
            query.addCriteria(criteria);
        }
        Sort sort = pageable.getSort();
        query.with(sort);
        List<Copy> copies = mongoTemplate.find(query, Copy.class);
        copies=copies.stream().filter(one->one.getStatus()!=null?!one.getStatus().equals("回收站"):false).collect(Collectors.toList());
        return this.listToPage(copies, pageable);
    }

    @Override
    public List<Copy> getListByCondition(final CopyDTO copyDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(copyDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(copyDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getUserId())) {
            Criteria criteria = Criteria.where("userId").is(copyDTO.getUserId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getTopicId())) {
            Criteria criteria = Criteria.where("topicId").is(copyDTO.getTopicId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getTopicName())) {
            Criteria criteria = Criteria.where("topicName").regex(copyDTO.getTopicName());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(copyDTO.getTitle())) {
            Criteria criteria = Criteria.where("title").regex(copyDTO.getTitle());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(copyDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(copyDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getContent())) {
            Criteria criteria = Criteria.where("content").regex(copyDTO.getContent());
            query.addCriteria(criteria);
        }

        if (!StringUtils.isEmpty(copyDTO.getWgurl())) {
            Criteria criteria = Criteria.where("wgurl").is(copyDTO.getWgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(copyDTO.getStatus())) {
            Criteria criteria = Criteria.where("status").is(copyDTO.getStatus());
            query.addCriteria(criteria);
        }
        if (copyDTO.getCjsj() != null) {
            Criteria criteria = Criteria.where("cjsj").gte(copyDTO.getCjsj());
            query.addCriteria(criteria);
        }
        if (copyDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(copyDTO.getPxh());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Copy.class);
    }

    @Override
    public Integer getCopyNumByUserId(String id) {
        return copyDao.countByUserId(id);
    }

    @Override
    public Integer getActrualCopyCountByTopicId(String topicId) {
        return copyDao.countByTopicId(topicId);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Copy mapToEntity(final CopyDTO copyDTO, final Copy copy) {
        BeanUtils.copyProperties(copyDTO, copy);
        return copy;
    }

    public CopyDTO mapToDTO(final Copy copy, final CopyDTO copyDTO) {
        BeanUtils.copyProperties(copy, copyDTO);
        return copyDTO;
    }
}