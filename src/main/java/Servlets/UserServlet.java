package Servlets;

import ContentProviders.userContentProvider;
import Exceptions.UserNotFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;


@WebServlet(name = "UserServlet",  urlPatterns = { "/users/*" })
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean submitButtonPressed = request.getParameter("submit") != null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String path = (request).getRequestURI();
        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        String context = tokenizer.nextToken();
        String userId = tokenizer.nextToken();
        request.setAttribute("userID", userId);


        try {
            HashMap<String, String> map = new HashMap<>(userContentProvider.getHTMLContentsForUser(userId));
            HashMap<String, String> skills = new HashMap<>(userContentProvider.getUserSkills(userId));
            HashMap<String, String> extraSkills = new HashMap<>(userContentProvider.getExtraSkills(userId));

            request.setAttribute("content", map);
            request.setAttribute("skills", skills);
            request.setAttribute("extraSkills", extraSkills);

            System.out.println(request.getAttribute("content"));
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user.jsp");
            dispatcher.forward(request, response);
        }
        catch (UserNotFoundException e){
            request.setAttribute("exception", e);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/error404.jsp");
            dispatcher.forward(request, response);
        }

    }
}