package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import bg.softuni.onlinepharmacy.model.entity.Interaction;
import bg.softuni.onlinepharmacy.model.entity.InteractionTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InteractionRepositoryIntegrationTest {

    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private ActiveIngredientRepository activeIngredientRepository;

    private ActiveIngredient drugOne;
    private ActiveIngredient drugTwo;
    private Interaction interaction;

    @BeforeEach
    public void setUp() {
        // Set up ActiveIngredients
        drugOne = new ActiveIngredient();
        drugOne.setIngredientName("DrugOne");
        drugOne.setIngredientCode("D1");
        activeIngredientRepository.save(drugOne);

        drugTwo = new ActiveIngredient();
        drugTwo.setIngredientName("DrugTwo");
        drugTwo.setIngredientCode("D2");
        activeIngredientRepository.save(drugTwo);

        // Set up Interaction
        interaction = new Interaction();
        interaction.setDrugName(drugOne);
        interaction.setInteractionDrug(drugTwo);
        interaction.setInteractionType(InteractionTypeEnum.MAJOR);
        interactionRepository.save(interaction);
    }

    @Test
    public void testFindByDrugNameAndInteractionDrugAndInteractionType_ShouldReturnInteraction() {
        // Act
        Optional<Interaction> foundInteraction = interactionRepository.findByDrugNameAndInteractionDrugAndInteractionType(
                drugOne, drugTwo, InteractionTypeEnum.MAJOR);

        // Assert
        assertThat(foundInteraction).isPresent();
        assertThat(foundInteraction.get().getDrugName()).isEqualTo(drugOne);
        assertThat(foundInteraction.get().getInteractionDrug()).isEqualTo(drugTwo);
        assertThat(foundInteraction.get().getInteractionType()).isEqualTo(InteractionTypeEnum.MAJOR);
    }

    @Test
    public void testFindByDrugNameAndInteractionDrugAndInteractionType_ShouldReturnEmpty() {
        // Act
        Optional<Interaction> foundInteraction = interactionRepository.findByDrugNameAndInteractionDrugAndInteractionType(
                drugOne, drugTwo, InteractionTypeEnum.MODERATE);

        // Assert
        assertThat(foundInteraction).isNotPresent();
    }

    @Test
    public void testExistsByActiveIngredients_ShouldReturnTrue() {
        // Act
        boolean exists = interactionRepository.existsByActiveIngredients(drugOne, drugTwo);

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByActiveIngredients_ShouldReturnFalse() {
        // Arrange
        ActiveIngredient drugThree = new ActiveIngredient();
        drugThree.setIngredientName("DrugThree");
        drugThree.setIngredientCode("D3");
        activeIngredientRepository.save(drugThree);

        // Act
        boolean exists = interactionRepository.existsByActiveIngredients(drugOne, drugThree);

        // Assert
        assertThat(exists).isFalse();
    }
}
