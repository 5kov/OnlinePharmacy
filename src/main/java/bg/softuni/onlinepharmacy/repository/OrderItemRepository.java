package bg.softuni.onlinepharmacy.repository;


import bg.softuni.onlinepharmacy.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
