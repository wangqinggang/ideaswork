package cn.ideaswork.ideacoder.domain.lms.room;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
  Room saveRoom(Room room);

  List<Room> getAllRooms();

  Room getRoomById(final String id);

  Room updateRoomById(Room room, final String id);

  void deleteRoomById(final String id);

  Boolean isRoomExist(final String id);

  Page<Room> getPageByCondition(RoomDTO roomDTO, Pageable pageable);

  List<Room> getListByCondition(RoomDTO roomDTO);
}
