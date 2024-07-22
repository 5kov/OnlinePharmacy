package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
}
