package io.sobczykm.github.animalshelter.query;

public class EmployeeQuery {
    public static final String SELECT_EMPLOYEE_BY_EMAIL_QUERY = "SELECT * FROM Employees WHERE email = :email";
    public static final String SELECT_ROLE_BY_ID_QUERY = "SELECT r.role_id, r.name, r.permission FROM Roles r JOIN EmployeeRoles er ON er.role_id = r.role_id JOIN Employees e ON e.employee_id = er.employee_id WHERE e.employee_id = :employeeId";
    public static final String SELECT_EMPLOYEE_BY_ID_QUERY = "SELECT * FROM Employees WHERE employee_id = :employeeId";
}
