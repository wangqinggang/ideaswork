package cn.ideaswork.ideacoder.domain.lms.room;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "教室 API")
@CrossOrigin
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    RoomService roomService;

    @PostMapping
    @ApiOperation("添加教室")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Room saveRoom(@RequestBody Room room) {
        room.setId(UUID.randomUUID().toString());
        return roomService.saveRoom(room);
    }

    @GetMapping
    @ApiOperation("获取 教室列表")
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 教室")
    public Room getRoom(@PathVariable("id") final String id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新教室")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Room updateRoom(@RequestBody Room room, @PathVariable("id") final String id) {
        return roomService.updateRoomById(room, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键 id 删除教室")
    public void deleteRoomById(@PathVariable("id") final String id) {
        roomService.deleteRoomById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看教室是否存在")
    public Boolean isExistRoom(@PathVariable("id") final String id) {
        return roomService.isRoomExist(id);
    }

    @GetMapping("/getPageList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("分页条件查询")
    public Page<Room> getPageByCondition(RoomDTO roomDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return roomService.getPageByCondition(roomDTO, pageable);
    }
}