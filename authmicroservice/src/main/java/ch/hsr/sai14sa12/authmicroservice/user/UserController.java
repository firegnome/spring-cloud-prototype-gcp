package ch.hsr.sai14sa12.authmicroservice.user;

import ch.hsr.sai14sa12.authmicroservice.user.dto.UserCreationDto;
import ch.hsr.sai14sa12.authmicroservice.user.dto.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserCreationDto userCreationDto) {
        userService.createUser(userCreationDto);
    }

    @GetMapping("/user/{id}")
    public UserInfoDto getUserInfo(@PathVariable("id") UUID userId) {
        return userService.getUserInfo(userId);
    }
}