package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ManageUserService {
    private final UserRepository userRepository;
    private final UserSession userSession;
    private final PasswordEncoder passwordEncoder;

    public ManageUserService(UserRepository userRepository, UserSession userSession, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    public boolean canDeleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity != null && userEntity.isAdministrator()) {
            long count = userRepository.countByAdministrator(true);
            return count > 1;
        }
        return true;
    }

    public void deleteUser(Long id) {
        if (canDeleteUser(id)) {
            userRepository.deleteById(id);
        } else {
            throw new IllegalStateException("At least one administrator is required.");
        }
    }

    public void toggleAdminStatus(Long id, boolean adminStatus) {
        if (canDeleteUser(id)) {
            userRepository.updateUserAdminStatus(id, adminStatus);
        } else {
            throw new IllegalStateException("At least one administrator is required.");
        }

    }

    public UpdateUserDTO findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        return convertToDto(userEntity);
    }

    public String updateUser(UpdateUserDTO updateUserDTO) {
        UserEntity userEntity = userRepository.findById(userSession.getId()).orElse(null);

        Optional<UserEntity> user2 = userRepository.findByUsername(updateUserDTO.getUsername());

        if (user2.isPresent() && user2.get().getId() != userSession.getId()) {
            return "usernameExists";
        }
        user2 = userRepository.findByEmail(updateUserDTO.getEmail());
        if (user2.isPresent() && user2.get().getId() != userSession.getId()) {
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
            UserEntity userEntity = userRepository.findById(userSession.getId()).orElse(null);
            userEntity.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            userRepository.save(userEntity);
            return "success";
        }
        return "passwordsLength";

    }
}
