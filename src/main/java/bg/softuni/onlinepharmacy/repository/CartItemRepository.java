package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.CartItem;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.cart.id = :cartId AND c.medicine.id = :medicineId")
    Optional<CartItem> findCartItemByCartIdAndMedicineId(@Param("cartId") Long cartId, @Param("medicineId") Long medicineId);

}
