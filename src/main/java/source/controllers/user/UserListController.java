package source.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Account;
import source.controllers.entity.User;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class UserListController {
    @Autowired
    public UserListController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/user-list")
    public String userListPage(@AuthenticationPrincipal User activeUser, Model model) throws AccStorageException {
        List<Account> allUsers = userService.getAllAccounts();
        updateModel(activeUser, model, allUsers);
        return "all-users";
    }

    private void updateModel(User activeUser, Model model, List<Account> allUsers) {
        model.addAttribute("users", allUsers);
        model.addAttribute("id", activeUser.getId());
        model.addAttribute("active", SideMenuItems.USERS);
    }
}
