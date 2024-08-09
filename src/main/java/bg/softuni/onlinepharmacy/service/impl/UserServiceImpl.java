package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.model.user.PharmacyUserDetails;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    @Override
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

    @Override
    public Optional<PharmacyUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof PharmacyUserDetails pharmacyUserDetails) {
            return Optional.of(pharmacyUserDetails);
        }
        return Optional.empty();
    }
}
