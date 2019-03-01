package Servlets;

import ContentProviders.userContentProvider;
import Commands.DeleteSkillOfUserCommand;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "delSkill")
public class delSkill extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userID = request.getParameter("userID");
        String name = request.getParameter("name");
        System.out.println("userId: " +userID);
        System.out.println("name: " +name);

        DeleteSkillOfUserCommand command = new DeleteSkillOfUserCommand(userID, name);
        command.execute();

        HashMap<String, String> map = new HashMap<String, String>();
        HashMap<String, String> skills = new HashMap<String, String>();

        map = userContentProvider.getHTMLContentsForUser(userID);
        skills = userContentProvider.getUserSkills(userID);

        request.setAttribute("content", map);
        request.setAttribute("skills",skills);
        request.setAttribute("userID", userID);

        System.out.println(request.getAttribute("content"));
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user.jsp");
        dispatcher.forward(request, response);

    }

}