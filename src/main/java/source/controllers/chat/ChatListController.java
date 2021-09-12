package source.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import source.controllers.entity.Chat;
import source.controllers.entity.User;
import source.controllers.entity.html.SideMenuItems;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import java.util.List;

@Controller
public class ChatListController {
    @Autowired
    public ChatListController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;

    @GetMapping("/chat-list")
    public String chatListPage(@AuthenticationPrincipal User activeUser, Model model) throws AccStorageException {

        List<Chat> allChats = chatRepository.getChats(activeUser.getId());


        updateModel(model, allChats, activeUser.getId());
        return "chat-list";
    }

    private void updateModel(Model model, List<Chat> allChats, int id) {
        model.addAttribute("chats", allChats);
        model.addAttribute("id", id);
        model.addAttribute("active", SideMenuItems.CHAT);
    }
}
