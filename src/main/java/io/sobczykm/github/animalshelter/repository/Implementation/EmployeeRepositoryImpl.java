package io.sobczykm.github.animalshelter.repository.Implementation;

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

import static io.sobczykm.github.animalshelter.query.EmployeeQuery.SELECT_EMPLOYEE_BY_EMAIL_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Employee getEmployeeByEmail(String email) {
        try {
            Employee employee = jdbc.queryForObject(SELECT_EMPLOYEE_BY_EMAIL_QUERY, Map.of("email", email), new EmployeeRowMapper());
            return employee;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No Employee found by email: " + email);
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again");
        }
    }
}
