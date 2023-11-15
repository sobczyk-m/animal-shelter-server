package io.sobczykm.github.animalshelter.service;

import io.sobczykm.github.animalshelter.domain.Role;

public interface RoleService {
    Role getRoleByEmployeeId(Long employeeId);
}
