package io.sobczykm.github.animalshelter.configuration;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.domain.EmployeePrincipal;
import io.sobczykm.github.animalshelter.repository.EmployeeRepository;
import io.sobczykm.github.animalshelter.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomEmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.getEmployeeByEmail(email);
        if (employee == null) {
            log.error("Employee not found in the database");
            throw new UsernameNotFoundException("Employee not found in the database");
        } else {
            log.info("Employee found in the database");
            return new EmployeePrincipal(employee, roleRepository.getRoleByEmployeeId(employee.getEmployeeId()));
        }
    }
}
