package source.verification;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.AccountStorage;
import source.exception.AccStorageException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class LogIn extends HttpServlet {

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
        templateEngine.process("log-in", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        configServlet(req, resp);

        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        checkAcc(req, resp, email, password);

    }

    private void configServlet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
    }

    private void checkAcc(HttpServletRequest req, HttpServletResponse resp, String email, String password) throws IOException, ServletException {
        try {
            if (accountStorage.exist(email)) {
                if (accountStorage.confirmPass(email, password)) {
                    req.getSession().setAttribute("id", accountStorage.get(email).getId());
                    resp.sendRedirect("main");
                } else {
                    resp.sendRedirect("login");
                }
            } else {
                resp.sendRedirect("login");
            }
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
    }

}
