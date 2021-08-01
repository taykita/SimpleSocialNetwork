package source.verification;

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
import java.io.UnsupportedEncodingException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

//TODO Обработать регистрацию с пустыми полями
//TODO Обработать пробелы
//TODO Добавить ограничение по кол-ву символов
public class SignIn extends HttpServlet {
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

        String error = req.getParameter("error");

        Context context = new Context(LOCALE);
        if (error != null && error.equals("1")) {
            context.setVariable("error", "На этот email уже зарегистрирован аккаунт");
        }

        templateEngine.process("sign-in", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        configServlet(req, resp);

        String email = req.getParameter("email");
        String password = req.getParameter("pass");
        String checkPassword = req.getParameter("chPass");
        if (password.equals(checkPassword)) {
            String userName = req.getParameter("userName");
            Account newAccount = new Account(email, password, userName);
            registration(req, resp, email, newAccount);
        } else {
            resp.sendRedirect("sign");
        }
    }

    private void configServlet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
    }

    private void registration(HttpServletRequest req, HttpServletResponse resp, String email, Account newAccount) throws IOException, ServletException {
        try {
            if (accountStorage.exist(email)) {
                resp.sendRedirect("sign?error=1");
            } else {
                accountStorage.add(newAccount);
                req.getSession().setAttribute("id", newAccount.getId());
                resp.sendRedirect("main");
            }
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
    }

}
