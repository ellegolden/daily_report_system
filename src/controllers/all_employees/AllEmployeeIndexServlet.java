package controllers.all_employees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.employees.FollowChk;
import models.Employee;
import models.FollowFollower;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeeAllIndexServlet
 */
@WebServlet("/all_employees/index")
public class AllEmployeeIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllEmployeeIndexServlet() {
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

        // フォロー情報を回収して、フォローしているかのチェックを行う
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        List<FollowFollower> follow_check_flag = em.createNamedQuery("getMyAllFollows", FollowFollower.class)
                .setParameter("follower", login_employee)
                .getResultList();

        em.close();

        // フォローのチェック
        List<FollowChk> followChks = new ArrayList<FollowChk>();

        // 拡張For文でemployeeの中身に一つずつ処理を行い、フォローされてい場合、
        // フォローされているという情報を新たに付加し、eに格納していく
        for(Employee e: employees) {
            FollowChk fc = new FollowChk();     // フォローチェック用の新しいインスタンスfcを用意
            fc.setEmp(e);   // インスタンスにemployeesの中身を一つセット

            for(FollowFollower ff: follow_check_flag) {     // follow_check_flagで拾ったフォロー情報を1件取得取り出す

                if(e.getId() == ff.getFollow_employee().getId()) {      // フォローしているIDと同一のIDをeから探し
                    fc.setChk(true);                                    // 一致したIDにフォロー情報を付加する
                    break;          // 情報を付加したらfor文(内側)を抜け出す
                }
            }
            followChks.add(fc);     // フォローチェックの処理が終わったらfcに値をセットする
        }

        request.setAttribute("followChks", followChks);     // フォローチェックを終えたリスト(followChks)の内容をリクエストスコープ"followChks"にセット
        request.setAttribute("employees_count", employees_count);     // カウントした全件数の数をリクエストスコープ"employees_count"にセット
        request.setAttribute("page", page);
        request.setAttribute("_token", request.getSession().getId());

        // フラッシュメッセージがセッションスコープにセットされていたら
        //リクエストスコープに保存する。(セッションスコープからは削除)
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/all_employees/index.jsp");
        rd.forward(request, response);
    }

}
