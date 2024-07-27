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

import java.util.Random;

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

            String[] ingredientNames = {
                    "sodium fluoride", "sodium monofluorophosphate", "olaflur", "stannous fluoride", "combinations", "sodium fluoride, combinations", "hydrogen peroxide", "chlorhexidine", "amphotericin B"
            };

            String[] ingredientCodes = {
                    "A01AA01", "A01AA02", "A01AA03", "A01AA04", "A01AA30", "A01AA51", "A01AB02", "A01AB03", "A01AB04"
            };

            for (int i = 0; i < ingredientNames.length; i++) {
                ActiveIngredient activeIngredient = new ActiveIngredient();
                activeIngredient.setIngredientName(ingredientNames[i]);
                activeIngredient.setIngredientCode(ingredientCodes[i]);
                activeIngredientRepository.save(activeIngredient);
            }


        }
        if (medicineRepository.count() == 0) {
//            Medicine medicine = new Medicine();
//            medicine.setMedicineNameEn("Omeprazole capsules 20 mg x28");
//            medicine.setMedicineNameBg("Омепразол капсули 20 мг х28");
//            medicine.setPrice(14.99);
//            medicine.setDescriptionEn("Omeprazole is a medication used to treat various acid-related conditions in the stomach and intestines. Here are the key points about omeprazole:\n" +
//                    "\n" +
//                    "Mechanism of Action:\n" +
//                    "Omeprazole belongs to a class of drugs called proton pump inhibitors (PPIs).\n" +
//                    "It works by reducing the production of stomach acid.\n" +
//                    "Indications:\n" +
//                    "Omeprazole is prescribed for the following conditions:\n" +
//                    "Gastroesophageal reflux disease (GERD): A condition where stomach acid flows back into the esophagus, causing symptoms like heartburn.\n" +
//                    "Peptic ulcers: Omeprazole helps heal ulcers in the stomach or upper part of the small intestine.\n" +
//                    "Zollinger-Ellison syndrome: A rare condition where the stomach produces excessive acid due to a tumor.\n" +
//                    "Dosage and Administration:\n" +
//                    "Omeprazole is available as 20 mg capsules.\n" +
//                    "The usual dose varies depending on the condition being treated.\n" +
//                    "It’s usually taken once daily before a meal.\n" +
//                    "Duration of Treatment:\n" +
//                    "Treatment duration depends on the specific condition and individual response.\n" +
//                    "Some conditions may require short-term use, while others may need longer treatment.\n" +
//                    "Side Effects:\n" +
//                    "Common side effects include headache, nausea, and abdominal pain.\n" +
//                    "Serious side effects are rare but may include allergic reactions or changes in blood cell counts.\n" +
//                    "Precautions and Interactions:\n" +
//                    "Inform your doctor about any other medications you’re taking.\n" +
//                    "Omeprazole may interact with certain drugs, including those metabolized by liver enzymes.\n" +
//                    "Remember, my friend, omeprazole is like a guardian for your stomach—a shield against excess acid.");
//            medicine.setDescriptionBg("Омепразол е медикамент, използван за лечение на различни заболявания, свързани с киселината в стомаха и червата. Ето някои ключови точки за омепразол:\n" +
//                    "\n" +
//                    "Механизъм на Действие:\n" +
//                    "Омепразол принадлежи към класа на лекарствата, наречени инхибитори на протонната помпа (ИПП).\n" +
//                    "Той намалява образуването на стомашна киселина.\n" +
//                    "Показания:\n" +
//                    "Омепразол се прилага за следните състояния:\n" +
//                    "Гастроезофагеална рефлуксна болест (ГЕРБ): Състояние, при което киселината от стомаха се връща в пищевода, причинявайки болка и възпаление.\n" +
//                    "Язви на дванадесетопръстника или стомаха: Омепразол помага за заздравяването на язвите.\n" +
//                    "Синдром на Цьолинген-Елисон: Прекомерно образуване на киселина в стомаха.\n" +
//                    "Дозировка и Приложение:\n" +
//                    "Омепразол се предлага като капсули от 20 мг.\n" +
//                    "Обикновено се приема веднъж дневно преди хранене.\n" +
//                    "Продължителност на Лечението:\n" +
//                    "Продължителността на лечението зависи от конкретното състояние и реакцията на индивида.\n" +
//                    "Нежелани Ефекти:\n" +
//                    "Обикновени странични ефекти включват главоболие, гадене и болки в корема.\n" +
//                    "Сериозни странични ефекти са редки,");
//            medicine.setImageUrl("pack_mockup.gif");
//            medicine.setFavourite(false);
//            medicine.setVotes(0);
//            medicine.setActiveIngredient(activeIngredientRepository.findById(1L).get());
//            medicineRepository.save(medicine);


            Random random = new Random();
            String[] medicineBases = {"Paracetamol", "Ibuprofen", "Aspirin", "Cetirizine", "Amoxicillin",
                    "Metformin", "Simvastatin", "Lisinopril", "Amlodipine", "Atorvastatin",
                    "Albuterol", "Gabapentin", "Warfarin", "Zolpidem", "Clonazepam",
                    "Sertraline", "Furosemide", "Ciprofloxacin", "Tamsulosin", "Prednisone"};
            String[] descriptionsEn = {
                    "is used to relieve pain, reduce fever, and alleviate inflammation symptoms.",
                    "is used to reduce inflammation and pain in the body.",
                    "is used to treat pain, fever, or inflammation.",
                    "is used to treat allergy symptoms such as itching, swelling, and rashes.",
                    "is an antibiotic that fights bacteria.",
                    "is a diabetes medication that helps control blood sugar levels.",
                    "helps lower cholesterol and triglycerides in the blood.",
                    "is used to treat high blood pressure or improve survival after a heart attack.",
                    "is used to treat high blood pressure (hypertension) or chest pain.",
                    "is used to help lower bad cholesterol and fats.",
                    "is used to treat or prevent bronchospasm in people with reversible obstructive airway disease.",
                    "is used to treat neuropathic pain and nerve damage.",
                    "is an anticoagulant that helps prevent the formation of blood clots.",
                    "is used to treat insomnia.",
                    "is used to treat seizures and panic disorder.",
                    "is an antidepressant that is used to treat depression.",
                    "is a diuretic that causes you to make more urine, helping your body get rid of extra salt and water.",
                    "is an antibiotic that fights bacteria in the body.",
                    "is used to improve urination in men with benign prostatic hyperplasia (enlarged prostate).",
                    "is used to reduce inflammation and suppress the immune system."
            };

            String[] medicineNamesBg = {
                    "Парацетамол", "Ибупрофен", "Аспирин", "Цетиризин", "Амоксицилин",
                    "Метформин", "Симвастатин", "Лизиноприл", "Амлодипин", "Аторвастатин",
                    "Албутерол", "Габапентин", "Варфарин", "Золпидем", "Клоназепам",
                    "Сертралин", "Фуроземид", "Ципрофлоксацин", "Тамсулозин", "Преднизон"
            };
            String[] descriptionsBg = {
                    "се използва за облекчаване на болка, намаляване на треска и облекчаване на симптоми на възпаление.",
                    "се използва за намаляване на възпаление и болка в тялото.",
                    "се използва за лечение на болка, треска или възпаление.",
                    "се използва за лечение на алергични симптоми като сърбеж, подуване и обриви.",
                    "е антибиотик, който бори бактерии.",
                    "е лекарство за диабет, което помага за контролиране на нивата на кръвната захар.",
                    "помага за понижаване на нивата на холестерол и триглицериди в кръвта.",
                    "се използва за лечение на високо кръвно налягане или подобряване на оцеляването след сърдечен удар.",
                    "се използва за лечение на високо кръвно налягане (хипертония) или болки в гърдите.",
                    "се използва за помагане на намаляване на лошия холестерол и мазнините.",
                    "се използва за лечение или предотвратяване на бронхоспазъм при хора с обратима обструктивна белодробна болест.",
                    "се използва за лечение на невропатична болка и нервни увреждания.",
                    "е антикоагулант, който помага за предотвратяване на образуването на кръвни съсиреци.",
                    "се използва за лечение на безсъние.",
                    "се използва за лечение на припадъци и паническо разстройство.",
                    "е антидепресант, който се използва за лечение на депресия.",
                    "е диуретик, който кара тялото ви да произвежда повече урина, помагайки да се отървете от излишната сол и вода.",
                    "е антибиотик, който бори бактерии в тялото.",
                    "се използва за подобряване на уринирането при мъже с доброкачествена хиперплазия на простатата (увеличена простата).",
                    "се използва за намаляване на възпалението и потискане на имунната система."
            };

            for (int i = 0; i < 20; i++) {
                Medicine medicine = new Medicine();
                int mg = random.nextInt(500) + 100; // random mg from 100 to 600
                int tablets = (random.nextInt(4) + 1) * 10; // random tablets count from 10 to 40

                String value1 = medicineBases[i] + " capsules " + mg + "mg x" + tablets;
                String value2 = medicineNamesBg[i] + " капсули " + mg + "мг x" + tablets;
                double value3 = 10.00 + random.nextDouble() * 50.0; // price from 10 to 60
                String value4 = medicineBases[i] + " " + descriptionsEn[i];
                String value5 = medicineNamesBg[i] + " " + descriptionsBg[i];

                medicine.setMedicineNameEn(value1);
                medicine.setMedicineNameBg(value2);
                medicine.setPrice(value3);
                medicine.setDescriptionEn(value4);
                medicine.setDescriptionBg(value5);
                medicine.setImageUrl("pack_mockup.gif");
                medicine.setFavourite(false);
                medicine.setVotes(random.nextInt(100)); // votes from 0 to 99

                long ingredientId = 1L + random.nextInt(9); // Random ID between 1 and 9
                ActiveIngredient ingredient = activeIngredientRepository.findById(ingredientId).orElse(null);
                medicine.setActiveIngredient(ingredient);

                medicineRepository.save(medicine); // Save the medicine

            }
        }
    }
}
