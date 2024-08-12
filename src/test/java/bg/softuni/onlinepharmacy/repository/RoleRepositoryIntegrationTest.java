package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;

    private UserRoleEntity adminRole;
    private UserRoleEntity userRole;

    @BeforeEach
    public void setUp() {
        // Set up admin role
        if(roleRepository.findByRole(UserRoleEnum.ADMIN) == null) {
            adminRole = new UserRoleEntity();
            adminRole.setRole(UserRoleEnum.ADMIN);
            roleRepository.save(adminRole);
        }


        // Set up user role
        if(roleRepository.findByRole(UserRoleEnum.USER) == null) {
            userRole = new UserRoleEntity();
            userRole.setRole(UserRoleEnum.USER);
            roleRepository.save(userRole);
        }
    }

    @Test
    public void testFindByRole_ShouldReturnAdminRole() {
        // Act
        UserRoleEntity foundRole = roleRepository.findByRole(UserRoleEnum.ADMIN);

        // Assert
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRole()).isEqualTo(UserRoleEnum.ADMIN);
    }

    @Test
    public void testFindByRole_ShouldReturnUserRole() {
        // Act
        UserRoleEntity foundRole = roleRepository.findByRole(UserRoleEnum.USER);

        // Assert
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRole()).isEqualTo(UserRoleEnum.USER);
    }

    @Test
    public void testFindByRole_ShouldReturnNullForNonExistingRole() {
        // Act
        UserRoleEntity foundRole = roleRepository.findByRole(UserRoleEnum.SUPER_ADMIN);

        // Assert
        assertThat(foundRole).isNull();
    }
}
