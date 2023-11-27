package io.sobczykm.github.animalshelter.service;

import io.sobczykm.github.animalshelter.repository.implementation.RoleRepositoryImpl;
import io.sobczykm.github.animalshelter.service.implementation.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepositoryImpl roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;


    @Test
    void getRoleByEmployeeId_shouldCallRepositoryMethod_whenCalled() {
        roleService.getRoleByEmployeeId(1L);
        verify(roleRepository, times(1)).getRoleByEmployeeId(1L);
    }
}