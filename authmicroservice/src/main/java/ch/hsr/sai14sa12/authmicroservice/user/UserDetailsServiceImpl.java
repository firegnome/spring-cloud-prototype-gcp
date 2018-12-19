package ch.hsr.sai14sa12.authmicroservice.user;

import ch.hsr.sai14sa12.authmicroservice.user.dto.UserCreationDto;
import ch.hsr.sai14sa12.authmicroservice.user.dto.UserInfoDto;
import ch.hsr.sai14sa12.authmicroservice.user.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static ch.hsr.sai14sa12.authmicroservice.user.UserTransformer.applicationUserToUserInfoDto;
import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public UserDetailsServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationUserRepository applicationUserRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }

    @Override
    public UserInfoDto getUserInfo(UUID userId) {
        return applicationUserToUserInfoDto(this.applicationUserRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    @Override
    public ApplicationUser findUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username);
    }

    @Override
    public void createUser(UserCreationDto userCreationDto) {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setPassword(bCryptPasswordEncoder.encode(userCreationDto.getPassword()));
        newUser.setUsername(userCreationDto.getUsername());
        applicationUserRepository.save(newUser);
        log.info("Created user with id " + newUser.getId());
    }
}
