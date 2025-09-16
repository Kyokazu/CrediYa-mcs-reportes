package co.com.crediya.model.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserTokenInfo {
    private String token;
    private String email;
    private String role;
}