package io.sobczykm.github.animalshelter.service.implementation;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import io.sobczykm.github.animalshelter.dtomapper.EmployeeDTOMapper;
import io.sobczykm.github.animalshelter.repository.EmployeeRepository;
import io.sobczykm.github.animalshelter.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO getEmployeeByEmail(String email) {
        return mapToEmployeeDTO(employeeRepository.getEmployeeByEmail(email));
    }

    private EmployeeDTO mapToEmployeeDTO(Employee employee) {
        return EmployeeDTOMapper.fromEmployee(employee);
    }
}
