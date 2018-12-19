package ch.hsr.sai14sa12.authmicroservice.user;

import ch.hsr.sai14sa12.authmicroservice.user.dto.UserInfoDto;

class UserTransformer {
    private UserTransformer() {}

    static UserInfoDto applicationUserToUserInfoDto(ApplicationUser user) {
        return new UserInfoDto(user.getId(), user.getUsername());
    }
}
