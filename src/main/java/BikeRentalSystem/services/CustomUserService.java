package BikeRentalSystem.services;
import BikeRentalSystem.models.UserMeta;
import BikeRentalSystem.models.UserPrincipal;
import BikeRentalSystem.repositories.UserMetaRepo;
import BikeRentalSystem.repositories.UserPrincipalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    UserPrincipalRepo userPrincipalRepo;

    @Autowired
    UserMetaRepo userMetaRepo;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPrincipalRepo.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username or email : " + username)
        );
    }


    public UserMeta updateUserMeta(UserMeta userToUpdate) {
        UserMeta updatedUser = userMetaRepo.save(userToUpdate);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userPrincipal.setUserMeta(userToUpdate);
        return updatedUser;
    }

    //@RolesAllowed("ADMIN")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //public List<UserPrincipal> getAllUsersWithExpiredCredentials() {
    //    return userPrincipalRepo.findByCredentialsNonExpiredIsFalse();
    // }
}
