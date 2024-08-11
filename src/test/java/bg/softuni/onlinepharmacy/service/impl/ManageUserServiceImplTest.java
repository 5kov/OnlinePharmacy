package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.ManageUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ManageUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ManageUserServiceImpl manageUserService;

    private UserEntity userEntity;
    private UserRoleEntity userRole;
    private UserRoleEntity adminRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userRole = new UserRoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        adminRole = new UserRoleEntity();
        adminRole.setRole(UserRoleEnum.ADMIN);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        List<UserRoleEntity> list = new ArrayList<>();
        list.add(userRole);
        userEntity.setRoles(list);


        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void searchUsers() {
        when(userRepository.findByUsernameContaining("test")).thenReturn(Collections.singletonList(userEntity));

        List<UserEntity> result = manageUserService.searchUsers("test");

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void deleteUser_UserRole() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        boolean result = manageUserService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_AdminRoleNotLast() {
        List<UserRoleEntity> list = new ArrayList<>();
        list.add(adminRole);
        userEntity.setRoles(list);


        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UserEntity anotherAdmin = new UserEntity();
        anotherAdmin.setRoles(list);
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity, anotherAdmin));

        boolean result = manageUserService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_AdminRoleIsLast() {
        List<UserRoleEntity> list = new ArrayList<>();
        list.add(adminRole);
        userEntity.setRoles(list);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        boolean result = manageUserService.deleteUser(1L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void toggleUserRole_UserToAdmin() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByRole(UserRoleEnum.USER)).thenReturn(userRole);

        boolean result = manageUserService.toggleUserRole(1L);

        assertTrue(result);
        assertTrue(userEntity.getRoles().contains(adminRole));
        assertFalse(userEntity.getRoles().contains(userRole));
        verify(userRepository).save(userEntity);
    }

    @Test
    void toggleUserRole_AdminToUser() {

        List<UserRoleEntity> list = new ArrayList<>();
        list.add(adminRole);
        userEntity.setRoles(list);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByRole(UserRoleEnum.USER)).thenReturn(userRole);

        UserEntity anotherAdmin = new UserEntity();
        anotherAdmin.setRoles(Collections.singletonList(adminRole));
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity, anotherAdmin));

        boolean result = manageUserService.toggleUserRole(1L);

        assertTrue(result);
        assertTrue(userEntity.getRoles().contains(userRole));
        assertFalse(userEntity.getRoles().contains(adminRole));
        verify(userRepository).save(userEntity);
    }

    @Test
    void toggleUserRole_LastAdmin() {
        List<UserRoleEntity> list = new ArrayList<>();
        list.add(adminRole);
        userEntity.setRoles(list);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(adminRole);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        boolean result = manageUserService.toggleUserRole(1L);

        assertFalse(result);
        verify(userRepository, never()).save(userEntity);
    }

    @Test
    void isLastAdmin() {
        UserEntity admin = new UserEntity();
        admin.setRoles(Collections.singletonList(adminRole));

        when(userRepository.findAll()).thenReturn(Collections.singletonList(admin));

        boolean result = manageUserService.isLastAdmin();

        assertTrue(result);
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UpdateUserDTO dto = manageUserService.findById(1L);

        assertEquals(userEntity.getUsername(), dto.getUsername());
    }

    @Test
    void updateUser_UsernameExists() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("existinguser");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(2L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        String result = manageUserService.updateUser(updateUserDTO);

        assertEquals("usernameExists", result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updateUser_EmailExists() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setEmail("existingemail@test.com");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(2L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail("existingemail@test.com")).thenReturn(Optional.of(existingUser));

        String result = manageUserService.updateUser(updateUserDTO);

        assertEquals("emailExists", result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updateUser_Success() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setEmail("testuser@test.com");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        String result = manageUserService.updateUser(updateUserDTO);

        assertEquals("success", result);
        verify(userRepository).save(userEntity);
    }

    @Test
    void updatePassword_PasswordsDoNotMatch() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setPassword("password1");
        updateUserDTO.setConfirmPassword("password2");

        String result = manageUserService.updatePassword(updateUserDTO);

        assertEquals("passwordsDoNotMatch", result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updatePassword_PasswordLengthInvalid() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setPassword("12");
        updateUserDTO.setConfirmPassword("12");

        String result = manageUserService.updatePassword(updateUserDTO);

        assertEquals("passwordsLength", result);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updatePassword_Success() {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setPassword("password123");
        updateUserDTO.setConfirmPassword("password123");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String result = manageUserService.updatePassword(updateUserDTO);

        assertEquals("success", result);
        verify(userRepository).save(userEntity);
        assertEquals("encodedPassword", userEntity.getPassword());
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        UpdateUserDTO dto = manageUserService.findByUsername("testuser");

        assertEquals("testuser", dto.getUsername());
    }
}

