package co.com.crediya.api.openapi;

import co.com.crediya.api.dto.ApiErrorDTO;
import co.com.crediya.model.report.ReportSummary;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class ReportApiDoc {

    public Builder getReportDoc(Builder builder) {
        return builder
                .operationId("getReport")
                .description("Generate a report of approved loans")
                .tag("Report")
                .response(responseBuilder()
                        .responseCode("200")
                        .description("Report generated successfully")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ReportSummary.class))))
                .response(responseBuilder()
                        .responseCode("400")
                        .description("Business error: NoReportsFoundException or ApprovedReportNotSavedException")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ApiErrorDTO.class))))
                .response(responseBuilder()
                        .responseCode("401")
                        .description("Unauthorized: NotAdminRoleException")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ApiErrorDTO.class))))
                .response(responseBuilder()
                        .responseCode("403")
                        .description("Authorization error: MissingInvalidAuthHeaderException or InvalidTokenException")
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(ApiErrorDTO.class))));
    }


}