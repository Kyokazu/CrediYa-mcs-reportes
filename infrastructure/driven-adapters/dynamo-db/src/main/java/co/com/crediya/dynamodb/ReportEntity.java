package co.com.crediya.dynamodb;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;


@Setter
@Getter
@DynamoDbBean
public class ReportEntity {

    private String id;
    private String email;
    private BigDecimal monthlyPayment;
    private BigDecimal amount;
    private Long duration;
    private Double interestRate;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

}
