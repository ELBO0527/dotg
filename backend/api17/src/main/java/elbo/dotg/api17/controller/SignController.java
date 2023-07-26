package elbo.dotg.api17.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/sign")
public class SignController {

    @PostMapping("/signup")
    public ResponseEntity signup(){
        return null;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(){
        return null;
    }
}
