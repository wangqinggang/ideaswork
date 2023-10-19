package cn.ideaswork.ideacoder.domain.lms.room;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.Integer;
import java.lang.String;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ROOM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "教室")
@Accessors(chain = true)
public final class Room {
    @Id
    @Schema(description = "教室id")
    private String id;

    @Schema(description = "教室名称")
    private String name;

    @Schema(description = "人数")
    private Integer num;

    @Schema(description = "直播id")
    private String liveid;
}
