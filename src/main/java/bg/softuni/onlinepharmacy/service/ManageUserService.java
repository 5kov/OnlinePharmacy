package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import java.util.List;

public interface ManageUserService {
    List<UserEntity> searchUsers(String username);

    boolean deleteUser(Long userId);

    boolean toggleUserRole(Long userId);

    boolean isLastAdmin();

    UpdateUserDTO findById(Long id);

    String updateUser(UpdateUserDTO updateUserDTO);

    UpdateUserDTO convertToDto(UserEntity userEntity);

    void convertToUser(UpdateUserDTO dto, UserEntity userEntity);

    String updatePassword(UpdateUserDTO updateUserDTO);

    UpdateUserDTO findByUsername(String currentPrincipalName);
}
