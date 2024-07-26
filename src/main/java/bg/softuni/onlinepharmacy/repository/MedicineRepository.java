package bg.softuni.onlinepharmacy.repository;


import bg.softuni.onlinepharmacy.model.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    boolean existsByMedicineNameEn(String medicineNameEn);
    boolean existsByMedicineNameBg(String medicineNameBg);
    List<Medicine> findByMedicineNameEnContainingIgnoreCase(String name);

    @Query("SELECT m FROM Medicine m WHERE m.medicineNameEn LIKE %:search% OR m.medicineNameBg LIKE %:search% OR m.activeIngredient.ingredientName LIKE %:search% OR m.activeIngredient.ingredientCode LIKE %:search%")
    Page<Medicine> findBySearchTerm(@Param("search") String search, Pageable pageable);
}