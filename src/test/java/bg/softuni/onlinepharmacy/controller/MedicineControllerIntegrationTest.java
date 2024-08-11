package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicineControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicineRepository medicineRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowMedicineDetails_MedicineFound() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setMedicineNameEn("Test Medicine");

        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));

        mockMvc.perform(get("/item").param("medicineId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("medicine"))
                .andExpect(model().attribute("medicine", medicine));
    }

    @Test
    public void testShowMedicineDetails_MedicineNotFound() throws Exception {
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/item").param("medicineId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorPage"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Medicine not found"));
    }
}
