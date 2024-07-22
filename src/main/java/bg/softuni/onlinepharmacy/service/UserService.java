package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.LoginDTO;
import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
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


}