package io.sobczykm.github.animalshelter.repository.implementation;

import io.sobczykm.github.animalshelter.domain.Role;
import io.sobczykm.github.animalshelter.exception.ApiException;
import io.sobczykm.github.animalshelter.repository.RoleRepository;
import io.sobczykm.github.animalshelter.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

import static io.sobczykm.github.animalshelter.constant.Constants.GENERIC_API_EXCEPTION_MESSAGE;
import static io.sobczykm.github.animalshelter.query.EmployeeQuery.SELECT_ROLE_BY_ID_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role getRoleByEmployeeId(Long employeeId) {
        log.info("Fetching role for employee id: {}", employeeId);
        try {
            Role role = jdbc.queryForObject(SELECT_ROLE_BY_ID_QUERY, Map.of("employeeId", employeeId), new RoleRowMapper());
            return role;
        } catch (EmptyResultDataAccessException exception) {
            throw new ApiException("No role found by employeeId: " + employeeId);
        } catch (Exception exception) {
            throw new ApiException(GENERIC_API_EXCEPTION_MESSAGE);
        }
    }
}
