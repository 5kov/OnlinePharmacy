package bg.softuni.onlinepharmacy.repository;


import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserEntity(UserEntity userEntity);
}
