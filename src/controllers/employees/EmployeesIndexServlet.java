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
        } //初回はここに来て42行目のint page = 1が入る

        //最大件数と開始位置を指定して社員情報を取得
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class) //取ってくるデータの型を第2引数に指定
                .setFirstResult(15 * (page - 1)) //最初にデータを取る位置を指定
                .setMaxResults(15) //setFirstResultから最大でどこまでデータを取るか指定
                .getResultList(); //取ってきたデータをリスト(employees)に代入

        //全件数を取得
        long employees_count = (long) em.createNamedQuery("getEmployeesCount", Long.class) //countは数字を持ってくるのでlongを指定
                .getSingleResult();

        em.close();

        request.setAttribute("employees", employees);     // リスト(employees)の内容をリクエストスコープ"employees"にセット
        request.setAttribute("employees_count", employees_count);     // カウントした全件数の数をリクエストスコープ"employees_count"にセット

        // 次回読み込みの際にページングの処理を行うため、現在のpageの値をリクエストスコープにセット
        // ※ここで設定した値が"page = Integer.parseInt(request.getParameter("page"));"
        // で読み込まれ50行目のページングの処理が行われる
        request.setAttribute("page", page);
        request.setAttribute("_token", request.getSession().getId());


        // フラッシュメッセージがセッションスコープにセットされていたら
        //リクエストスコープに保存する。(セッションスコープからは削除)
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);

    }

}

