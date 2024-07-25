package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveIngredientRepository extends JpaRepository<ActiveIngredient, Long> {
    boolean existsByIngredientName(String ingredientName);
    boolean existsByIngredientCode(String ingredientCode);
}
