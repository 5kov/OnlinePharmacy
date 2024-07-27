package bg.softuni.onlinepharmacy.service;


import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.repository.RoleRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManageUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ManageUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    public List<UserEntity> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

//    public boolean canDeleteUser(Long id) {
//        UserEntity userEntity = userRepository.findById(id).orElse(null);
//        if (userEntity != null && userEntity.isAdministrator()) {
//            long count = userRepository.countByAdministrator(true);
//            return count > 1;
//        }
//        return true;
//    }

//    public void deleteUser(Long id) {
//        if (canDeleteUser(id)) {
//            userRepository.deleteById(id);
//        } else {
//            throw new IllegalStateException("At least one administrator is required.");
//        }
//    }
//
//    public void toggleAdminStatus(Long id, boolean adminStatus) {
//        if (canDeleteUser(id)) {
//            userRepository.updateUserAdminStatus(id, adminStatus);
//        } else {
//            throw new IllegalStateException("At least one administrator is required.");
//        }
//
//    }

//--------------------------------------------------------------------------------------------

    @Transactional
    public List<UserEntity> searchUsers(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Transactional
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

    private boolean isLastAdmin() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN)))
                .count() == 1;
    }
//---------------------------------------------------------------------------------------------


    public UpdateUserDTO findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return convertToDto(userEntity);
    }

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

    private UpdateUserDTO convertToDto(UserEntity userEntity) {
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

    private void convertToUser(UpdateUserDTO dto, UserEntity userEntity) {

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

    public UpdateUserDTO findByUsername(String currentPrincipalName) {
        return convertToDto(userRepository.findByUsername(currentPrincipalName).get());
    }
}
