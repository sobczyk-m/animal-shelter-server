package io.sobczykm.github.animalshelter.repository;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.exception.ApiException;
import io.sobczykm.github.animalshelter.repository.implementation.EmployeeRepositoryImpl;
import io.sobczykm.github.animalshelter.rowmapper.EmployeeRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static io.sobczykm.github.animalshelter.constant.Constants.GENERIC_API_EXCEPTION_MESSAGE;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryImplTest {
    @Mock
    private NamedParameterJdbcTemplate jdbc;
    @InjectMocks
    private EmployeeRepositoryImpl underTest;
    private String email;
    private Long id;
    private Employee employee;

    @BeforeEach
    void setup() {
        id = 1L;
        email = "test@email.com";
        employee = new Employee();
        employee.setEmployeeId(id);
        employee.setEmail(email);
    }

    @Test
    void getEmployeeByEmail_shouldReturnEmployeeWithMatchingEmail_whenEmailIsValid() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("email", email)),
                any(EmployeeRowMapper.class))).thenReturn(employee);
        Employee result = underTest.getEmployeeByEmail(email);
        assertEquals(email, result.getEmail());
    }

    @Test
    void getEmployeeByEmail_shouldThrowApiException_whenNoEmployeeFound() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("email", email)),
                any(EmployeeRowMapper.class))).thenThrow(EmptyResultDataAccessException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getEmployeeByEmail(email));
        assertEquals("No employee found by email", apiException.getMessage());
    }

    @Test
    void getEmployeeByEmail_shouldThrowApiException_whenUnexpectedErrorOccurs() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("email", email)),
                any(EmployeeRowMapper.class))).thenThrow(RuntimeException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getEmployeeByEmail(email));
        assertEquals(GENERIC_API_EXCEPTION_MESSAGE, apiException.getMessage());
    }

    @Test
    void getEmployeeById_shouldReturnEmployeeWithMatchingId_whenIdIsValid() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("employeeId", id)),
                any(EmployeeRowMapper.class))).thenReturn(employee);
        Employee result = underTest.getEmployeeById(id);
        assertEquals(id, result.getEmployeeId());
    }

    @Test
    void getEmployeeById_shouldThrowApiException_whenNoEmployeeFound() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("employeeId", id)),
                any(EmployeeRowMapper.class))).thenThrow(EmptyResultDataAccessException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getEmployeeById(id));
        assertEquals(format("No employee found by id: %d", id), apiException.getMessage());
    }

    @Test
    void getEmployeeById_shouldThrowApiException_whenUnexpectedErrorOccurs() {
        when(jdbc.queryForObject(anyString(), eq(Map.of("employeeId", id)),
                any(EmployeeRowMapper.class))).thenThrow(RuntimeException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getEmployeeById(id));
        assertEquals(GENERIC_API_EXCEPTION_MESSAGE, apiException.getMessage());
    }
}