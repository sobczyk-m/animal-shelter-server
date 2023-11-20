package io.sobczykm.github.animalshelter.service;

import io.sobczykm.github.animalshelter.dto.EmployeeDTO;

public interface EmployeeService {
    EmployeeDTO getEmployeeByEmail(String email);

    EmployeeDTO getEmployeeById(Long id);
}
