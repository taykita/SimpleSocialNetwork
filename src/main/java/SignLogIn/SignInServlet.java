package SignLogIn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {

    public static Map<String, String> emailPassDB = new HashMap<>();
    public static Map<String, Account> accountDB = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("pass");
        String checkPassword = req.getParameter("chPass");
        if (password.equals(checkPassword)) {
            Account newAccount = new Account(email, password);
            if (emailPassDB.containsKey(newAccount.getEmail())) {
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/sign-in";
                resp.sendRedirect(path);
            } else {
                emailPassDB.put(newAccount.getEmail(), newAccount.getPass());
                accountDB.put(newAccount.getEmail(), newAccount);
                String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/main";
                resp.sendRedirect(path);
            }
        } else {
            String path = req.getScheme() + "://" + req.getServerName() + req.getContextPath() + "/sign-in";
            resp.sendRedirect(path);
        }
    }
}
