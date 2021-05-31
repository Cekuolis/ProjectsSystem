package lt.vtmc.back_end.service;

import java.util.List;
import java.util.stream.Collectors;
import lt.vtmc.back_end.domain.User;
import lt.vtmc.back_end.model.UserDTO;
import lt.vtmc.back_end.repos.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        log.trace("Entering method findAll");
        log.info("Returning all users");
        return userRepository.findAll()
                .stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    public UserDTO get(final Long id) {
        log.trace("Entering method get");
        log.debug("Checking if user with id: " + id + " exists");
        log.info("Returning user");
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final UserDTO userDTO) {
        log.trace("Entering method create");
        log.info("Creating user");
        final User user = new User();
        mapToEntity(userDTO, user);
        log.info("User created successfully");

        Long id = userRepository.save(user).getId();
        log.info("Returning user id: " + id);
        return id;
    }

    public void update(final Long id, final UserDTO userDTO) {
        log.trace("Entering method update");
        log.debug("Checking if user with id: " + id + " exists");
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(userDTO, user);
        userRepository.save(user);
        log.info("User updated successfully");
    }

    public void delete(final Long id) {
        log.trace("Entering method delete");
        userRepository.deleteById(id);
        log.info("User with id: " + id + " deleted successfully");
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setMail(user.getMail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setMail(userDTO.getMail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }

}
