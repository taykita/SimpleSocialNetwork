package source.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class UserPageController {
    @Autowired
    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/user-page")
    public String userPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam Integer id,
                           Model model) throws AccStorageException {

        if (isActiveUser(activeUser, id)) {
            return "redirect:/main";
        }

        Account user = userService.getAccount(activeUser.getId());


        updateModel(user, model, id, activeUser.getId());

        return "user-page";
    }

    private boolean isActiveUser(User activeUser, int id) {
        return id == activeUser.getId();
    }

    private void updateModel(Account activeUser, Model model, int id, int activeUserId) throws AccStorageException {
        Account friend = userService.getAccount(id);

        model.addAttribute("name", friend.getName());
        model.addAttribute("isFriend", isFriend(activeUser, friend));
        model.addAttribute("id", id);
        model.addAttribute("activeUserId", activeUserId);
        model.addAttribute("active", SideMenuItems.NONE);
    }

    private boolean isFriend(Account user, Account friend) throws AccStorageException {
        return userService.isFriend(user, friend);
    }

    @GetMapping("/user-page/get-posts")
    @ResponseBody
    public List<Post> getPosts(@RequestParam int id,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return userService.getPosts(id, firstPostId);
    }


}
