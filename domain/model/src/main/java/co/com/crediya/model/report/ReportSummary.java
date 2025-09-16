package co.com.crediya.model.report;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportSummary {

    private long quantity;
    private BigDecimal amountBorrowed;
}
