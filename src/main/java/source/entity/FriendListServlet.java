package source.entity;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.AccountStorage;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class FriendListServlet extends HttpServlet {

    TemplateEngine templateEngine;
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        accountStorage = (AccountStorage) getServletContext().getAttribute("accountStorage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        Context context = new Context(LOCALE);
        List<Account> allUsers = null;
        allUsers = getFriends(req);

        context.setVariable("users", allUsers.toArray());
        templateEngine.process("friend-list", context, resp.getWriter());
    }

    private List<Account> getFriends(HttpServletRequest req) throws ServletException {
        List<Account> allUsers;
        try {
            allUsers = accountStorage.getFriends(getUserId(req));
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
        return allUsers;
    }

    private int getUserId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("id");
    }
}