package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MedicineRepositoryIntegrationTest {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private ActiveIngredientRepository activeIngredientRepository;

    private Medicine medicine;

    @BeforeEach
    public void setUp() {
        // Create and save an ActiveIngredient
        ActiveIngredient activeIngredient = new ActiveIngredient();
        activeIngredient.setIngredientName("Test Ingredient 1");
        activeIngredient.setIngredientCode("TEST123");
        activeIngredientRepository.save(activeIngredient);

        // Create and save a Medicine entity
        medicine = new Medicine();
        medicine.setMedicineNameEn("Test Name 1");
        medicine.setMedicineNameBg("Тест Име 1");
        medicine.setPrice(9.99);
        medicine.setDescriptionEn("Effective test description");
        medicine.setDescriptionBg("Ефективно тест описание");
        medicine.setImageUrl("http://example.com/test-image.jpg");
        medicine.setFavourite(true);
        medicine.setVotes(5);
        medicine.setActiveIngredient(activeIngredient);
        medicineRepository.save(medicine);
    }

    @Test
    public void testExistsByMedicineNameEn_ShouldReturnTrue() {
        // Act
        boolean exists = medicineRepository.existsByMedicineNameEn("Test Name 1");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByMedicineNameEn_ShouldReturnFalse() {
        // Act
        boolean exists = medicineRepository.existsByMedicineNameEn("Test Name 2");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByMedicineNameBg_ShouldReturnTrue() {
        // Act
        boolean exists = medicineRepository.existsByMedicineNameBg("Тест Име 1");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByMedicineNameBg_ShouldReturnFalse() {
        // Act
        boolean exists = medicineRepository.existsByMedicineNameBg("Тест Име 2");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindByMedicineNameEnContainingIgnoreCase_ShouldReturnMatchingMedicines() {
        // Act
        List<Medicine> medicines = medicineRepository.findByMedicineNameEnContainingIgnoreCase("test name 1");

        // Assert
        assertThat(medicines).isNotEmpty();
        assertThat(medicines.get(0).getMedicineNameEn()).isEqualTo("Test Name 1");
        assertThat(medicines.get(0).getPrice()).isEqualTo(9.99);
        assertThat(medicines.get(0).getDescriptionEn()).isEqualTo("Effective test description");
        assertThat(medicines.get(0).getImageUrl()).isEqualTo("http://example.com/test-image.jpg");
        assertThat(medicines.get(0).isFavourite()).isTrue();
        assertThat(medicines.get(0).getVotes()).isEqualTo(5);
    }

    @Test
    public void testFindByMedicineNameEnContainingIgnoreCase_ShouldReturnEmptyList() {
        // Act
        List<Medicine> medicines = medicineRepository.findByMedicineNameEnContainingIgnoreCase("test name 2");

        // Assert
        assertThat(medicines).isEmpty();
    }

    @Test
    public void testFindByMedicineNameBgContainingIgnoreCase_ShouldReturnMatchingMedicines() {
        // Act
        List<Medicine> medicines = medicineRepository.findByMedicineNameBgContainingIgnoreCase("тест име 1");

        // Assert
        assertThat(medicines).isNotEmpty();
        assertThat(medicines.get(0).getMedicineNameBg()).isEqualTo("Тест Име 1");
        assertThat(medicines.get(0).getPrice()).isEqualTo(9.99);
        assertThat(medicines.get(0).getDescriptionBg()).isEqualTo("Ефективно тест описание");
        assertThat(medicines.get(0).getImageUrl()).isEqualTo("http://example.com/test-image.jpg");
        assertThat(medicines.get(0).isFavourite()).isTrue();
        assertThat(medicines.get(0).getVotes()).isEqualTo(5);
    }

    @Test
    public void testFindByMedicineNameBgContainingIgnoreCase_ShouldReturnEmptyList() {
        // Act
        List<Medicine> medicines = medicineRepository.findByMedicineNameBgContainingIgnoreCase("тест име 2");

        // Assert
        assertThat(medicines).isEmpty();
    }

    @Test
    public void testFindBySearchTerm_ShouldReturnMatchingMedicines() {
        // Act
        Page<Medicine> medicines = medicineRepository.findBySearchTerm("Test Name 1", PageRequest.of(0, 10));

        // Assert
        assertThat(medicines).isNotEmpty();
        assertThat(medicines.getContent().get(0).getMedicineNameEn()).isEqualTo("Test Name 1");
        assertThat(medicines.getContent().get(0).getPrice()).isEqualTo(9.99);
        assertThat(medicines.getContent().get(0).getDescriptionEn()).isEqualTo("Effective test description");
        assertThat(medicines.getContent().get(0).getImageUrl()).isEqualTo("http://example.com/test-image.jpg");
        assertThat(medicines.getContent().get(0).isFavourite()).isTrue();
        assertThat(medicines.getContent().get(0).getVotes()).isEqualTo(5);
    }

    @Test
    public void testFindBySearchTerm_ShouldReturnEmptyPage() {
        // Act
        Page<Medicine> medicines = medicineRepository.findBySearchTerm("Test Name 2", PageRequest.of(0, 10));

        // Assert
        assertThat(medicines.getContent()).isEmpty();
    }
}
