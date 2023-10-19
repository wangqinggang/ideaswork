package cn.ideaswork.ideacoder.domain.pm.persona;

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
public class PersonaServiceImpl implements PersonaService {
  @Autowired
  private PersonaDao personaDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  @Transactional
  public Persona savePersona(final Persona persona) {
    return personaDao.save(persona);
  }

  @Override
  public List<Persona> getAllPersonas() {
    return personaDao.findAll();
  }

  @Override
  public Persona getPersonaById(final String id) {
    return personaDao.findById(id).orElse(new Persona());
  }

  @Override
  @Transactional
  public Persona updatePersonaById(final Persona persona, final String id) {
    Persona personaDb = personaDao.findById(id).orElse(new Persona());
    BeanUtils.copyProperties(persona,personaDb);
    return personaDao.save(personaDb);
  }

  @Override
  @Transactional
  public void deletePersonaById(final String id) {
    personaDao.deleteById(id);
  }

  @Override
  public Boolean isPersonaExist(final String id) {
    return personaDao.existsById(id);
  }

  @Override
  public Page<Persona> getPageByCondition(final PersonaDTO personaDTO, final Pageable pageable) {
    Query query = new Query();
    if(!StringUtils.isEmpty(personaDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(personaDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getName())){
    	Criteria criteria =  Criteria.where("name").regex(personaDTO.getName());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getImageUrl())){
    	Criteria criteria =  Criteria.where("imageUrl").is(personaDTO.getImageUrl()); 
    	query.addCriteria(criteria);
    }
    if(personaDTO.getAge()!=null){
    	Criteria criteria =  Criteria.where("age").is(personaDTO.getAge()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getProfession())){
    	Criteria criteria =  Criteria.where("profession").regex(personaDTO.getProfession());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getTags())){
    	Criteria criteria =  Criteria.where("tags").regex(personaDTO.getTags());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getQuotations())){
    	Criteria criteria =  Criteria.where("quotations").regex(personaDTO.getQuotations());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getEducation())){
    	Criteria criteria =  Criteria.where("education").regex(personaDTO.getEducation());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getWorkingStatus())){
    	Criteria criteria =  Criteria.where("workingStatus").regex(personaDTO.getWorkingStatus());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getFamilyStatus())){
    	Criteria criteria =  Criteria.where("familyStatus").regex(personaDTO.getFamilyStatus());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getEnvironment())){
    	Criteria criteria =  Criteria.where("environment").regex(personaDTO.getEnvironment());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getHobbies())){
    	Criteria criteria =  Criteria.where("hobbies").regex(personaDTO.getHobbies());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToThings())){
    	Criteria criteria =  Criteria.where("attitudeToThings").regex(personaDTO.getAttitudeToThings());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getTargets())){
    	Criteria criteria =  Criteria.where("targets").regex(personaDTO.getTargets());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfInternet())){
    	Criteria criteria =  Criteria.where("usageLevelOfInternet").regex(personaDTO.getUsageLevelOfInternet());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfHardware())){
    	Criteria criteria =  Criteria.where("usageLevelOfHardware").regex(personaDTO.getUsageLevelOfHardware());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfApplication())){
    	Criteria criteria =  Criteria.where("usageLevelOfApplication").regex(personaDTO.getUsageLevelOfApplication());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToNewTech())){
    	Criteria criteria =  Criteria.where("attitudeToNewTech").regex(personaDTO.getAttitudeToNewTech());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getOperantLevelToHardware())){
    	Criteria criteria =  Criteria.where("operantLevelToHardware").regex(personaDTO.getOperantLevelToHardware());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getRelationshipWithTargetProduct())){
    	Criteria criteria =  Criteria.where("relationshipWithTargetProduct").regex(personaDTO.getRelationshipWithTargetProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToTargetProduct())){
    	Criteria criteria =  Criteria.where("attitudeToTargetProduct").regex(personaDTO.getAttitudeToTargetProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageStatusOfCompetingProduct())){
    	Criteria criteria =  Criteria.where("usageStatusOfCompetingProduct").regex(personaDTO.getUsageStatusOfCompetingProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAccessoriesAndPeripheral())){
    	Criteria criteria =  Criteria.where("accessoriesAndPeripheral").regex(personaDTO.getAccessoriesAndPeripheral());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getSituationOfUsingProduct())){
    	Criteria criteria =  Criteria.where("situationOfUsingProduct").regex(personaDTO.getSituationOfUsingProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getFrequentlyUsedFunction())){
    	Criteria criteria =  Criteria.where("frequentlyUsedFunction").regex(personaDTO.getFrequentlyUsedFunction());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getCurrentDifficultiesAndObstacles())){
    	Criteria criteria =  Criteria.where("currentDifficultiesAndObstacles").regex(personaDTO.getCurrentDifficultiesAndObstacles());
    	query.addCriteria(criteria);
    }
    return this.listToPage(mongoTemplate.find(query,Persona.class),pageable);
  }

  @Override
  public List<Persona> getListByCondition(final PersonaDTO personaDTO) {
    Query query = new Query();
    if(!StringUtils.isEmpty(personaDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(personaDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getName())){
    	Criteria criteria =  Criteria.where("name").regex(personaDTO.getName());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getImageUrl())){
    	Criteria criteria =  Criteria.where("imageUrl").is(personaDTO.getImageUrl()); 
    	query.addCriteria(criteria);
    }
    if(personaDTO.getAge()!=null){
    	Criteria criteria =  Criteria.where("age").is(personaDTO.getAge()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getProfession())){
    	Criteria criteria =  Criteria.where("profession").regex(personaDTO.getProfession());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getTags())){
    	Criteria criteria =  Criteria.where("tags").regex(personaDTO.getTags());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getQuotations())){
    	Criteria criteria =  Criteria.where("quotations").regex(personaDTO.getQuotations());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getEducation())){
    	Criteria criteria =  Criteria.where("education").regex(personaDTO.getEducation());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getWorkingStatus())){
    	Criteria criteria =  Criteria.where("workingStatus").regex(personaDTO.getWorkingStatus());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getFamilyStatus())){
    	Criteria criteria =  Criteria.where("familyStatus").regex(personaDTO.getFamilyStatus());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getEnvironment())){
    	Criteria criteria =  Criteria.where("environment").regex(personaDTO.getEnvironment());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getHobbies())){
    	Criteria criteria =  Criteria.where("hobbies").regex(personaDTO.getHobbies());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToThings())){
    	Criteria criteria =  Criteria.where("attitudeToThings").regex(personaDTO.getAttitudeToThings());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getTargets())){
    	Criteria criteria =  Criteria.where("targets").regex(personaDTO.getTargets());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfInternet())){
    	Criteria criteria =  Criteria.where("usageLevelOfInternet").regex(personaDTO.getUsageLevelOfInternet());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfHardware())){
    	Criteria criteria =  Criteria.where("usageLevelOfHardware").regex(personaDTO.getUsageLevelOfHardware());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageLevelOfApplication())){
    	Criteria criteria =  Criteria.where("usageLevelOfApplication").regex(personaDTO.getUsageLevelOfApplication());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToNewTech())){
    	Criteria criteria =  Criteria.where("attitudeToNewTech").regex(personaDTO.getAttitudeToNewTech());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getOperantLevelToHardware())){
    	Criteria criteria =  Criteria.where("operantLevelToHardware").regex(personaDTO.getOperantLevelToHardware());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getRelationshipWithTargetProduct())){
    	Criteria criteria =  Criteria.where("relationshipWithTargetProduct").regex(personaDTO.getRelationshipWithTargetProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAttitudeToTargetProduct())){
    	Criteria criteria =  Criteria.where("attitudeToTargetProduct").regex(personaDTO.getAttitudeToTargetProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getUsageStatusOfCompetingProduct())){
    	Criteria criteria =  Criteria.where("usageStatusOfCompetingProduct").regex(personaDTO.getUsageStatusOfCompetingProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getAccessoriesAndPeripheral())){
    	Criteria criteria =  Criteria.where("accessoriesAndPeripheral").regex(personaDTO.getAccessoriesAndPeripheral());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getSituationOfUsingProduct())){
    	Criteria criteria =  Criteria.where("situationOfUsingProduct").regex(personaDTO.getSituationOfUsingProduct());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getFrequentlyUsedFunction())){
    	Criteria criteria =  Criteria.where("frequentlyUsedFunction").regex(personaDTO.getFrequentlyUsedFunction());
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(personaDTO.getCurrentDifficultiesAndObstacles())){
    	Criteria criteria =  Criteria.where("currentDifficultiesAndObstacles").regex(personaDTO.getCurrentDifficultiesAndObstacles());
    	query.addCriteria(criteria);
    }
    return mongoTemplate.find(query, Persona.class);
  }

  public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
    int start = (int)pageable.getOffset();
    int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
    return new PageImpl<T>(list.subList(start, end), pageable, list.size());
  }

  public Persona mapToEntity(final PersonaDTO personaDTO, final Persona persona) {
    BeanUtils.copyProperties(personaDTO,persona);
    return persona;
  }

  public PersonaDTO mapToDTO(final Persona persona, final PersonaDTO personaDTO) {
    BeanUtils.copyProperties(persona,personaDTO);
    return personaDTO;
  }
}