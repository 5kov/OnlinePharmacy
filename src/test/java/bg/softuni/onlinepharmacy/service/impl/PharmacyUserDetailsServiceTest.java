package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.model.user.PharmacyUserDetails;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PharmacyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PharmacyUserDetailsService pharmacyUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists() {
        // Arrange
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID());
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
        userEntity.setRoles(List.of(userRole));
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails userDetails = pharmacyUserDetailsService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertEquals(1, userDetails.getAuthorities().size());

        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> pharmacyUserDetailsService.loadUserByUsername("nonExistentUser"));

        assertEquals("User with username nonExistentUser not found!", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonExistentUser");
    }

    @Test
    void map_UserRoleEnumMapping() {
        // Act
        GrantedAuthority authority = PharmacyUserDetailsService.map(UserRoleEnum.ADMIN);

        // Assert
        assertNotNull(authority);
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }
}

