package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ActiveIngredientRepositoryIntegrationTest {

    @Autowired
    private ActiveIngredientRepository activeIngredientRepository;

    private ActiveIngredient activeIngredient;

    @BeforeEach
    public void setUp() {
        activeIngredient = new ActiveIngredient();
        activeIngredient.setIngredientName("TestName");
        activeIngredient.setIngredientCode("Test");
    }

    @Test
    public void testExistsByIngredientName_ShouldReturnTrue() {
        // Arrange
        activeIngredientRepository.save(activeIngredient);

        // Act
        boolean exists = activeIngredientRepository.existsByIngredientName("TestName");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByIngredientName_ShouldReturnFalse() {
        // Act
        boolean exists = activeIngredientRepository.existsByIngredientName("None existing name");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByIngredientCode_ShouldReturnTrue() {
        // Arrange
        activeIngredientRepository.save(activeIngredient);

        // Act
        boolean exists = activeIngredientRepository.existsByIngredientCode("Test");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByIngredientCode_ShouldReturnFalse() {
        // Act
        boolean exists = activeIngredientRepository.existsByIngredientCode("None");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    public void testSaveActiveIngredient() {
        // Act
        ActiveIngredient savedIngredient = activeIngredientRepository.save(activeIngredient);

        // Assert
        assertThat(savedIngredient).isNotNull();
        assertThat(savedIngredient.getId()).isNotNull();
        assertThat(savedIngredient.getIngredientName()).isEqualTo("TestName");
        assertThat(savedIngredient.getIngredientCode()).isEqualTo("Test");
    }
}
