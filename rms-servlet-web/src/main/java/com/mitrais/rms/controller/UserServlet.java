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
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends AbstractController
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	String path = getTemplatePath(req.getServletPath()+req.getPathInfo());
    	UserDao userDao = UserDaoImpl.getInstance();
    	String pathInfo = "";
    	HttpSession session = req.getSession(true);
    	if(session == null || session.getAttribute("username")== null)
    	{
    		resp.sendRedirect(req.getContextPath() + "/login");
    	}
    	else if ("/list".equalsIgnoreCase(req.getPathInfo())){
    		pathInfo = "/list";
    		List<User> users = userDao.findAll();
    		req.setAttribute("users", users);
    	}
    	else if (req.getPathInfo().contains("/edit")) {
    		pathInfo = "/form";
    		Long userId = Long.parseLong(req.getParameter("userId"));
    		User user = userDao.find(userId).get();
    		req.setAttribute("user", user);
    	} else if (req.getPathInfo().contains("/delete")) {
    		Long userId = Long.parseLong(req.getParameter("userId"));
    		User user = userDao.find(userId).get();
    		boolean isSuccess = userDao.delete(user);
    		List<User> users = userDao.findAll();
    		req.setAttribute("users", users);
    		req.setAttribute("isDeleteSuccess", isSuccess);
    		resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/list");
    		return;
    	} else if ("/form".equalsIgnoreCase(req.getPathInfo())) {
			pathInfo = "/form";
		} else {
			resp.sendRedirect(req.getContextPath() + "/index.jsp");
			return;
		}
    	path = getTemplatePath(req.getServletPath() + pathInfo);
    	RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
    	requestDispatcher.forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
    	System.out.println("test post");
        String username = req.getParameter("userName");
        String password = req.getParameter("password");
        String userId = req.getParameter("userId");
        UserDao userDao = UserDaoImpl.getInstance();
        if (userId == null || userId.isEmpty()) {
        	System.out.println("test add");
        	User user = new User(null,username,password);
        	userDao.save(user);
        }
        else 
        {
        	User user = new User(Long.parseLong(userId),username,password);
        	Boolean res = userDao.update(user);
        }
        List<User> users = userDao.findAll();
		req.setAttribute("users", users);
		resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/list");
    }
    
   
}
