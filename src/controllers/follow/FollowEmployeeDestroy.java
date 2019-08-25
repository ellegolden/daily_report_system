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
 * Servlet implementation class FollowEmployeeDestroy
 */
@WebServlet("/follow/destroy")
public class FollowEmployeeDestroy extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeDestroy() {
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

            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

            FollowFollower follow_data = (FollowFollower) em.createNamedQuery("getFollowFollower", FollowFollower.class)// follow_followerテーブルの情報を取得
                    .setParameter("follower", login_employee)
                    .setParameter("follow" ,em.find(Employee.class, Integer.parseInt(request.getParameter("unfollow_id"))))
                    .getSingleResult();


            em.getTransaction().begin();
            em.remove(follow_data);     //テーブル内のデータを削除する
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "フォローを解除しました。");

            response.sendRedirect(request.getContextPath() + "/all_employees/index");
        }
    }
}

//Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");    // ログイン情報を取得
//Employee follow_employee = em.find(Employee.class, request.getParameter("unfollow_id"));        // ボタンから送信された情報を取得

//NamedQueryのselect where(login_employee+follow)- getshingleresult→ FollowFollower f→消す