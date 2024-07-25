package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.User;
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

    public List<User> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    public boolean canDeleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.isAdministrator()) {
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
        User user = userRepository.findById(id).orElse(null);
        return convertToDto(user);
    }

    public String updateUser(UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(userSession.getId()).orElse(null);

        Optional<User> user2 = userRepository.findByUsername(updateUserDTO.getUsername());

        if (user2.isPresent() && user2.get().getId() != userSession.getId()) {
            return "usernameExists";
        }
        user2 = userRepository.findByEmail(updateUserDTO.getEmail());
        if (user2.isPresent() && user2.get().getId() != userSession.getId()) {
            return "emailExists";
        }

        convertToUser(updateUserDTO, user);
        userRepository.save(user);
        return "success";
    }

    private UpdateUserDTO convertToDto(User user) {
        UpdateUserDTO dto = new UpdateUserDTO();
        if (user != null) {
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setCountry(user.getCountry());
            dto.setCity(user.getCity());
            dto.setStreet(user.getStreet());
            dto.setPostalCode(user.getPostalCode());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setPassword(user.getPassword());
        }
        return dto;
    }

    private void convertToUser(UpdateUserDTO dto, User user) {

        if (dto != null && user != null) {
            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setCountry(dto.getCountry());
            user.setCity(dto.getCity());
            user.setStreet(dto.getStreet());
            user.setPostalCode(dto.getPostalCode());
            user.setPhoneNumber(dto.getPhoneNumber());
            if(dto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

    }

    public String updatePassword(UpdateUserDTO updateUserDTO) {
        if (!updateUserDTO.getPassword().equals(updateUserDTO.getConfirmPassword())) {
            return "passwordsDoNotMatch";
        }
        if(updateUserDTO.getPassword().length() >= 3 && updateUserDTO.getPassword().length() <= 20) {
            User user = userRepository.findById(userSession.getId()).orElse(null);
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            userRepository.save(user);
            return "success";
        }
        return "passwordsLength";

    }
}
