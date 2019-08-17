package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //EntityManagerの起動
        EntityManager em = DBUtil.createEntityManager();

        //開くページ数を取得(デフォルトは1ページ目)
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page =1;
        } //初回はcatchを拾って44行目の page = 1が入る

        //最大件数と開始位置を指定してメッセージを取得
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class) //取ってくるデータの型を第2引数に指定
                .setFirstResult(15 * (page - 1)) //最初にデータを取る位置を指定
                .setMaxResults(15) //setFirstResultから最大でどこまでデータを取るか指定
                .getResultList(); //取ってきたデータをreportsに代入

        //全件数を取得
        long reports_count = (long) em.createNamedQuery("getReportsCount", Long.class) //countは数字を持ってくるのでlongを指定
                .getSingleResult();     //count1件だけなのでgetSingleResultで結果を取得

        em.close();

        request.setAttribute("reports", reports);     //リスト(reports)の内容を"reports"に代入
        request.setAttribute("reports_count", reports_count);     //カウントした全件数の総数を"reports_count"に代入
        request.setAttribute("page", page);

        /* フラッシュメッセージがセッションスコープにセットされていたら
        リクエストスコープに保存する。(セッションスコープからは削除) */
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }

}
