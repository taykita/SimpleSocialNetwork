package source.controllers.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import source.controllers.entity.Account;
import source.controllers.entity.Post;
import source.controllers.entity.User;
import source.database.AccountRepository;
import source.controllers.entity.html.SideMenuItems;
import source.exception.AccStorageException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class MainPageController {
    @Autowired
    public MainPageController(MainService mainService) {
        this.mainService = mainService;
    }

    private final MainService mainService;


    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal User activeUser,
                           Model model) throws AccStorageException {

        Account user = mainService.getAccount(activeUser);

        updateModel(user, model);

        return "main";
    }

    private void updateModel(Account activeUser, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("name", activeUser.getName());
        model.addAttribute("id", activeUser.getId());
        model.addAttribute("active", SideMenuItems.MAIN);
    }

    @GetMapping("/main/get-posts")
    @ResponseBody
    public List<Post> getPosts(@AuthenticationPrincipal User activeUser,
                               @RequestParam(required = false, defaultValue = "1") int firstPostId) throws AccStorageException {

        return mainService.getPosts(activeUser.getId(), firstPostId);
    }

    @PostMapping("main/load")
    public String saveUser(@AuthenticationPrincipal User activeUser,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        String uploadDir = "user-photos/" + activeUser.getId();

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }


        return "redirect:main";
    }



}
