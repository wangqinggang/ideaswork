package cn.ideaswork.ideacoder.domain.lms.room;

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
public class RoomServiceImpl implements RoomService {
  @Autowired
  private RoomDao roomDao;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  @Transactional
  public Room saveRoom(final Room room) {
    return roomDao.save(room);
  }

  @Override
  public List<Room> getAllRooms() {
    return roomDao.findAll();
  }

  @Override
  public Room getRoomById(final String id) {
    return roomDao.findById(id).orElse(new Room());
  }

  @Override
  @Transactional
  public Room updateRoomById(final Room room, final String id) {
    Room roomDb = roomDao.findById(id).orElse(new Room());
    BeanUtils.copyProperties(room,roomDb);
    return roomDao.save(roomDb);
  }

  @Override
  @Transactional
  public void deleteRoomById(final String id) {
    roomDao.deleteById(id);
  }

  @Override
  public Boolean isRoomExist(final String id) {
    return roomDao.existsById(id);
  }

  @Override
  public Page<Room> getPageByCondition(final RoomDTO roomDTO, final Pageable pageable) {
    Query query = new Query();
    if(!StringUtils.isEmpty(roomDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(roomDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(roomDTO.getName())){
    	Criteria criteria =  Criteria.where("name").is(roomDTO.getName()); 
    	query.addCriteria(criteria);
    }
    if(roomDTO.getNum()!=null){
    	Criteria criteria =  Criteria.where("num").is(roomDTO.getNum()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(roomDTO.getLiveid())){
    	Criteria criteria =  Criteria.where("liveid").is(roomDTO.getLiveid()); 
    	query.addCriteria(criteria);
    }
    return this.listToPage(mongoTemplate.find(query,Room.class),pageable);
  }

  @Override
  public List<Room> getListByCondition(final RoomDTO roomDTO) {
    Query query = new Query();
    if(!StringUtils.isEmpty(roomDTO.getId())){
    	Criteria criteria =  Criteria.where("id").is(roomDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(roomDTO.getId()!=null){
    	Criteria criteria =  Criteria.where("id").is(roomDTO.getId()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(roomDTO.getName())){
    	Criteria criteria =  Criteria.where("name").is(roomDTO.getName()); 
    	query.addCriteria(criteria);
    }
    if(roomDTO.getName()!=null){
    	Criteria criteria =  Criteria.where("name").is(roomDTO.getName()); 
    	query.addCriteria(criteria);
    }
    if(roomDTO.getNum()!=null){
    	Criteria criteria =  Criteria.where("num").is(roomDTO.getNum()); 
    	query.addCriteria(criteria);
    }
    if(!StringUtils.isEmpty(roomDTO.getLiveid())){
    	Criteria criteria =  Criteria.where("liveid").is(roomDTO.getLiveid()); 
    	query.addCriteria(criteria);
    }
    if(roomDTO.getLiveid()!=null){
    	Criteria criteria =  Criteria.where("liveid").is(roomDTO.getLiveid()); 
    	query.addCriteria(criteria);
    }
    return mongoTemplate.find(query, Room.class);
  }

  public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
    int start = (int)pageable.getOffset();
    int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
    return new PageImpl<T>(list.subList(start, end), pageable, list.size());
  }

  public Room mapToEntity(final RoomDTO roomDTO, final Room room) {
    BeanUtils.copyProperties(roomDTO,room);
    return room;
  }

  public RoomDTO mapToDTO(final Room room, final RoomDTO roomDTO) {
    BeanUtils.copyProperties(room,roomDTO);
    return roomDTO;
  }
}