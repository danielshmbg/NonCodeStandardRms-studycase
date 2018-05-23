package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends AbstractController
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String path = getTemplatePath(req.getServletPath());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String username = req.getParameter("userName");
		String password = req.getParameter("password");
		
		try {
			UserDao dao = UserDaoImpl.getInstance();
			User user = dao.findByUserName(username).get();
			if(user.getPassword().equals(password)){  
				HttpSession session = req.getSession(true);
				session.setAttribute("username", username);
				session.setMaxInactiveInterval(30);
				resp.sendRedirect(req.getContextPath()+"/index.jsp");
		        }  
		        else{  
		        	resp.sendRedirect(req.getContextPath() + "/login"); 
		        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/login");
		}

    }
    
    
}
