package source.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import source.controllers.entity.User;
import source.database.ChatRepository;
import source.exception.AccStorageException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChatCheckAuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    public ChatCheckAuthInterceptor(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    private final ChatRepository chatRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int chatId = Integer.parseInt(request.getParameter("chatId"));
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
        User activeUser = (User) authenticationToken.getPrincipal();
        if (authUser(chatId, activeUser.getId())) {
            return true;
        }
        response.sendRedirect("chat-list");
        return false;
    }

    public boolean authUser(int chatId, int accId) throws AccStorageException {
        return chatRepository.authChatUser(chatId, accId);
    }
}
