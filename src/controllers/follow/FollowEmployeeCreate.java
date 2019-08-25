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
@WebServlet("/follow/create")
public class FollowEmployeeCreate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeCreate() {
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

            //jspから渡されたid情報から、employeesテーブルに格納されている社員情報を取得する
            Employee follow_employee = em.find(Employee.class, (Integer.parseInt(request.getParameter("follow_id"))));

            FollowFollower f = new FollowFollower();

            // 変数の省略化として
            // f.setFollow_employee(em.find(Employee.class, (Integer.parseInt(request.getParameter("follow_id")))));
            // でもよい

            f.setFollow_employee(follow_employee);      // JSPから送信されたユーザー情報をインスタンスにセット
            f.setFollower_employee((Employee)request.getSession().getAttribute("login_employee"));      // ログイン者情報をインスタンスにセット

            request.setAttribute("_token", request.getSession().getId());

            em.getTransaction().begin();
            em.persist(f);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush", "フォローしました。");

            response.sendRedirect(request.getContextPath() + "/all_employees/index");
        }

    }

}

//Employee follow = (Employee)request.getSession().getAttribute("follow_id"); 間違い
//f.setFollow_employee_id(follow);　間違い2
//f.setFollow_employee_id(Integer.parseInt(request.getParameter("follow_id"))); 直接入れられないのでem.findでデータを持ってこないといけなかった
//Employee follow_employee = em.find(Employee.class, (request.getParameter("follow_id"));     //em.findじゃなくcreateでテーブルに追加
//FollowFollower f = em.find(FollowFollower.class, (Integer)(request.getSession().getAttribute("follow")));     惜しい、持ってくるのはEmployee.class