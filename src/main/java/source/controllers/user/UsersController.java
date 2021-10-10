package source.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import source.controllers.entity.Account;
import source.exception.AccStorageException;

import java.util.List;

@RestController
public class UsersController {
    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/users")
    public List<Account> getAllUsers() throws AccStorageException {
        return userService.getAllAccounts();
    }

    @PostMapping("/users")
    public void addUser(@RequestParam Account account) throws AccStorageException {
        userService.addAccount(account);
    }

    @GetMapping("/users/{id}")
    public Account getUser(@PathVariable int id) throws AccStorageException {
        return userService.getAccount(id);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) throws AccStorageException {

    }
}
