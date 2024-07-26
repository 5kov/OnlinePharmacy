package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserSession userSession, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
        this.roleRepository = roleRepository;
    }

    public String register(RegisterDTO data) {
        Optional<UserEntity> user = userRepository.findByUsername(data.getUsername());
        if (user.isPresent()) {
            return "usernameExists";
        }
        user = userRepository.findByEmail(data.getEmail());
        if (user.isPresent()) {
            return "emailExists";
        }
        if (!data.getPassword().equals(data.getConfirmPassword())) {
            return "passwordsDoNotMatch";
        }

        UserRoleEntity adminRole = roleRepository.findByRole(UserRoleEnum.ADMIN);
        UserRoleEntity userRole = roleRepository.findByRole(UserRoleEnum.USER);

        UserEntity mapped = modelMapper.map(data, UserEntity.class);
        mapped.setPassword(passwordEncoder.encode(data.getPassword()));

        boolean firstUser = userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN)))
                .count() == 0;
        if(firstUser) {
            mapped.getRoles().add(adminRole);
        } else {
            mapped.getRoles().add(userRole);
        }
        userRepository.save(mapped);
        return "success";
    }

//    public boolean login(LoginDTO data) {
//        Optional<UserEntity> user = userRepository.findByUsername(data.getUsername());
//        if (user.isPresent()) {
//            if(passwordEncoder.matches(data.getPassword(), user.get().getPassword())) {
//                userSession.logIn(user.get());
//                return true;
//            }
//        }
//        return false;
//    }

//    public void logout() {
//        userSession.logOut();
//    }
}
