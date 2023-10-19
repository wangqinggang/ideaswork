package cn.ideaswork.ideacoder.domain.pm.releaseplan;

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
public class ReleaseplanServiceImpl implements ReleaseplanService {
  @Autowired
  private ReleaseplanDao releaseplanDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  @Transactional
  public Releaseplan saveReleaseplan(final Releaseplan releaseplan) {
    return releaseplanDao.save(releaseplan);
  }

  @Override
  public List<Releaseplan> getAllReleaseplans() {
    return releaseplanDao.findAll();
  }

  @Override
  public Releaseplan getReleaseplanById(final String id) {
    return releaseplanDao.findById(id).orElse(new Releaseplan());
  }

  @Override
  @Transactional
  public Releaseplan updateReleaseplanById(final Releaseplan releaseplan, final String id) {
    Releaseplan releaseplanDb = releaseplanDao.findById(id).orElse(new Releaseplan());
    BeanUtils.copyProperties(releaseplan,releaseplanDb);
    return releaseplanDao.save(releaseplanDb);
  }

  @Override
  @Transactional
  public void deleteReleaseplanById(final String id) {
    releaseplanDao.deleteById(id);
  }

  @Override
  public Boolean isReleaseplanExist(final String id) {
    return releaseplanDao.existsById(id);
  }

  @Override
  public Page<Releaseplan> getPageByCondition(final ReleaseplanDTO releaseplanDTO,
      final Pageable pageable) {
    Query query = new Query();
    if(!StringUtils.isEmpty(releaseplanDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(releaseplanDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getUserid())){
    	Criteria criteria =  Criteria.where("userid").is(releaseplanDTO.getUserid()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getProductid())){
    	Criteria criteria =  Criteria.where("productid").is(releaseplanDTO.getProductid()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getVersion())){
    	Criteria criteria =  Criteria.where("version").is(releaseplanDTO.getVersion()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getContent())){
    	Criteria criteria =  Criteria.where("content").is(releaseplanDTO.getContent()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getStarttime()!=null){
    	Criteria criteria =  Criteria.where("starttime").is(releaseplanDTO.getStarttime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getExpectedendtime()!=null){
    	Criteria criteria =  Criteria.where("expectedendtime").is(releaseplanDTO.getExpectedendtime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getActualreleasetime()!=null){
    	Criteria criteria =  Criteria.where("actualreleasetime").is(releaseplanDTO.getActualreleasetime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getCreatetime()!=null){
    	Criteria criteria =  Criteria.where("createtime").is(releaseplanDTO.getCreatetime()); 
    	query.addCriteria(criteria);
    }
    return this.listToPage(mongoTemplate.find(query,Releaseplan.class),pageable);
  }

  @Override
  public List<Releaseplan> getListByCondition(final ReleaseplanDTO releaseplanDTO) {
    Query query = new Query();
    if(!StringUtils.isEmpty(releaseplanDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(releaseplanDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getUserid())){
    	Criteria criteria =  Criteria.where("userid").is(releaseplanDTO.getUserid()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getProductid())){
    	Criteria criteria =  Criteria.where("productid").is(releaseplanDTO.getProductid()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getVersion())){
    	Criteria criteria =  Criteria.where("version").is(releaseplanDTO.getVersion()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(releaseplanDTO.getContent())){
    	Criteria criteria =  Criteria.where("content").is(releaseplanDTO.getContent()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getStarttime()!=null){
    	Criteria criteria =  Criteria.where("starttime").is(releaseplanDTO.getStarttime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getExpectedendtime()!=null){
    	Criteria criteria =  Criteria.where("expectedendtime").is(releaseplanDTO.getExpectedendtime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getActualreleasetime()!=null){
    	Criteria criteria =  Criteria.where("actualreleasetime").is(releaseplanDTO.getActualreleasetime()); 
    	query.addCriteria(criteria);
    }
    if(releaseplanDTO.getCreatetime()!=null){
    	Criteria criteria =  Criteria.where("createtime").is(releaseplanDTO.getCreatetime()); 
    	query.addCriteria(criteria);
    }
    return mongoTemplate.find(query, Releaseplan.class);
  }

  public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
    int start = (int)pageable.getOffset();
    int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
    return new PageImpl<T>(list.subList(start, end), pageable, list.size());
  }

  public Releaseplan mapToEntity(final ReleaseplanDTO releaseplanDTO,
      final Releaseplan releaseplan) {
    BeanUtils.copyProperties(releaseplanDTO,releaseplan);
    return releaseplan;
  }

  public ReleaseplanDTO mapToDTO(final Releaseplan releaseplan,
      final ReleaseplanDTO releaseplanDTO) {
    BeanUtils.copyProperties(releaseplan,releaseplanDTO);
    return releaseplanDTO;
  }
}