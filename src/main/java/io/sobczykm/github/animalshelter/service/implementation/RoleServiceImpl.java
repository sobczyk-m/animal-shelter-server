package io.sobczykm.github.animalshelter.service.implementation;

import io.sobczykm.github.animalshelter.domain.Role;
import io.sobczykm.github.animalshelter.repository.RoleRepository;
import io.sobczykm.github.animalshelter.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByEmployeeId(Long employeeId) {
        return roleRepository.getRoleByEmployeeId(employeeId);
    }
}
