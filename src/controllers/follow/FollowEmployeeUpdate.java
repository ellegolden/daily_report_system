package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.FollowFollower;
import utils.DBUtil;

/**
 * Servlet implementation class FollowEmployeeCreate
 */
@WebServlet("/follow/update")
public class FollowEmployeeUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {

            EntityManager em = DBUtil.createEntityManager();

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");    //ログイン情報を取得
            Employee follow_employee = em.find(Employee.class, request.getParameter("follow"));

            FollowFollower f = em.find(FollowFollower.class, (Integer)(request.getSession().getAttribute("follow")));
            f.setFollower(login_employee);
            f.setFollow(follow_employee);

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "フォローしました。");

            response.sendRedirect(request.getContextPath() + "/employees/index");
        }

    }

}