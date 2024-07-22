package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    boolean existsByMedicineNameEn(String medicineNameEn);
    boolean existsByMedicineNameBg(String medicineNameBg);
}