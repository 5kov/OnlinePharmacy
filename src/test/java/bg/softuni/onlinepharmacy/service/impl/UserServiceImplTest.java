package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.model.user.PharmacyUserDetails;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;


    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    void register_UsernameExists() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new UserEntity()));

        // Act
        String result = userService.register(registerDTO);

        // Assert
        assertEquals("usernameExists", result);
    }

    @Test
    void register_EmailExists() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newUser");
        registerDTO.setEmail("existingEmail@example.com");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existingEmail@example.com")).thenReturn(Optional.of(new UserEntity()));

        // Act
        String result = userService.register(registerDTO);

        // Assert
        assertEquals("emailExists", result);
    }

    @Test
    void register_PasswordsDoNotMatch() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newUser");
        registerDTO.setEmail("newEmail@example.com");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("differentPassword");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.empty());

        // Act
        String result = userService.register(registerDTO);

        // Assert
        assertEquals("passwordsDoNotMatch", result);
    }

    @Test
    void register_FirstUserAssignedAdminRole() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newUser");
        registerDTO.setEmail("newEmail@example.com");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("password");

        UserEntity mappedUser = new UserEntity();
        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setRole(UserRoleEnum.ADMIN);

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.empty());
        when(modelMapper.map(registerDTO, UserEntity.class)).thenReturn(mappedUser);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByRole(UserRoleEnum.ADMIN)).thenReturn(adminRole);
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        String result = userService.register(registerDTO);

        // Assert
        assertEquals("success", result);
        assertTrue(mappedUser.getRoles().contains(adminRole));
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void register_NotFirstUserAssignedUserRole() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newUser");
        registerDTO.setEmail("newEmail@example.com");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("password");

        UserEntity mappedUser = new UserEntity();
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setRole(UserRoleEnum.USER);

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newEmail@example.com")).thenReturn(Optional.empty());
        when(modelMapper.map(registerDTO, UserEntity.class)).thenReturn(mappedUser);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByRole(UserRoleEnum.USER)).thenReturn(userRole);


        UserEntity adminEntity = new UserEntity();
        UserRoleEntity userRole2 = new UserRoleEntity();
        userRole2.setRole(UserRoleEnum.ADMIN);
        adminEntity.getRoles().add(userRole2);
        when(userRepository.findAll()).thenReturn(List.of(adminEntity));

        // Act
        String result = userService.register(registerDTO);

        // Assert
        assertEquals("success", result);
        assertTrue(mappedUser.getRoles().contains(userRole));
        verify(userRepository, times(1)).save(mappedUser);
    }

@Test
void getCurrentUser_AuthenticatedUser() {
    // Arrange
    UUID uuid = UUID. randomUUID();
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    PharmacyUserDetails userDetails = new PharmacyUserDetails(
            uuid,
            "testuser",
            "password",
            authorities,
            "Test",
            "User"
    );

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);


    // Act
    Optional<PharmacyUserDetails> result = userService.getCurrentUser();

    // Assert
    assertTrue(result.isPresent());
    assertTrue(result.get().getUsername().equals("testuser"));
    verify(securityContext, times(1)).getAuthentication();
    verify(authentication, times(1)).getPrincipal();
}


    @Test
    void getCurrentUser_NotAuthenticated() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(null);

        // Act
        Optional<PharmacyUserDetails> result = userService.getCurrentUser();

        // Assert
        assertFalse(result.isPresent());
    }
}
