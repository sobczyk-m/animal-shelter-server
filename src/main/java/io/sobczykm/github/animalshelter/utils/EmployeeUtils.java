package io.sobczykm.github.animalshelter.utils;

import io.sobczykm.github.animalshelter.domain.EmployeePrincipal;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import org.springframework.security.core.Authentication;

public class EmployeeUtils {

    public static EmployeeDTO getAuthenticatedEmployee(Authentication authentication) {
        return ((EmployeeDTO) authentication.getPrincipal());
    }

    public static EmployeeDTO getLoggedInEmployee(Authentication authentication) {
        return ((EmployeePrincipal) authentication.getPrincipal()).getEmployee();
    }
}
