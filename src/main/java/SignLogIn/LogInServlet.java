package SignLogIn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import static SignLogIn.SignInServlet.emailPassDB;

public class LogInServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");

        if (emailPassDB.containsKey(email)) {
            if (emailPassDB.get(email).equals(password)) {
                HttpSession session = req.getSession();
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