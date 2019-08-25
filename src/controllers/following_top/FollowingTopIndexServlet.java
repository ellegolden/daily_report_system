package controllers.following_top;

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
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class FollowingTopIndexServlet
 */
@WebServlet("/following_top/index")
public class FollowingTopIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowingTopIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //EntityManagerの起動
        EntityManager em = DBUtil.createEntityManager();

     // セッションスコープに保存された従業員（ログインユーザ）情報を取得
        Employee following_emp = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

        //開くページ数を取得(デフォルトは1ページ目)
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page =1;
        } //初回はcatchを拾って48行目の page = 1が入る

        //最大件数と開始位置を指定して日報を取得
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class) //取ってくるデータの型を第2引数に指定
                .setParameter("employee", following_emp)   // ログイン情報をNamedQueryのemployeeにセット
                .setFirstResult(15 * (page - 1)) //最初にデータを取る位置を指定
                .setMaxResults(15) //setFirstResultから最大でどこまでデータを取るか指定
                .getResultList(); //取ってきたデータをreportsに代入

        //全件数を取得
        long reports_count = (long) em.createNamedQuery("getMyReportsCount", Long.class) //countは数字を持ってくるのでlongを指定
                .setParameter("employee", following_emp)
                .getSingleResult();     //count1件だけなのでgetSingleResultで結果を取得

        em.close();

        request.setAttribute("emp", following_emp);
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);

        // ログイン後のTOPページにフラッシュメッセージを表示する
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));  // セッションスコープからリクエストスコープに格納場所を移動
            request.getSession().removeAttribute("flush");      //セッションスコープの中を空にする
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/following_top/index.jsp");
        rd.forward(request, response);
    }

}
