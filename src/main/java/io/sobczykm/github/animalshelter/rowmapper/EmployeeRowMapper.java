package io.sobczykm.github.animalshelter.rowmapper;

import io.sobczykm.github.animalshelter.domain.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Employee.builder()
                .employeeId(resultSet.getLong("employee_id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .address(resultSet.getString("address"))
                .phone(resultSet.getString("phone"))
                .title(resultSet.getString("title"))
                .bio(resultSet.getString("bio"))
                .enabled(resultSet.getBoolean("enabled"))
                .isNotLocked(resultSet.getBoolean("non_locked"))
                .employmentStart(resultSet.getDate("employment_start").toLocalDate())
                .employmentTermination(resultSet.getDate("employment_termination") != null
                        ? resultSet.getDate("employment_termination").toLocalDate()
                        : null)
                .identityDocument(resultSet.getString("identity_document"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .imageUrl(resultSet.getString("image_url"))
                .build();
    }
}
