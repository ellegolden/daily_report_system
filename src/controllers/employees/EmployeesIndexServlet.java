package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //EntityManagerの起動
        EntityManager em = DBUtil.createEntityManager();

        //開くページ数を取得(デフォルトは1ページ目)
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
        } //初回はここに来て40行目のint page = 1が入る

        //最大件数と開始位置を指定してメッセージを取得
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class) //取ってくるデータの型を第2引数に指定
                .setFirstResult(15 * (page - 1)) //最初にデータを取る位置を指定
                .setMaxResults(15) //setFirstResultから最大でどこまでデータを取るか指定
                .getResultList(); //取ってきたデータをmessagesに代入

        //全件数を取得
        long employees_count = (long) em.createNamedQuery("getEmployeesCount", Long.class) //countは数字を持ってくるのでlongを指定
                .getSingleResult();

        em.close();

        request.setAttribute("employees", employees);     //リスト(employees)の内容を"employees"に代入
        request.setAttribute("employees_count", employees_count);     //カウントした全件数の数を"employees_count"に代入
        request.setAttribute("page", page);

        /* フラッシュメッセージがセッションスコープにセットされていたら
        リクエストスコープに保存する。(セッションスコープからは削除) */
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);

    }

}

