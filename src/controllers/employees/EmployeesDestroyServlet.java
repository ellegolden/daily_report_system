package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesDestroyServlet
 */
@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())){
            EntityManager em = DBUtil.createEntityManager();

            /* セッションスコープに登録されたキー("employee_id")からメッセージのIDを取得して
             * 該当のIDの社員情報1件のみをデータベースから取得 */
            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));

            // 直接データベースのデータを消すわけではなく、
            // setDelete_flag()に1を渡す事で削除した扱いにしてDB上にデータは残す
            e.setDelete_flag(1);
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "登録が完了しました。");

            //セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("employee_id");

            // indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/employees/index");
        }
    }

}
