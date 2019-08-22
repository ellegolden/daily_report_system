package controllers.follow;

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
import models.FollowFollower;
import utils.DBUtil;

/**
 * Servlet implementation class FollowEmployeeIndex
 */
@WebServlet("/follow")
public class FollowEmployeeIndex extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowEmployeeIndex() {
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
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        //開くページ数を取得(デフォルトは1ページ目)
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page =1;
        } //初回はcatchを拾って48行目の page = 1が入る

        //最大件数と開始位置を指定してフォロワーの情報を取得
        List<FollowFollower> follows = em.createNamedQuery("getMyAllFollows", FollowFollower.class) //取ってくるデータの型を第2引数に指定
                .setParameter("follow", login_employee)   // ログインIDをNamedQueryのfollowにセット
                .setFirstResult(15 * (page - 1)) //最初にデータを取る位置を指定
                .setMaxResults(15) //setFirstResultから最大でどこまでデータを取るか指定
                .getResultList(); //取ってきたデータをfollowsに代入

        //全件数を取得
        long follows_count = (long) em.createNamedQuery("getMyFollowsCount", Long.class) //countは数字を持ってくるのでlongを指定
                .setParameter("follows", login_employee)
                .getSingleResult();     //count1件だけなのでgetSingleResultで結果を取得

        em.close();

        request.setAttribute("follows", follows);
        request.setAttribute("follows", follows_count);
        request.setAttribute("page", page);

        // ログイン後のTOPページにフラッシュメッセージを表示する
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));  // セッションスコープからリクエストスコープに格納場所を移動
            request.getSession().removeAttribute("flush");      //セッションスコープの中を空にする
        }
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}
