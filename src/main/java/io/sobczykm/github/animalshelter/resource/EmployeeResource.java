package io.sobczykm.github.animalshelter.resource;

import io.sobczykm.github.animalshelter.domain.HttpResponse;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/employee")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(Authentication authentication) {
        return ResponseEntity
                .ok()
                .body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("Employee", employeeService.getEmployeeByEmail(authentication.getName())))
                                .message("Login success")
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }
}
