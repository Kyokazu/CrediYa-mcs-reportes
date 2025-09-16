package co.com.crediya.sqs.listener;

import co.com.crediya.model.report.Report;
import co.com.crediya.sqs.listener.exception.NotAbleToHandleSQSResponseException;
import co.com.crediya.usecase.updatefromqueue.UpdateFromQueueUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.math.BigDecimal;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final UpdateFromQueueUseCase updateFromQueueUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        String body = message.body();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new NotAbleToHandleSQSResponseException(e.getMessage());
        }


        Report report = Report.builder()
                .id(node.get("loanId").asText())
                .email(node.get("email").asText())
                .monthlyPayment(new BigDecimal(node.get("monthlyPayment").asText()))
                .amount(new BigDecimal(node.get("amount").asText()))
                .duration(Long.parseLong(node.get("duration").asText()))
                .interestRate(Double.parseDouble(node.get("interestRate").asText()))
                .build();

        return updateFromQueueUseCase.saveReportFromLambda(report)
                .doOnSuccess(str -> log.info("Report message recieved from SQS with the ID {}", node.get("loanId").asText()))
                .then();
    }
}
