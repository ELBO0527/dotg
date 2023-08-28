package elbo.dotg.api17.service.user;

import elbo.dotg.api17.advice.exception.user.UserNotFoundException;
import elbo.dotg.api17.advice.exception.user.UsernameDuplicationException;
import elbo.dotg.api17.domain.user.Role;
import elbo.dotg.api17.domain.user.User;
import elbo.dotg.api17.dto.request.user.UpdateRequest;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.response.user.UserResponse;
import elbo.dotg.api17.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> findAllUsers(){
        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public UserResponse findUserById(long id){
        User user = userRepository.findById(id).orElseThrow(UsernameDuplicationException::new);
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void signUp(SignUpRequest signUpRequest){
        User user = User.builder()
                .username(signUpRequest.username())
                .passwd(signUpRequest.passwd())
                .name(signUpRequest.name())
                .role(Role.ROLE_USER)
                .build();

        checkUsernameDuplication(user.getUsername());

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(long id, UpdateRequest updateRequest){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        checkUsernameDuplication(updateRequest.getUsername());

        user.setUserInfo(updateRequest.getUsername(), updateRequest.getName());
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public void checkUsernameDuplication(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameDuplicationException();
        }
    }
}
