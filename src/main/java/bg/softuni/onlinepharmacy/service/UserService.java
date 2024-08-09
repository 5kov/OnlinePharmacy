package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.user.PharmacyUserDetails;

import java.util.Optional;

public interface UserService {
    String register(RegisterDTO data);

    Optional<PharmacyUserDetails> getCurrentUser();
}
