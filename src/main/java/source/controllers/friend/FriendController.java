package source.controllers.friend;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import source.controllers.entity.User;
import source.exception.AccStorageException;

@Controller
public class FriendController {
    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    private final FriendService friendService;

    @PostMapping("/add-friend")
    public String addFriend(@RequestParam int id, @AuthenticationPrincipal User activeUser)
            throws AccStorageException, JsonProcessingException {
        friendService.addFriend(id, activeUser);
        return "redirect:" + "user-page?id=" + id;
    }

    @GetMapping("/delete-friend")
    public String deleteFriend(@RequestParam int id, @AuthenticationPrincipal User activeUser) throws AccStorageException {
        friendService.deleteFriend(id, activeUser);
        return "redirect:" + "friend-list";
    }

}
