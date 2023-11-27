package io.sobczykm.github.animalshelter.repository.implementation;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.exception.ApiException;
import io.sobczykm.github.animalshelter.repository.EmployeeRepository;
import io.sobczykm.github.animalshelter.rowmapper.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static io.sobczykm.github.animalshelter.constant.Constants.GENERIC_API_EXCEPTION_MESSAGE;
import static io.sobczykm.github.animalshelter.query.EmployeeQuery.SELECT_EMPLOYEE_BY_EMAIL_QUERY;
import static io.sobczykm.github.animalshelter.query.EmployeeQuery.SELECT_EMPLOYEE_BY_ID_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Employee getEmployeeByEmail(String email) {
        log.info("Retrieving employee by email");
        try {
            Employee employee = jdbc.queryForObject(SELECT_EMPLOYEE_BY_EMAIL_QUERY, Map.of("email", email), new EmployeeRowMapper());
            return employee;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No employee found by email");
        } catch (Exception exception) {
            throw new ApiException(GENERIC_API_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
        try {
            return jdbc.queryForObject(SELECT_EMPLOYEE_BY_ID_QUERY, Map.of("employeeId", id), new EmployeeRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No employee found by id: " + id);
        } catch (Exception exception) {
            throw new ApiException(GENERIC_API_EXCEPTION_MESSAGE);
        }
    }
}
