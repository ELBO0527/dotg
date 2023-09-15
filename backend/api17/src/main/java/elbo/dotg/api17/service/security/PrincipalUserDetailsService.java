package elbo.dotg.api17.service.security;

import elbo.dotg.api17.advice.exception.sign.CustomAuthenticationException;
import elbo.dotg.api17.dto.security.PrincipalUserDetails;
import elbo.dotg.api17.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return new PrincipalUserDetails(
                userRepository.findByUsername(userName)
                        .orElseThrow(CustomAuthenticationException::new)
        );
    }
}
