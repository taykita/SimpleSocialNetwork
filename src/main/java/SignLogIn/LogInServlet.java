package SignLogIn;

import DataBase.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogInServlet extends HttpServlet {

    DataBase dataBase;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DataBase dataBase = (DataBase) session.getAttribute("DB");
        if (dataBase == null) {
            dataBase = new DataBase();
            session.setAttribute("DB", dataBase);
        }

        resp.setContentType("text/html;charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        if (dataBase.exist(email)) {
            if (dataBase.confirmPass(email, password)) {
                session.setAttribute("email", email);
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/main";
                resp.sendRedirect(path);
            } else {
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/";
                resp.sendRedirect(path);
            }
        } else {
            String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/";
            resp.sendRedirect(path);
        }
    }
}

/*
            resp.getWriter().println(email + " " + password + " " + emailPassDB.get(email));
 */