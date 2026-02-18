package apirestpsp.security;

import apirestpsp.entity.AppUser;
import apirestpsp.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(appUser.getEmail())
            .password(appUser.getPassword())
            .roles("USER")
            .build();
    }
}
