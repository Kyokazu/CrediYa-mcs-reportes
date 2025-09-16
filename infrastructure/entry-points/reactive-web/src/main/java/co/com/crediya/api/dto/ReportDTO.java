package co.com.crediya.api.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {

    private long quantity;
    private BigDecimal amountBorrowed;
}
