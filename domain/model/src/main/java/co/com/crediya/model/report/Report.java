package co.com.crediya.model.report;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {

    private String id;
    private String email;
    private BigDecimal monthlyPayment;
    private BigDecimal amount;
    private Long duration;
    private Double interestRate;
}
