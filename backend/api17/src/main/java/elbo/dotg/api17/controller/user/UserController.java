package elbo.dotg.api17.controller.user;

import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.UpdateRequest;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.dto.response.user.UserResponse;
import elbo.dotg.api17.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    private ApiResponse<List<UserResponse>> findAllUsers() {
        return success(userService.findAllUsers());
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
    private ResponseEntity<String> addUser(@RequestBody final SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<UserResponse> updateUserInfo(@PathVariable final long id, @RequestBody final UpdateRequest updateRequest) {
        userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(userService.updateUser(id, updateRequest));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<Long> deleteUserById(@PathVariable final long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
