package elbo.dotg.api17.controller.user;

import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.UpdateRequest;
import elbo.dotg.api17.dto.response.user.UserResponse;
import elbo.dotg.api17.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    private ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<UserResponse> findUserById(@PathVariable final long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping(value = "/1/{id}")
    private ResponseEntity<UserResponse> findUserByIdz(@PathVariable final long id, final SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.findUser(id,signUpRequest));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<String> addUser(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void updateUserInfo(@PathVariable final long id, @RequestBody final UpdateRequest updateRequest) {
        userService.updateUser(id, updateRequest);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteUserById(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
