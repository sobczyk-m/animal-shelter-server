package io.sobczykm.github.animalshelter.resource;

import io.sobczykm.github.animalshelter.domain.EmployeePrincipal;
import io.sobczykm.github.animalshelter.domain.HttpResponse;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import io.sobczykm.github.animalshelter.dtomapper.EmployeeDTOMapper;
import io.sobczykm.github.animalshelter.form.LoginForm;
import io.sobczykm.github.animalshelter.provider.TokenProvider;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import io.sobczykm.github.animalshelter.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
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

import static io.sobczykm.github.animalshelter.constant.Constants.TOKEN_PREFIX;
import static io.sobczykm.github.animalshelter.utils.EmployeeUtils.getAuthenticatedEmployee;
import static io.sobczykm.github.animalshelter.utils.EmployeeUtils.getLoggedInEmployee;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
                                .build());
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
                                        "employee", employeeService.getEmployeeByEmail(loginForm.getEmail()),
                                        "access_token", tokenProvider.createAccessToken(getEmployeePrincipal(employee)),
                                        "refresh_token", tokenProvider.createRefreshToken(getEmployeePrincipal(employee))))
                                .message("Login success")
                                .status(OK)
                                .statusCode(OK.value())
                                .build());
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderAndTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            EmployeeDTO employee = employeeService.getEmployeeById(Long.valueOf(tokenProvider.getSubject(token)));
            return ResponseEntity
                    .ok()
                    .body(
                            HttpResponse.builder()
                                    .timeStamp(LocalDateTime.now().toString())
                                    .data(Map.of(
                                            "employee", employee,
                                            "access_token", tokenProvider.createAccessToken(getEmployeePrincipal(employee)),
                                            "refresh_token", token))
                                    .message("Token refreshed")
                                    .status(OK)
                                    .statusCode(OK.value())
                                    .build());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(
                            HttpResponse.builder()
                                    .timeStamp(now().toString())
                                    .message("Refresh Token missing or invalid")
                                    .status(BAD_REQUEST)
                                    .statusCode(BAD_REQUEST.value())
                                    .build());
        }
    }

    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()),
                Long.valueOf(tokenProvider.getSubject(request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()))));
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
