package elbo.dotg.api17.service;

import elbo.dotg.api17.advice.exception.UsernameAlreadyExistsException;
import elbo.dotg.api17.domain.User.Role;
import elbo.dotg.api17.domain.User.User;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.UpdateRequest;
import elbo.dotg.api17.dto.response.user.UserResponse;
import elbo.dotg.api17.repository.User.UserRepository;
import elbo.dotg.api17.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllUsersTest() {
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .name("User One")
                .role(Role.ROLE_USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .name("User Two")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        StopWatch stopWatch = new StopWatch("Dotg_Users");
        stopWatch.start("findAllUsers");

        List<UserResponse> users = userService.findAllUsers();

        stopWatch.stop();
        long elapsedTime = stopWatch.getTotalTimeMillis();
        System.out.println("총 실행 시간: " + elapsedTime + "ms");
        System.out.println(stopWatch.prettyPrint());

        assertEquals(2, users.size());
        assertEquals(user1.getUsername(), users.get(0).getUsername());
        assertEquals(user1.getName(), users.get(0).getName());
        assertEquals(user2.getUsername(), users.get(1).getUsername());
        assertEquals(user2.getName(), users.get(1).getName());
    }

    @Test
    void findUserByIdTest() {
        User user = User.builder()
                .id(1L)
                .username("user1")
                .name("User One")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponse userResponse = userService.findUserById(1L);

        assertEquals(user.getId(), userResponse.getId());
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getName(), userResponse.getName());
        assertEquals(user.getRole(), userResponse.getRole());

    }

    @Test
    void signUpTest() {
        SignUpRequest signUpRequest = new SignUpRequest("user1", "password", "User One");

        userService.signUp(signUpRequest);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void signUp_throwsUsernameAlreadyExistsException_whenUsernameExists() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest("user1", "password", "name1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(User.builder().build()));

        // when & then
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    void updateUserTest() {
        UpdateRequest updateRequest = new UpdateRequest("new username", "new name");

        User user = User.builder()
                .id(1L)
                .username("user1")
                .name("User One")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.updateUser(1L, updateRequest);

        assertEquals(updateRequest.getUsername(), user.getUsername());
        assertEquals(updateRequest.getName(), user.getName());
    }

    @Test
    @DisplayName("유저정보를_업데이트_해보자")
    void testUpdateUserWithExistingUsername() {
        // given
        long userId = 1L;
        String existingUsername = "existingUsername";
        String newUsername = "newUsername";
        String name = "name";
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setUsername(newUsername);
        updateRequest.setName(name);

        User existingUser = User.builder()
                .id(userId)
                .name("existingName")
                .username(existingUsername)
                .build();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.findByUsername(newUsername)).thenReturn(Optional.of(User.builder().username(newUsername).build()));

        // when, then
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.updateUser(userId, updateRequest));
    }

    @Test
    public void testCheckIfUsernameExistsWithNonExistingUser() {
        // given
        String nonExistingUsername = "nonExistingUser";

        // when & then
        assertDoesNotThrow(() -> userService.checkIfUsernameExists(nonExistingUsername));
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
}