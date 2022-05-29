package com.authService.service;

import com.authService.model.dto.UserDTO;
import com.authService.model.entity.User;
import com.authService.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public AppUserService(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userEntity = userRepo.
                findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " was not found."));

        return map(userEntity);
    }

    private UserDetails map(User user) {
        Set<GrantedAuthority> grantedAuthorities =
                user.
                        getRoles().
                        stream().
                        map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRole().name())).
                        collect(Collectors.toUnmodifiableSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }

    public void registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepo.save(user);
    }

    public UserDTO findById(Long id) {
        return userRepo
                .findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }
}
