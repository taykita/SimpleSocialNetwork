package source.verification;

import source.database.DataBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignIn extends HttpServlet {
    private DataBase dataBase;

    @Override
    public void init() throws ServletException {
        dataBase = (DataBase) getServletContext().getAttribute("dataBase");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");
        String checkPassword = req.getParameter("chPass");
        if (password.equals(checkPassword)) {
            Account newAccount = new Account(email, password);
            newAccount.setUserName(req.getParameter("userName"));
            if (dataBase.exist(email)) {
                resp.sendRedirect("sign");
            } else {
                dataBase.add(newAccount);
                req.getSession().setAttribute("email", email);
                resp.sendRedirect("main");
            }
        } else {
            resp.sendRedirect("sign");
        }
    }
}
