package io.sobczykm.github.animalshelter.repository;

import io.sobczykm.github.animalshelter.domain.Role;
import io.sobczykm.github.animalshelter.exception.ApiException;
import io.sobczykm.github.animalshelter.repository.implementation.RoleRepositoryImpl;
import io.sobczykm.github.animalshelter.rowmapper.RoleRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static io.sobczykm.github.animalshelter.constant.Constants.GENERIC_API_EXCEPTION_MESSAGE;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryImplTest {
    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;
    @InjectMocks
    private RoleRepositoryImpl underTest;
    private Role role;
    private long employeeId;

    @BeforeEach
    public void setup() {
        employeeId = 1L;
        role = new Role();
        role.setRoleId(employeeId);
    }

    @Test
    void getRoleByEmployeeId_shouldReturnRole_whenIdIsValid() {
        when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenReturn(role);
        Role result = underTest.getRoleByEmployeeId(employeeId);
        assertEquals(role.getRoleId(), result.getRoleId());
    }

    @Test
    void getRoleByEmployeeId_shouldThrowApiException_whenNoRoleFound() {
        when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(EmptyResultDataAccessException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getRoleByEmployeeId(employeeId));
        assertEquals(format("No role found by employeeId: %d", employeeId), apiException.getMessage());
    }

    @Test
    void getRoleByEmployeeId_shouldThrowApiException_whenUnexpectedErrorOccurs() {
        when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(RoleRowMapper.class)))
                .thenThrow(RuntimeException.class);
        ApiException apiException = assertThrows(ApiException.class, () -> underTest.getRoleByEmployeeId(employeeId));
        assertEquals(GENERIC_API_EXCEPTION_MESSAGE, apiException.getMessage());
    }
}