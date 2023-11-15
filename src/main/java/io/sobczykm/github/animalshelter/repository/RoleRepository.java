package io.sobczykm.github.animalshelter.repository;

import io.sobczykm.github.animalshelter.domain.Role;

public interface RoleRepository {

    Role getRoleByEmployeeId(Long employeeId);
}
