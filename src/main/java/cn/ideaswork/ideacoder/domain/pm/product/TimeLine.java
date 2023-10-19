package cn.ideaswork.ideacoder.domain.pm.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLine {
    @Schema(description = "里程碑状态 0 预计  1 达到  2 超出预期")
    private String status;

    @Schema(description = "里程碑")
    private String content;

    @Schema(description = "时间")
    private LocalDate time;

}
