package ru.timutkin.socialmediaapi.api.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.socialmediaapi.storage.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(
                userRepository.findByUsername(username)
                        .orElseThrow(
                                () -> new UsernameNotFoundException("User Not Found with username: " + username)
                        ));
    }
}
