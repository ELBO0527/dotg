package elbo.dotg.api17.service.security;

import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.security.PrincipalUserDetails;
import elbo.dotg.api17.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrincipalUserServiceDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(
                        ()->{
                            return new RuntimeException("유저 이름 없음");
                        }
        );
        return new PrincipalUserDetails(principal);
    }
}
