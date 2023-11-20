package io.sobczykm.github.animalshelter.resource;

import io.sobczykm.github.animalshelter.domain.EmployeePrincipal;
import io.sobczykm.github.animalshelter.domain.HttpResponse;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import io.sobczykm.github.animalshelter.dtomapper.EmployeeDTOMapper;
import io.sobczykm.github.animalshelter.form.LoginForm;
import io.sobczykm.github.animalshelter.provider.TokenProvider;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import io.sobczykm.github.animalshelter.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static io.sobczykm.github.animalshelter.utils.EmployeeUtils.getAuthenticatedEmployee;
import static io.sobczykm.github.animalshelter.utils.EmployeeUtils.getLoggedInEmployee;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@RestController
@RequestMapping(path = "/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeResource {
    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final TokenProvider tokenProvider;

    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> profile(Authentication authentication) {
        EmployeeDTO employeeDTO = getAuthenticatedEmployee(authentication);
        return ResponseEntity
                .ok()
                .body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("Employee", employeeDTO))
                                .message("Employee retrieved")
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
        EmployeeDTO employee = authenticate(loginForm.getEmail(), loginForm.getPassword());
        return ResponseEntity
                .ok()
                .body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of(
                                        "Employee", employeeService.getEmployeeByEmail(loginForm.getEmail()),
                                        "Token", tokenProvider.createAccessToken(getEmployeePrincipal(employee))))
                                .message("Login success")
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    private EmployeePrincipal getEmployeePrincipal(EmployeeDTO employee) {
        return new EmployeePrincipal(
                EmployeeDTOMapper.toEmployee(employeeService.getEmployeeByEmail(employee.getEmail())),
                roleService.getRoleByEmployeeId(employee.getEmployeeId()));
    }

    private EmployeeDTO authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return getLoggedInEmployee(authentication);
    }
}
