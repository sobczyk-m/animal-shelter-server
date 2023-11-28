package io.sobczykm.github.animalshelter.unit.service;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.domain.Role;
import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import io.sobczykm.github.animalshelter.repository.EmployeeRepository;
import io.sobczykm.github.animalshelter.repository.RoleRepository;
import io.sobczykm.github.animalshelter.service.implementation.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplUnitTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private EmployeeServiceImpl underTest;
    private Employee employee;
    private Role role;

    @BeforeEach
    void setup() {
        employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmail("test@email.com");
        role = new Role();
    }

    @Test
    void getEmployeeByEmail_shouldCallRepositoryMethods_whenCalled() {
        when(employeeRepository.getEmployeeByEmail(anyString())).thenReturn(employee);
        when(roleRepository.getRoleByEmployeeId(anyLong())).thenReturn(role);
        underTest.getEmployeeByEmail(anyString());
        verify(employeeRepository, times(1)).getEmployeeByEmail(anyString());
        verify(roleRepository, times(1)).getRoleByEmployeeId(anyLong());
    }

    @Test
    void getEmployeeByEmail_shouldReturnEmployeeDtoWithMatchingEmail_whenEmailIsValid() {
        when(employeeRepository.getEmployeeByEmail(anyString())).thenReturn(employee);
        when(roleRepository.getRoleByEmployeeId(anyLong())).thenReturn(role);
        EmployeeDTO returnedEmployee = underTest.getEmployeeByEmail(anyString());
        assertEquals(employee.getEmail(), returnedEmployee.getEmail());
    }

    @Test
    void getEmployeeById_shouldCallRepositoryMethods_whenCalled() {
        when(employeeRepository.getEmployeeById(anyLong())).thenReturn(employee);
        when(roleRepository.getRoleByEmployeeId(anyLong())).thenReturn(role);
        underTest.getEmployeeById(anyLong());
        verify(employeeRepository, times(1)).getEmployeeById(anyLong());
        verify(roleRepository, times(1)).getRoleByEmployeeId(anyLong());
    }

    @Test
    void getEmployeeById_shouldReturnEmployeeDtoWithMatchingId_whenIdIsValid() {
        when(employeeRepository.getEmployeeById(anyLong())).thenReturn(employee);
        when(roleRepository.getRoleByEmployeeId(anyLong())).thenReturn(role);
        EmployeeDTO returnedEmployee = underTest.getEmployeeById(anyLong());
        assertEquals(employee.getEmployeeId(), returnedEmployee.getEmployeeId());
    }
}