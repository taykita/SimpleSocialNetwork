package source.entity;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.AccountStorage;
import source.exception.AccStorageException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class UserPageServlet extends HttpServlet {

    TemplateEngine templateEngine;
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        accountStorage = (AccountStorage) getServletContext().getAttribute("accountStorage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        Context context = new Context(LOCALE);
        int id = Integer.parseInt(req.getParameter("id"));

        context.setVariable("name", getUserName(id));
        context.setVariable("isFriend", isFriend(getUserId(req), id));
        context.setVariable("id", id);
        templateEngine.process("user-page", context, resp.getWriter());
    }

    private boolean isFriend(int userId, int friendId) throws ServletException {
        try {
            return accountStorage.isFriend(userId, friendId);
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
    }

    private String getUserName(Integer id) throws ServletException {
        try {
            return accountStorage.get(id).getUserName();
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
    }

    private int getUserId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("id");
    }
}