package io.sobczykm.github.animalshelter.integration.repository;

import io.sobczykm.github.animalshelter.domain.Employee;
import io.sobczykm.github.animalshelter.repository.implementation.EmployeeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EmployeeRepositoryImplIntegrationTest {
    @Autowired
    private NamedParameterJdbcTemplate jdbc;
    private EmployeeRepositoryImpl underTest;
    private Map<String, Object> sqlParameters;

    @BeforeEach
    void setup() {
        underTest = new EmployeeRepositoryImpl(jdbc);
        sqlParameters = new HashMap<>() {{
            put("employeeId", 1L);
            put("firstName", "eM");
            put("lastName", "eS");
            put("email", "test@email.com");
            put("password", "password");
            put("address", "Test Location");
            put("phone", "777222555");
            put("title", "Employee");
            put("employmentStart", LocalDate.now().toString());
            put("identityDocument", "ASD123321");
            put("shelterId", 1L);
            put("shelterName", "Test Shelter Name");
            put("shelterAddress", "Test Address");
        }};
        cleanTables();
        insertTestData();
    }

    @Test
    void getEmployeeByEmail_shouldReturnEmployee_whenEmailExists() {
        Employee employee = underTest.getEmployeeByEmail((String) sqlParameters.get("email"));
        assertThat(employee).isNotNull();
        assertEquals(employee.getEmail(), sqlParameters.get("email"));
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenIdExists() {
        Employee employee = underTest.getEmployeeById((Long) sqlParameters.get("employeeId"));
        assertThat(employee).isNotNull();
        assertEquals(employee.getEmployeeId(), sqlParameters.get("employeeId"));
    }

    private void cleanTables() {
        jdbc.getJdbcOperations().execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.getJdbcOperations().update("TRUNCATE TABLE Shelters");
        jdbc.getJdbcOperations().update("TRUNCATE TABLE Employees");
        jdbc.getJdbcOperations().execute("SET FOREIGN_KEY_CHECKS=1");
    }

    private void insertTestData() {
        Map<String, Object> sheltersMap = Map.of(
                "shelterId", sqlParameters.get("shelterId"),
                "shelterName", sqlParameters.get("shelterName"),
                "shelterAddress", sqlParameters.get("shelterAddress"));
        Map<String, Object> employeeMap = Map.ofEntries(
                Map.entry("employeeId", sqlParameters.get("shelterId")),
                Map.entry("firstName", sqlParameters.get("firstName")),
                Map.entry("lastName", sqlParameters.get("lastName")),
                Map.entry("email", sqlParameters.get("email")),
                Map.entry("password", sqlParameters.get("password")),
                Map.entry("address", sqlParameters.get("address")),
                Map.entry("phone", sqlParameters.get("phone")),
                Map.entry("title", sqlParameters.get("title")),
                Map.entry("employmentStart", sqlParameters.get("employmentStart")),
                Map.entry("identityDocument", sqlParameters.get("identityDocument")),
                Map.entry("shelterId", sqlParameters.get("shelterId"))
        );

        jdbc.update(
                "INSERT INTO Shelters (shelter_id, name, address) VALUES(:shelterId, :shelterName, :shelterAddress)",
                sheltersMap
        );
        jdbc.update(
                "INSERT INTO Employees (employee_id, first_name, last_name, email, password, address, phone, " +
                        "title, employment_start, identity_document, shelter_id) VALUES " +
                        "(:employeeId, :firstName, :lastName, :email, :password, :address, :phone, :title, " +
                        ":employmentStart, :identityDocument, :shelterId)",
                employeeMap
        );
    }
}