package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.CartRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PharmacyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private MedicineRepository medicineRepository;

    @MockBean
    private CartServiceImpl cartServiceImpl;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testListMedicines() throws Exception {
        Medicine medicine = new Medicine();
        Page<Medicine> medicinePage = new PageImpl<>(Collections.singletonList(medicine), PageRequest.of(0, 9), 1);

        when(medicineRepository.findBySearchTerm(any(String.class), any(Pageable.class))).thenReturn(medicinePage);

        mockMvc.perform(get("/pharmacy").param("search", "").param("page", "0").param("size", "9"))
                .andExpect(status().isOk())
                .andExpect(view().name("pharmacy"))
                .andExpect(model().attributeExists("medicines"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("currentSize"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attribute("medicines", medicinePage.getContent()))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("totalPages", medicinePage.getTotalPages()))
                .andExpect(model().attribute("currentSize", 9))
                .andExpect(model().attribute("search", ""));
    }

    @Test
    public void testAddToCart() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        mockMvc.perform(post("/add-to-cart")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("medicineId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pharmacy"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "Item added to cart successfully!"));
    }
}
