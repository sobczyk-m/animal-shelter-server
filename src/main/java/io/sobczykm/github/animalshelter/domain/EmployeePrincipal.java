package io.sobczykm.github.animalshelter.domain;

import io.sobczykm.github.animalshelter.dto.EmployeeDTO;
import io.sobczykm.github.animalshelter.dtomapper.EmployeeDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class EmployeePrincipal implements UserDetails {
    private final Employee employee;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role.getPermission());
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return employee.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return employee.isEnabled();
    }

    public EmployeeDTO getEmployee() {
        return EmployeeDTOMapper.fromEmployee(employee, role);
    }
}
