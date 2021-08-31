package source.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import source.controllers.entity.*;
import source.database.AccountRepository;
import source.database.ChatRepository;
import source.enums.SideMenuEnum;
import source.exception.AccStorageException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    public ChatController(AccountRepository accountRepository, SimpMessagingTemplate messagingTemplate, ChatRepository chatRepository) {
        this.accountRepository = accountRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AccountRepository accountRepository;

    @PostMapping("/create-chat")
    public String createChat(@AuthenticationPrincipal User activeUser,
                             @RequestParam int id) throws AccStorageException {
        if (!chatRepository.exist(id)) {
            chatRepository.add(Arrays.asList(activeUser.getId(), id), accountRepository.get(id).getName());
        }

        return "redirect:chat-list";
    }

    @PostMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User activeUser,
                           @RequestParam int id,
                           Model model) throws AccStorageException {

        Chat chat = chatRepository.get(id);

        updateModel(activeUser, model, chat);

        return "chat";
    }

    private void updateModel(User activeUser, Model model, Chat chat) {
        model.addAttribute("name", chat.getName());
        model.addAttribute("userName", activeUser.getUsername());
        model.addAttribute("chatId", chat.getId());
        model.addAttribute("active", SideMenuEnum.NONE);
    }

    @MessageMapping("/chat")
    public void chatHandler(Message message) throws Exception {
        //TODO В параметр user передавать друга
//        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        chatRepository.addMessage(message);
        messagingTemplate.convertAndSendToUser(activeUser.getUsername(),
                "/queue/chat/" + message.getChatId(), message);
    }

    @GetMapping("/chat/get-messages")
    @ResponseBody
    public List<Message> getMessages(@RequestParam(required = false, defaultValue = "1") int firstMessageId,
                                     @RequestParam int chatId) throws AccStorageException {
        return chatRepository.getMessages(chatId, firstMessageId, 10);
    }

}
