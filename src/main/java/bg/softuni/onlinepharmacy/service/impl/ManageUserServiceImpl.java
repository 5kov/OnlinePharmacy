package bg.softuni.onlinepharmacy.service.impl;


import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.ManageUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManageUserServiceImpl implements ManageUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ManageUserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public List<UserEntity> searchUsers(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserRoleEntity userRole = user.getRoles().stream().findFirst().get();
        if (userRole.getRole().equals(UserRoleEnum.USER)){
            userRepository.deleteById(userId);
            return true;
        } else if (!isLastAdmin()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean toggleUserRole(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        boolean hasUser = user.getRoles().stream().anyMatch(role -> role.getRole().equals(UserRoleEnum.USER));


        UserRoleEntity adminRole = roleRepository.findByRole(UserRoleEnum.ADMIN);
        UserRoleEntity userRole = roleRepository.findByRole(UserRoleEnum.USER);

        if (hasUser) {
            user.getRoles().remove(userRole);
            user.getRoles().add(adminRole);
        } else {
            if (isLastAdmin()) {
                return false; // Cannot remove the last admin
            }
            user.getRoles().remove(adminRole);
            user.getRoles().add(userRole);
        }
        userRepository.save(user);
        return true;
    }
    @Override
    public boolean isLastAdmin() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN)))
                .count() == 1;
    }
    @Override
    public UpdateUserDTO findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return convertToDto(userEntity);
    }
    @Override
    public String updateUser(UpdateUserDTO updateUserDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();

        Optional<UserEntity> user2 = userRepository.findByUsername(updateUserDTO.getUsername());

        if (user2.isPresent() && user2.get().getId() != userEntity.getId()) {
            return "usernameExists";
        }
        user2 = userRepository.findByEmail(updateUserDTO.getEmail());
        if (user2.isPresent() && user2.get().getId() != userEntity.getId()) {
            return "emailExists";
        }

        convertToUser(updateUserDTO, userEntity);
        userRepository.save(userEntity);
        return "success";
    }
    @Override
    public UpdateUserDTO convertToDto(UserEntity userEntity) {
        UpdateUserDTO dto = new UpdateUserDTO();
        if (userEntity != null) {
            dto.setId(userEntity.getId());
            dto.setUsername(userEntity.getUsername());
            dto.setFirstName(userEntity.getFirstName());
            dto.setLastName(userEntity.getLastName());
            dto.setEmail(userEntity.getEmail());
            dto.setCountry(userEntity.getCountry());
            dto.setCity(userEntity.getCity());
            dto.setStreet(userEntity.getStreet());
            dto.setPostalCode(userEntity.getPostalCode());
            dto.setPhoneNumber(userEntity.getPhoneNumber());
            dto.setPassword(userEntity.getPassword());
        }
        return dto;
    }
    @Override
    public void convertToUser(UpdateUserDTO dto, UserEntity userEntity) {

        if (dto != null && userEntity != null) {
            userEntity.setId(dto.getId());
            userEntity.setUsername(dto.getUsername());
            userEntity.setFirstName(dto.getFirstName());
            userEntity.setLastName(dto.getLastName());
            userEntity.setEmail(dto.getEmail());
            userEntity.setCountry(dto.getCountry());
            userEntity.setCity(dto.getCity());
            userEntity.setStreet(dto.getStreet());
            userEntity.setPostalCode(dto.getPostalCode());
            userEntity.setPhoneNumber(dto.getPhoneNumber());
            if(dto.getPassword() != null) {
                userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

    }
    @Override
    public String updatePassword(UpdateUserDTO updateUserDTO) {
        if (!updateUserDTO.getPassword().equals(updateUserDTO.getConfirmPassword())) {
            return "passwordsDoNotMatch";
        }
        if(updateUserDTO.getPassword().length() >= 3 && updateUserDTO.getPassword().length() <= 20) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();
            userEntity.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            userRepository.save(userEntity);
            return "success";
        }
        return "passwordsLength";

    }
    @Override
    public UpdateUserDTO findByUsername(String currentPrincipalName) {
        return convertToDto(userRepository.findByUsername(currentPrincipalName).get());
    }
}
