package by.it.academy.controller;

import by.it.academy.pojo.Transaction;
import by.it.academy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/all-transactions")
public class UserController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = {"", "/{page}"}, method = RequestMethod.GET)
    public ModelAndView showUser(@PathVariable(required = false, name = "page") String page, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        PagedListHolder<Transaction> userList;
        if (page == null) {
            userList = new PagedListHolder<Transaction>();
            List<Transaction> usersList = getListOfUsers();
            // Setting the source for PagedListHolder
            userList.setSource(usersList);
            userList.setPageSize(5);
            // Setting PagedListHolder instance to session
            request.getSession().setAttribute("userList", userList);
        } else if (page.equals("prev")) {
            // get the user list from session
            userList = (PagedListHolder<Transaction>) request.getSession().getAttribute("userList");
            // switch to previous page
            userList.previousPage();
        } else if (page.equals("next")) {
            userList = (PagedListHolder<Transaction>) request.getSession().getAttribute("userList");
            // switch to next page
            userList.nextPage();
        } else {
            int pageNum = Integer.parseInt(page);
            userList = (PagedListHolder<Transaction>) request.getSession().getAttribute("userList");
            // set the current page number
            // page number starts from zero in PagedListHolder that's why subtracting 1
            userList.setPage(pageNum - 1);
        }

        mv.setViewName("transaction-all-paged");
        return mv;
    }

    // Dummy method for adding List of Users
    private List<Transaction> getListOfUsers() {
        List<Transaction> users = transactionService.findAllTransactions();
        return users;
    }
}
