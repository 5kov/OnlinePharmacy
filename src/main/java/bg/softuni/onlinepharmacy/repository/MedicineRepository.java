package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    boolean existsByMedicineNameEn(String medicineNameEn);
    boolean existsByMedicineNameBg(String medicineNameBg);
    List<Medicine> findByMedicineNameEnContainingIgnoreCase(String name);
}