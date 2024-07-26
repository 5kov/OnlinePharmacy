package bg.softuni.onlinepharmacy.init;


import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeData implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public InitializeData(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
    }
}
