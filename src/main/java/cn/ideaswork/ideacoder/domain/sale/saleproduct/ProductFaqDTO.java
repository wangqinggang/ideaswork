package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFaqDTO {

    @Schema(description = "问题")
    private String question;

    @Schema(description = "解答")
    private String answer;
}
