package source.action;

import source.database.AccountStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddFriend extends HttpServlet {
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        accountStorage = (AccountStorage) getServletContext().getAttribute("accountStorage");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getUserId(req);
        int friendId = getFriendId(req);

        accountStorage.addFriend(userId, friendId);

        resp.sendRedirect("user-page?id=" + friendId);
    }

    private Integer getFriendId(HttpServletRequest req) {
        return Integer.parseInt(req.getParameter("id"));
    }

    private int getUserId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("id");
    }
}
