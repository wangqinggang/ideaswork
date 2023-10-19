package cn.ideaswork.ideacoder.domain.lms.room;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    @Schema(description = "教室id")
    private String id;

    @Schema(description = "教室名称")
    private String name;

    @Schema(description = "人数")
    private Integer num;

    @Schema(description = "直播id")
    private String liveid;
}

