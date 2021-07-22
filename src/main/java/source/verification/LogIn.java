package source.verification;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import source.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static source.thymeleaf.config.ThymeleafEngineInitializer.LOCALE;

public class LogIn extends HttpServlet {

    TemplateEngine templateEngine;
    DataBase dataBase;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        dataBase = (DataBase) getServletContext().getAttribute("dataBase");
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
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        if (dataBase.exist(email)) {
            if (dataBase.confirmPass(email, password)) {
                req.getSession().setAttribute("id", dataBase.get(email).getId());
                resp.sendRedirect("main");
            } else {
                resp.sendRedirect( "login");
            }
        } else {
            resp.sendRedirect("login");
        }

    }
}
