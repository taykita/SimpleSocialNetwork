
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import DataBase.DataBase;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class MainServlet extends HttpServlet {

    ThymeleafEngine thymeleafEngine;

    @Override
    public void init() throws ServletException {
        thymeleafEngine = new ThymeleafEngine(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        DataBase dataBase = (DataBase) session.getAttribute("DB");
        if (dataBase == null) {
            dataBase = new DataBase();
            session.setAttribute("DB", dataBase);
        }

        TemplateEngine templateEngine = thymeleafEngine.getTemplateEngine();
        Context context = thymeleafEngine.getContext();
        String id = (String) session.getAttribute("email");
        if (id != null) {
            context.setVariable("name", dataBase.get(id).getUserName());
        } else {
            String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/";
            resp.sendRedirect(path);
        }
        templateEngine.process("main", context, resp.getWriter());
    }

}