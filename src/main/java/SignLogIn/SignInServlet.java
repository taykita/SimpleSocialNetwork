package SignLogIn;

import DataBase.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignInServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        HttpSession session = req.getSession();
        DataBase dataBase = (DataBase) session.getAttribute("DB");
        if (dataBase == null) {
            dataBase = new DataBase();
            session.setAttribute("DB", dataBase);
        }
        String email = req.getParameter("email");
        String password = req.getParameter("pass");
        String checkPassword = req.getParameter("chPass");
        if (password.equals(checkPassword)) {
            Account newAccount = new Account(email, password);
            newAccount.setUserName(req.getParameter("userName"));
            if (dataBase.exist(email)) {
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/sign-in";
                resp.sendRedirect(path);
            } else {
                dataBase.add(newAccount);
                session.setAttribute("email", email);
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/main";
                resp.sendRedirect(path);
            }
        } else {
            String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/sign-in";
            resp.sendRedirect(path);
        }
    }
}
