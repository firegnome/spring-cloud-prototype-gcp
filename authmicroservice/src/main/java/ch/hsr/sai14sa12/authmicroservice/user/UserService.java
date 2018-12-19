package ch.hsr.sai14sa12.authmicroservice.user;

import ch.hsr.sai14sa12.authmicroservice.user.dto.UserCreationDto;
import ch.hsr.sai14sa12.authmicroservice.user.dto.UserInfoDto;

import java.util.UUID;

interface UserService {
    UserInfoDto getUserInfo(UUID userId);

    ApplicationUser findUserByUsername(String username);

    void createUser(UserCreationDto userCreationDto);
}
