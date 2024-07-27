package pl.bolewski.credit_management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ApiResponse {

    private String message;
    private int status;
}
