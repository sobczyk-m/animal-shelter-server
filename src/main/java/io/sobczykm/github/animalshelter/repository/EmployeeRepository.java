package io.sobczykm.github.animalshelter.repository;

import io.sobczykm.github.animalshelter.domain.Employee;

public interface EmployeeRepository {
    Employee getEmployeeByEmail(String email);

    Employee getEmployeeById(Long id);
}
