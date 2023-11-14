package io.sobczykm.github.animalshelter.resource;

import io.sobczykm.github.animalshelter.domain.HttpResponse;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/employee")
@RequiredArgsConstructor
public class EmployeeResource {
    private final EmployeeService employeeService;

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(@RequestParam String email) {
        return ResponseEntity
                .ok()
                .body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("Employee", employeeService.getEmployeeByEmail(email)))
                                .message("Employee retrieved")
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }
}
