package elbo.dotg.api17.service.user;

import elbo.dotg.api17.advice.exception.user.UserNotFoundException;
import elbo.dotg.api17.advice.exception.user.UsernameDuplicationException;
import elbo.dotg.api17.domain.user.Role;
import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.UpdateRequest;
import elbo.dotg.api17.dto.response.user.UserResponse;
import elbo.dotg.api17.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse findUserById(final long id) {
        return UserResponse.from(userRepository.findById(id).orElseThrow(UsernameDuplicationException::new));
    }

    public UserResponse findUser(final long id, SignUpRequest signUpRequest) {
        return UserResponse.from(
                userRepository.findById(id)
                .filter(it -> passwordEncoder.matches(signUpRequest.passwd(), it.getPasswd()))
                .orElseThrow(() -> new RuntimeException("유저 없음."))
        );
    }

    @Transactional
    public void signUp(final SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.username())
                .passwd(passwordEncoder.encode(signUpRequest.passwd()))
                .name(signUpRequest.name())
                .role(Role.ROLE_USER)
                .build();

        checkUsernameDuplication(user.getUsername());
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(final long id, final UpdateRequest updateRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        checkUsernameDuplication(updateRequest.getUsername());

        user.setUserInfo(updateRequest.getUsername(), updateRequest.getName());
    }

    @Transactional
    public void updateUserPassword(long id, final String passwd) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("z"));
        user.setPasswd(passwd);
    }

    @Transactional
    public void deleteUser(final long id) {
        userRepository.deleteById(id);
    }

    public void checkUsernameDuplication(final String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameDuplicationException();
        }
    }
}
