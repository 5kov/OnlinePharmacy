package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.ManageUserServiceImpl;
import bg.softuni.onlinepharmacy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationLoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private ManageUserServiceImpl manageUserServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testViewLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testViewRegister() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testDoRegister() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setFirstName("Test");
        registerDTO.setLastName("User");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setCountry("TestCountry");
        registerDTO.setCity("TestCity");
        registerDTO.setStreet("TestStreet");
        registerDTO.setPostalCode("12345");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("password");

        when(userServiceImpl.register(any(RegisterDTO.class))).thenReturn("success");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("firstName", registerDTO.getFirstName())
                        .param("lastName", registerDTO.getLastName())
                        .param("email", registerDTO.getEmail())
                        .param("country", registerDTO.getCountry())
                        .param("city", registerDTO.getCity())
                        .param("street", registerDTO.getStreet())
                        .param("postalCode", registerDTO.getPostalCode())
                        .param("phoneNumber", registerDTO.getPhoneNumber())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testDoRegisterUsernameExists() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setFirstName("Test");
        registerDTO.setLastName("User");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setCountry("TestCountry");
        registerDTO.setCity("TestCity");
        registerDTO.setStreet("TestStreet");
        registerDTO.setPostalCode("12345");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("password");

        when(userServiceImpl.register(any(RegisterDTO.class))).thenReturn("usernameExists");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("firstName", registerDTO.getFirstName())
                        .param("lastName", registerDTO.getLastName())
                        .param("email", registerDTO.getEmail())
                        .param("country", registerDTO.getCountry())
                        .param("city", registerDTO.getCity())
                        .param("street", registerDTO.getStreet())
                        .param("postalCode", registerDTO.getPostalCode())
                        .param("phoneNumber", registerDTO.getPhoneNumber())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("usernameExists"));
    }

    @Test
    public void testDoRegisterEmailExists() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setFirstName("Test");
        registerDTO.setLastName("User");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setCountry("TestCountry");
        registerDTO.setCity("TestCity");
        registerDTO.setStreet("TestStreet");
        registerDTO.setPostalCode("12345");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("password");

        when(userServiceImpl.register(any(RegisterDTO.class))).thenReturn("emailExists");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("firstName", registerDTO.getFirstName())
                        .param("lastName", registerDTO.getLastName())
                        .param("email", registerDTO.getEmail())
                        .param("country", registerDTO.getCountry())
                        .param("city", registerDTO.getCity())
                        .param("street", registerDTO.getStreet())
                        .param("postalCode", registerDTO.getPostalCode())
                        .param("phoneNumber", registerDTO.getPhoneNumber())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("emailExists"));
    }

    @Test
    public void testDoRegisterPasswordsDoNotMatch() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setFirstName("Test");
        registerDTO.setLastName("User");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setCountry("TestCountry");
        registerDTO.setCity("TestCity");
        registerDTO.setStreet("TestStreet");
        registerDTO.setPostalCode("12345");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setPassword("password");
        registerDTO.setConfirmPassword("differentPassword");

        when(userServiceImpl.register(any(RegisterDTO.class))).thenReturn("passwordsDoNotMatch");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", registerDTO.getUsername())
                        .param("firstName", registerDTO.getFirstName())
                        .param("lastName", registerDTO.getLastName())
                        .param("email", registerDTO.getEmail())
                        .param("country", registerDTO.getCountry())
                        .param("city", registerDTO.getCity())
                        .param("street", registerDTO.getStreet())
                        .param("postalCode", registerDTO.getPostalCode())
                        .param("phoneNumber", registerDTO.getPhoneNumber())
                        .param("password", registerDTO.getPassword())
                        .param("confirmPassword", registerDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("passwordsDoNotMatch"));
    }

    @Test
    public void testShowSettingsForm() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(manageUserServiceImpl.findByUsername(anyString())).thenReturn(updateUserDTO);

        mockMvc.perform(get("/user-settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-settings"))
                .andExpect(model().attributeExists("updateUserDTO"))
                .andExpect(model().attribute("updateUserDTO", updateUserDTO));
    }

    @Test
    public void testUpdateSettings() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setFirstName("Test");
        updateUserDTO.setLastName("User");
        updateUserDTO.setEmail("testuser@example.com");
        updateUserDTO.setCountry("TestCountry");
        updateUserDTO.setCity("TestCity");
        updateUserDTO.setStreet("TestStreet");
        updateUserDTO.setPostalCode("12345");
        updateUserDTO.setPhoneNumber("1234567890");
        updateUserDTO.setPassword("password");
        updateUserDTO.setConfirmPassword("password");

        when(manageUserServiceImpl.updateUser(any(UpdateUserDTO.class))).thenReturn("success");

        mockMvc.perform(post("/user-settings")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", updateUserDTO.getUsername())
                        .param("firstName", updateUserDTO.getFirstName())
                        .param("lastName", updateUserDTO.getLastName())
                        .param("email", updateUserDTO.getEmail())
                        .param("country", updateUserDTO.getCountry())
                        .param("city", updateUserDTO.getCity())
                        .param("street", updateUserDTO.getStreet())
                        .param("postalCode", updateUserDTO.getPostalCode())
                        .param("phoneNumber", updateUserDTO.getPhoneNumber())
                        .param("password", updateUserDTO.getPassword())
                        .param("confirmPassword", updateUserDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/success"));
    }

    @Test
    public void testShowSuccessForm() throws Exception {
        mockMvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"));
    }

    @Test
    public void testShowChangePasswordForm() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));
        when(manageUserServiceImpl.findById(any(Long.class))).thenReturn(updateUserDTO);

        mockMvc.perform(get("/change-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("change-password"))
                .andExpect(model().attributeExists("updateUserDTO"))
                .andExpect(model().attribute("updateUserDTO", updateUserDTO));
    }

    @Test
    public void testUpdateChangePassword() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setFirstName("Test");
        updateUserDTO.setLastName("User");
        updateUserDTO.setEmail("testuser@example.com");
        updateUserDTO.setCountry("TestCountry");
        updateUserDTO.setCity("TestCity");
        updateUserDTO.setStreet("TestStreet");
        updateUserDTO.setPostalCode("12345");
        updateUserDTO.setPhoneNumber("1234567890");
        updateUserDTO.setPassword("password");
        updateUserDTO.setConfirmPassword("password");

        when(manageUserServiceImpl.updatePassword(any(UpdateUserDTO.class))).thenReturn("success");

        mockMvc.perform(post("/change-password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", updateUserDTO.getUsername())
                        .param("firstName", updateUserDTO.getFirstName())
                        .param("lastName", updateUserDTO.getLastName())
                        .param("email", updateUserDTO.getEmail())
                        .param("country", updateUserDTO.getCountry())
                        .param("city", updateUserDTO.getCity())
                        .param("street", updateUserDTO.getStreet())
                        .param("postalCode", updateUserDTO.getPostalCode())
                        .param("phoneNumber", updateUserDTO.getPhoneNumber())
                        .param("password", updateUserDTO.getPassword())
                        .param("confirmPassword", updateUserDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-settings"));
    }
    @Test
    public void testUpdateSettingsUsernameExists() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setFirstName("Test");
        updateUserDTO.setLastName("User");
        updateUserDTO.setEmail("testuser@example.com");
        updateUserDTO.setCountry("TestCountry");
        updateUserDTO.setCity("TestCity");
        updateUserDTO.setStreet("TestStreet");
        updateUserDTO.setPostalCode("12345");
        updateUserDTO.setPhoneNumber("1234567890");
        updateUserDTO.setPassword("password");
        updateUserDTO.setConfirmPassword("password");

        when(manageUserServiceImpl.updateUser(any(UpdateUserDTO.class))).thenReturn("usernameExists");

        mockMvc.perform(post("/user-settings")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", updateUserDTO.getUsername())
                        .param("firstName", updateUserDTO.getFirstName())
                        .param("lastName", updateUserDTO.getLastName())
                        .param("email", updateUserDTO.getEmail())
                        .param("country", updateUserDTO.getCountry())
                        .param("city", updateUserDTO.getCity())
                        .param("street", updateUserDTO.getStreet())
                        .param("postalCode", updateUserDTO.getPostalCode())
                        .param("phoneNumber", updateUserDTO.getPhoneNumber())
                        .param("password", updateUserDTO.getPassword())
                        .param("confirmPassword", updateUserDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-settings"))
                .andExpect(flash().attributeExists("usernameExists"));
    }

    @Test
    public void testUpdateSettingsEmailExists() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setFirstName("Test");
        updateUserDTO.setLastName("User");
        updateUserDTO.setEmail("testuser@example.com");
        updateUserDTO.setCountry("TestCountry");
        updateUserDTO.setCity("TestCity");
        updateUserDTO.setStreet("TestStreet");
        updateUserDTO.setPostalCode("12345");
        updateUserDTO.setPhoneNumber("1234567890");
        updateUserDTO.setPassword("password");
        updateUserDTO.setConfirmPassword("password");

        when(manageUserServiceImpl.updateUser(any(UpdateUserDTO.class))).thenReturn("emailExists");

        mockMvc.perform(post("/user-settings")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", updateUserDTO.getUsername())
                        .param("firstName", updateUserDTO.getFirstName())
                        .param("lastName", updateUserDTO.getLastName())
                        .param("email", updateUserDTO.getEmail())
                        .param("country", updateUserDTO.getCountry())
                        .param("city", updateUserDTO.getCity())
                        .param("street", updateUserDTO.getStreet())
                        .param("postalCode", updateUserDTO.getPostalCode())
                        .param("phoneNumber", updateUserDTO.getPhoneNumber())
                        .param("password", updateUserDTO.getPassword())
                        .param("confirmPassword", updateUserDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-settings"))
                .andExpect(flash().attributeExists("emailExists"));
    }

    @Test
    public void testUpdateSettingsPasswordsDoNotMatch() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("testuser");
        updateUserDTO.setFirstName("Test");
        updateUserDTO.setLastName("User");
        updateUserDTO.setEmail("testuser@example.com");
        updateUserDTO.setCountry("TestCountry");
        updateUserDTO.setCity("TestCity");
        updateUserDTO.setStreet("TestStreet");
        updateUserDTO.setPostalCode("12345");
        updateUserDTO.setPhoneNumber("1234567890");
        updateUserDTO.setPassword("password");
        updateUserDTO.setConfirmPassword("differentPassword");

        when(manageUserServiceImpl.updateUser(any(UpdateUserDTO.class))).thenReturn("passwordsDoNotMatch");

        mockMvc.perform(post("/user-settings")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", updateUserDTO.getUsername())
                        .param("firstName", updateUserDTO.getFirstName())
                        .param("lastName", updateUserDTO.getLastName())
                        .param("email", updateUserDTO.getEmail())
                        .param("country", updateUserDTO.getCountry())
                        .param("city", updateUserDTO.getCity())
                        .param("street", updateUserDTO.getStreet())
                        .param("postalCode", updateUserDTO.getPostalCode())
                        .param("phoneNumber", updateUserDTO.getPhoneNumber())
                        .param("password", updateUserDTO.getPassword())
                        .param("confirmPassword", updateUserDTO.getConfirmPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user-settings"))
                .andExpect(flash().attributeExists("passwordsDoNotMatch"));
    }
}
