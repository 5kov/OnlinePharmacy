package bg.softuni.onlinepharmacy.service.impl;


import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.model.entity.UserRoleEntity;
import bg.softuni.onlinepharmacy.model.enums.UserRoleEnum;
import bg.softuni.onlinepharmacy.model.user.PharmacyUserDetails;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PharmacyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PharmacyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(PharmacyUserDetailsService::map)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private static UserDetails map(UserEntity userEntity) {

        return new PharmacyUserDetails(
                userEntity.getUuid(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(UserRoleEntity::getRole).map(PharmacyUserDetailsService::map).toList(),
                userEntity.getFirstName(),
                userEntity.getLastName()
        );
    }

    public static GrantedAuthority map(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }
}
