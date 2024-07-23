package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.LoginDTO;
import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.User;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    public String register(RegisterDTO data) {
        Optional<User> user = userRepository.findByUsername(data.getUsername());
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

        User mapped = modelMapper.map(data, User.class);
        mapped.setPassword(passwordEncoder.encode(data.getPassword()));
        mapped.setAdministrator(userRepository.countUsers() == 0);
        userRepository.save(mapped);
        return "success";
    }

    public boolean login(LoginDTO data) {
        Optional<User> user = userRepository.findByUsername(data.getUsername());
        if (user.isPresent()) {
            if(passwordEncoder.matches(data.getPassword(), user.get().getPassword())) {
                userSession.logIn(user.get());
                return true;
            }
        }
        return false;
    }

    public void logout() {
        userSession.logOut();
    }
//    public List<User> findUsersByUsername(String username) {
//        return userRepository.findByUsernameContaining(username);
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteUserById(id);
//    }
//
//    public void toggleAdminStatus(Long id, boolean adminStatus) {
//        userRepository.updateUserAdminStatus(id, adminStatus);
//    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    public boolean canDeleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.isAdministrator()) {
            // Count administrators
            long count = userRepository.countByAdministrator(true);
            // Check if this is the last administrator
            return count > 1;
        }
        return true; // User is not an administrator or not the only one
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
            dto.setEmail(user.getEmail());
            dto.setCountry(user.getCountry());
            dto.setCity(user.getCity());
            dto.setStreet(user.getStreet());
            dto.setPostalCode(user.getPostalCode());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setPassword(user.getPassword());  // Consider security implications of handling passwords
        }
        return dto;
    }

    private void convertToUser(UpdateUserDTO dto, User user) {

        if (dto != null && user != null) {
            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
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
