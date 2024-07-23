package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import bg.softuni.onlinepharmacy.model.entity.Interaction;
import bg.softuni.onlinepharmacy.model.entity.InteractionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Optional<Interaction> findByDrugNameAndInteractionDrugAndInteractionType(
            ActiveIngredient drugName, ActiveIngredient interactionDrug, InteractionTypeEnum interactionType);

    @Query("SELECT COUNT(i) > 0 FROM Interaction i WHERE (i.drugName = :ingredientOne AND i.interactionDrug = :ingredientTwo) OR (i.drugName = :ingredientTwo AND i.interactionDrug = :ingredientOne)")
    boolean existsByActiveIngredients(ActiveIngredient ingredientOne, ActiveIngredient ingredientTwo);

}
