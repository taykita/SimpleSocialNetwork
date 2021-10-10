package source.controllers.main;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class MainPageController {
    @Autowired
    public MainPageController(MainService mainService) {
        this.mainService = mainService;
    }

    private final MainService mainService;


    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }


    @GetMapping("/main/menu/bottoms/active")
    @ResponseBody
    public SideMenuItems getActiveMenuB() {
        return SideMenuItems.MAIN;
    }
    
    @GetMapping("/main/posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return mainService.getPosts(activeUser.getId(), firstPostId);
    }

    //Можно ли вот так?
    @GetMapping("main/user")
    @ResponseBody
    public Account getMainUser(@AuthenticationPrincipal User activeUser) throws AccStorageException {
        Account account = mainService.getAccount(activeUser);
        account.setAccountSet(null);
        return account;
    }

    @PostMapping("main/upload")
    public String uploadAvatar(@AuthenticationPrincipal User activeUser,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fullName = "user-photos/" + activeUser.getId() + "/avatar.jpg";

        try (InputStream inputStream = multipartFile.getInputStream()) {
            mainService.saveImage(fullName, inputStream);
        } catch (IOException e) {
            throw new IOException("Не могу сохранить файл: " + fullName, e);
        }

        return "redirect:/main";
    }

    @GetMapping("main/load/avatar")
    @ResponseBody
    public byte[] loadAvatar(@AuthenticationPrincipal User activeUser) throws AccStorageException, IOException {

        String fullName = "user-photos/" + activeUser.getId() + "/avatar.jpg";

        return mainService.loadImage(fullName);
    }
}
