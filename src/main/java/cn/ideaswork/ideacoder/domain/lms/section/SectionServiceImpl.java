package cn.ideaswork.ideacoder.domain.lms.section;

import cn.ideaswork.ideacoder.domain.lms.vod.VodService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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
public class SectionServiceImpl implements SectionService {
    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private VodService vodService;

    @Override
    @Transactional
    public Section saveSection(final Section section) {
        return sectionDao.save(section);
    }

    @Override
    public List<Section> getAllSections() {
        return sectionDao.findAll();
    }

    @Override
    public Section getSectionById(final String id) {
        return sectionDao.findById(id).orElse(new Section());
    }

    @Override
    @Transactional
    public Section updateSectionById(final Section section, final String id) {
        Section sectionDb = sectionDao.findById(id).orElse(new Section());
        BeanUtils.copyProperties(section, sectionDb);
        return sectionDao.save(sectionDb);
    }

    @Override
    @Transactional
    public void deleteSectionById(final String id) throws TencentCloudSDKException {
        Section sectionDb = sectionDao.findById(id).orElse(new Section());
    if (StringUtils.isNotBlank(sectionDb.getVid())){
        vodService.deleteVodFile(sectionDb.getVid());
    }
        sectionDao.deleteById(id);
    }

    @Override
    public Boolean isSectionExist(final String id) {
        return sectionDao.existsById(id);
    }

    @Override
    public Page<Section> getPageByCondition(final SectionDTO sectionDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(sectionDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(sectionDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(sectionDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getCourseId())) {
            Criteria criteria = Criteria.where("courseId").is(sectionDTO.getCourseId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getWgurl())) {
            Criteria criteria = Criteria.where("wgurl").is(sectionDTO.getWgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").regex(sectionDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getVid())) {
            Criteria criteria = Criteria.where("vid").is(sectionDTO.getVid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getMinites())) {
            Criteria criteria = Criteria.where("minites").is(sectionDTO.getMinites());
            query.addCriteria(criteria);
        }
        if (sectionDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(sectionDTO.getPxh());
            query.addCriteria(criteria);
        }
        if (sectionDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").gte(sectionDTO.getCzsj());
            query.addCriteria(criteria);
        }
        query.with(Sort.by(Sort.Order.asc("pxh")));
        return this.listToPage(mongoTemplate.find(query, Section.class), pageable);
    }

    @Override
    public List<Section> getListByCondition(final SectionDTO sectionDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(sectionDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(sectionDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(sectionDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getCourseId())) {
            Criteria criteria = Criteria.where("courseId").is(sectionDTO.getCourseId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getWgurl())) {
            Criteria criteria = Criteria.where("wgurl").is(sectionDTO.getWgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").regex(sectionDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getVid())) {
            Criteria criteria = Criteria.where("vid").is(sectionDTO.getVid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(sectionDTO.getMinites())) {
            Criteria criteria = Criteria.where("minites").is(sectionDTO.getMinites());
            query.addCriteria(criteria);
        }
        if (sectionDTO.getPxh() != null) {
            Criteria criteria = Criteria.where("pxh").is(sectionDTO.getPxh());
            query.addCriteria(criteria);
        }
        if (sectionDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").gte(sectionDTO.getCzsj());
            query.addCriteria(criteria);
        }
        query.with(Sort.by(Sort.Order.asc("pxh")));
        return mongoTemplate.find(query, Section.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Section mapToEntity(final SectionDTO sectionDTO, final Section section) {
        BeanUtils.copyProperties(sectionDTO, section);
        return section;
    }

    public SectionDTO mapToDTO(final Section section, final SectionDTO sectionDTO) {
        BeanUtils.copyProperties(section, sectionDTO);
        return sectionDTO;
    }
}