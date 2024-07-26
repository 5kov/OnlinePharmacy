package bg.softuni.onlinepharmacy.init;


import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.ActiveIngredientRepository;
import bg.softuni.onlinepharmacy.repository.InteractionRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeData implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final MedicineRepository medicineRepository;
    private final ActiveIngredientRepository activeIngredientRepository;

    public InitializeData(RoleRepository roleRepository, MedicineRepository medicineRepository, ActiveIngredientRepository activeIngredientRepository) {
        this.roleRepository = roleRepository;
        this.medicineRepository = medicineRepository;
        this.activeIngredientRepository = activeIngredientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            UserRoleEntity admin = new UserRoleEntity();
            admin.setRole(UserRoleEnum.ADMIN);
            roleRepository.save(admin);
            UserRoleEntity user = new UserRoleEntity();
            user.setRole(UserRoleEnum.USER);
            roleRepository.save(user);
        }
        if (activeIngredientRepository.count() == 0) {
            ActiveIngredient activeIngredient = new ActiveIngredient();
            activeIngredient.setIngredientName("omeprazole");
            activeIngredient.setIngredientCode("A02BC01");
            activeIngredientRepository.save(activeIngredient);
        }
        if (medicineRepository.count() == 0) {
            Medicine medicine = new Medicine();
            medicine.setMedicineNameEn("Omeprazole capsules 20 mg x28");
            medicine.setMedicineNameBg("Омепразол капсули 20 мг х28");
            medicine.setPrice(14.99);
            medicine.setDescriptionEn("Omeprazole is a medication used to treat various acid-related conditions in the stomach and intestines. Here are the key points about omeprazole:\n" +
                    "\n" +
                    "Mechanism of Action:\n" +
                    "Omeprazole belongs to a class of drugs called proton pump inhibitors (PPIs).\n" +
                    "It works by reducing the production of stomach acid.\n" +
                    "Indications:\n" +
                    "Omeprazole is prescribed for the following conditions:\n" +
                    "Gastroesophageal reflux disease (GERD): A condition where stomach acid flows back into the esophagus, causing symptoms like heartburn.\n" +
                    "Peptic ulcers: Omeprazole helps heal ulcers in the stomach or upper part of the small intestine.\n" +
                    "Zollinger-Ellison syndrome: A rare condition where the stomach produces excessive acid due to a tumor.\n" +
                    "Dosage and Administration:\n" +
                    "Omeprazole is available as 20 mg capsules.\n" +
                    "The usual dose varies depending on the condition being treated.\n" +
                    "It’s usually taken once daily before a meal.\n" +
                    "Duration of Treatment:\n" +
                    "Treatment duration depends on the specific condition and individual response.\n" +
                    "Some conditions may require short-term use, while others may need longer treatment.\n" +
                    "Side Effects:\n" +
                    "Common side effects include headache, nausea, and abdominal pain.\n" +
                    "Serious side effects are rare but may include allergic reactions or changes in blood cell counts.\n" +
                    "Precautions and Interactions:\n" +
                    "Inform your doctor about any other medications you’re taking.\n" +
                    "Omeprazole may interact with certain drugs, including those metabolized by liver enzymes.\n" +
                    "Remember, my friend, omeprazole is like a guardian for your stomach—a shield against excess acid.");
            medicine.setDescriptionBg("Омепразол е медикамент, използван за лечение на различни заболявания, свързани с киселината в стомаха и червата. Ето някои ключови точки за омепразол:\n" +
                    "\n" +
                    "Механизъм на Действие:\n" +
                    "Омепразол принадлежи към класа на лекарствата, наречени инхибитори на протонната помпа (ИПП).\n" +
                    "Той намалява образуването на стомашна киселина.\n" +
                    "Показания:\n" +
                    "Омепразол се прилага за следните състояния:\n" +
                    "Гастроезофагеална рефлуксна болест (ГЕРБ): Състояние, при което киселината от стомаха се връща в пищевода, причинявайки болка и възпаление.\n" +
                    "Язви на дванадесетопръстника или стомаха: Омепразол помага за заздравяването на язвите.\n" +
                    "Синдром на Цьолинген-Елисон: Прекомерно образуване на киселина в стомаха.\n" +
                    "Дозировка и Приложение:\n" +
                    "Омепразол се предлага като капсули от 20 мг.\n" +
                    "Обикновено се приема веднъж дневно преди хранене.\n" +
                    "Продължителност на Лечението:\n" +
                    "Продължителността на лечението зависи от конкретното състояние и реакцията на индивида.\n" +
                    "Нежелани Ефекти:\n" +
                    "Обикновени странични ефекти включват главоболие, гадене и болки в корема.\n" +
                    "Сериозни странични ефекти са редки,");
            medicine.setImageUrl("pack_mockup.gif");
            medicine.setFavourite(false);
            medicine.setVotes(0);
            medicine.setActiveIngredient(activeIngredientRepository.findById(1L).get());
            medicineRepository.save(medicine);
        }
    }
}
