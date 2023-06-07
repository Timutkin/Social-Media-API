package ru.timutkin.socialmediaapi.api.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.api.dto.SignupRequest;
import ru.timutkin.socialmediaapi.api.exception.InvalidRegistrationDataException;
import ru.timutkin.socialmediaapi.api.service.RegistrationService;

import ru.timutkin.socialmediaapi.storage.entity.RoleEntity;
import ru.timutkin.socialmediaapi.storage.entity.UserEntity;
import ru.timutkin.socialmediaapi.storage.enumeration.Role;
import ru.timutkin.socialmediaapi.storage.repository.RoleRepository;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

import java.util.List;


@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleEntity roleUser;

    public RegistrationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        roleUser = roleRepository.findByRole(Role.ROLE_USER);
    }

    @Override
    @Transactional
    public void register(SignupRequest request) {
        if(Boolean.TRUE.equals(userRepository.existsByUsername(request.getUsername()))){
            throw new InvalidRegistrationDataException("Username is already taken!");
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))){
            throw new InvalidRegistrationDataException("Email is already in use!");
        }
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(roleUser))
                .build();
        userRepository.save(user);
    }
}
