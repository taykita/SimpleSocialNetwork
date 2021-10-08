package source.controllers.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
public class MainPageController {
    @Autowired
    public MainPageController(MainService mainService) {
        this.mainService = mainService;
    }

    private final MainService mainService;


    @GetMapping("/main")
    public String mainPage(Model model) {

        updateModel(model);

        return "main";
    }

    private void updateModel(Model model) {
        model.addAttribute("post", new Post());
    }

    @GetMapping("/main/get-active-menu-button")
    @ResponseBody
    public SideMenuItems getActiveMenuB() {
        return SideMenuItems.MAIN;
    }

    @GetMapping("main/get-user-name")
    @ResponseBody
    public String getUserName() throws AccStorageException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User activeUser = (User) authentication.getPrincipal();
        Account user = mainService.getAccount(activeUser);
        return user.getName();
    }
    
    @GetMapping("/main/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return mainService.getPosts(activeUser.getId(), firstPostId);
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
