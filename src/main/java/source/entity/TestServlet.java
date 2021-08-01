package source.entity;

import org.hibernate.Session;
import org.thymeleaf.TemplateEngine;
import source.database.AccountStorage;
import source.database.hibernate.HibernateUtil;
import source.exception.AccStorageException;
import source.verification.entity.Account;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet {
    TemplateEngine templateEngine;
    AccountStorage accountStorage;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        accountStorage = (AccountStorage) getServletContext().getAttribute("accountStorage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Session session = new HibernateUtil().getSessionFactory().openSession();
        session.beginTransaction();
        /*
 Stock stock = new Stock();
        stock.setStockCode("7052");
        stock.setStockName("PADINI");

        Category category1 = new Category("CONSUMER", "CONSUMER COMPANY");
        Category category2 = new Category("INVESTMENT", "INVESTMENT COMPANY");

        Set<Category> categories = new HashSet<Category>();
        categories.add(category1);
        categories.add(category2);

        stock.setCategories(categories);

        session.save(stock);
         */
        Account account = new Account("321", "321", "321");

        Account account2 = new Account("3214", "3214", "3214");

        account.getAccountSet().add(account2);

        session.save(account);

        /*

        Account userAcc = null;
        try {
            userAcc = accountStorage.get(1);
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }
        Account friendAcc = null;
        try {
            friendAcc = accountStorage.get(2);
        } catch (AccStorageException e) {
            throw new ServletException(e);
        }

        userAcc.getAccountSet().add(friendAcc);
        //friendAcc.getAccountSet().add(userAcc);

        //session.update(userAcc);
        session.update(friendAcc);
*/
        session.getTransaction().commit();
        session.close();
    }
}
