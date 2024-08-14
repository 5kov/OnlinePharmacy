package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import bg.softuni.onlinepharmacy.model.entity.InteractionTypeEnum;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.repository.ActiveIngredientRepository;
import bg.softuni.onlinepharmacy.repository.InteractionRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.service.impl.ManageUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdministratorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageUserServiceImpl manageUserServiceImpl;

    @MockBean
    private ActiveIngredientRepository ingredientRepository;

    @MockBean
    private MedicineRepository medicineRepository;

    @MockBean
    private InteractionRepository interactionRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testShowUserManagement() throws Exception {
        when(manageUserServiceImpl.searchUsers(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-manage-users"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-manage-users"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(manageUserServiceImpl.deleteUser(any())).thenReturn(true);

        mockMvc.perform(post("/delete-user").param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-users"));
    }

    @Test
    public void testToggleRole() throws Exception {
        when(manageUserServiceImpl.toggleUserRole(any())).thenReturn(true);

        mockMvc.perform(post("/toggle-role").param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-users"));
    }

    @Test
    public void testShowAddIngredientForm() throws Exception {
        mockMvc.perform(get("/administrator-add-active-ingredient"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-add-active-ingredient"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testAddIngredient() throws Exception {
        when(ingredientRepository.existsByIngredientName(any())).thenReturn(false);
        when(ingredientRepository.existsByIngredientCode(any())).thenReturn(false);

        mockMvc.perform(post("/administrator-add-active-ingredient")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ingredientName", "Test Ingredient")
                        .param("ingredientCode", "12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("success-medicine"));
    }

    @Test
    public void testShowIngredients() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-manage-active-ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-manage-active-ingredients"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    public void testDeleteIngredient() throws Exception {
        mockMvc.perform(post("/deleteIngredient/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-active-ingredients"));
    }

    @Test
    public void testShowAddForm() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-add-medicines"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-add-medicines"))
                .andExpect(model().attributeExists("medicine"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    public void testAddMedicine() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
        when(medicineRepository.existsByMedicineNameEn(any())).thenReturn(false);
        when(medicineRepository.existsByMedicineNameBg(any())).thenReturn(false);

        mockMvc.perform(post("/administrator-add-medicines")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("medicineNameEn", "Test Medicine")
                        .param("medicineNameBg", "Тестово Лекарство")
                        .param("price", "10.0")
                        .param("descriptionEn", "Test Description")
                        .param("descriptionBg", "Тестово Описание")
                        .param("imageUrl", "http://example.com/image.jpg")
                        .param("activeIngredientId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("success-medicine"));
    }

    @Test
    public void testListMedicines() throws Exception {
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-manage-medicines"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-manage-medicines"))
                .andExpect(model().attributeExists("medicines"))
                .andExpect(model().attributeExists("ingredients"));
    }

    @Test
    public void testSaveMedicine() throws Exception {
        when(medicineRepository.findAll()).thenReturn(Collections.emptyList());
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("medicineNameEn", "Test Medicine")
                        .param("medicineNameBg", "Тестово Лекарство")
                        .param("price", "10.0")
                        .param("descriptionEn", "Test Description")
                        .param("descriptionBg", "Тестово Описание")
                        .param("imageUrl", "http://example.com/image.jpg")
                        .param("activeIngredientId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-medicines"));
    }

    @Test
    public void testDeleteMedicine() throws Exception {
        mockMvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-medicines"));
    }

    @Test
    public void testGetMedicineData() throws Exception {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setMedicineNameEn("Test Medicine");
        medicine.setMedicineNameBg("Тестово Лекарство");
        medicine.setPrice(10.0);
        medicine.setDescriptionEn("Test Description");
        medicine.setDescriptionBg("Тестово Описание");
        medicine.setImageUrl("http://example.com/image.jpg");
        ActiveIngredient ingredient = new ActiveIngredient();
        ingredient.setId(1L);
        medicine.setActiveIngredient(ingredient);

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        mockMvc.perform(get("/data/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.medicineNameEn").value("Test Medicine"))
                .andExpect(jsonPath("$.medicineNameBg").value("Тестово Лекарство"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.descriptionEn").value("Test Description"))
                .andExpect(jsonPath("$.descriptionBg").value("Тестово Описание"))
                .andExpect(jsonPath("$.imageUrl").value("http://example.com/image.jpg"))
                .andExpect(jsonPath("$.activeIngredientId").value(1L));
    }
    @Test
    public void testShowAddInteractionForm() throws Exception {
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-add-interactions"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-add-interactions"))
                .andExpect(model().attributeExists("ingredients"))
                .andExpect(model().attributeExists("interactionTypes"));
    }

    @Test
    public void testSaveInteraction() throws Exception {
        ActiveIngredient drugName = new ActiveIngredient();
        drugName.setId(1L);
        ActiveIngredient interactionDrug = new ActiveIngredient();
        interactionDrug.setId(2L);
        InteractionTypeEnum interactionType;

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(drugName));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(interactionDrug));
        when(interactionRepository.findByDrugNameAndInteractionDrugAndInteractionType(any(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/administrator-add-interactions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("drugNameId", "1")
                        .param("interactionDrugId", "2")
                        .param("interactionType", String.valueOf(InteractionTypeEnum.MINOR)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-add-interactions"));
    }

    @Test
    public void testListInteractions() throws Exception {
        when(interactionRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/administrator-manage-interactions"))
                .andExpect(status().isOk())
                .andExpect(view().name("administrator-manage-interactions"))
                .andExpect(model().attributeExists("interactions"));
    }

    @Test
    public void testDeleteInteraction() throws Exception {
        mockMvc.perform(post("/delete-interaction/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/administrator-manage-interactions"));
    }



}
