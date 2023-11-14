package io.sobczykm.github.animalshelter.dtomapper;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import org.springframework.beans.BeanUtils;

public class EmployeeDTOMapper {
    public static EmployeeDTO fromEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    public static Employee toEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
