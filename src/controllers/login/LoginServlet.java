package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //セッションIDをリクエストスコープに保存
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("hasError", false);

        // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープ(flush)に保存する。(セッションスコープからは削除)
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");  //遷移するページをlogin.jspに設定
        rd.forward(request, response);//設定したlogin.jspに遷移
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 認証結果を格納する変数
        Boolean check_result = false;

        String code = request.getParameter("code");     //①
        String plain_pass = request.getParameter("password");   //②

        Employee e = null;  //③

        //codeとplain_passに値が入っていればEntyityManagerを実行し、「password」に暗号化したパスを入れる
        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            EntityManager em = DBUtil.createEntityManager();

            String password = EncryptUtil.getPasswordEncrypt(plain_pass,
                    (String)this.getServletContext().getAttribute("salt"));

            // 社員番号とパスワードが正しいかチェックする
            // :codeと:passに①と②で持ってきたデータをセットし
            // .getSingleResult();で指定した社員のデータを1件だけ取得する
            // データが違うと例外が発生し、cacthで何も指定しないので、
            // ③で設定したのnullのまま④へ入る
            try {
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                        .setParameter("code", code)     //:codeにcodeを設定
                        .setParameter("pass", password)     //:passにpasswordを設定
                        .getSingleResult();
            } catch(NoResultException ex){
            }

            em.close();

            // ④ try-cacthを抜けてが無事データが入っていればtrueを返す
            if(e != null) {
                check_result = true;

            }
        }

        if(!check_result) {     //eがnullのままだとデフォルトのfalseがはいりtrueになる
            // 認証できなかったらログイン画面に戻る
            request.setAttribute("_token", request.getSession().getId());   //現在のセッションIDをリクエストスコープに送る
            request.setAttribute("hasError", true);     // JSP側でフラッシュメッセージを表示するため、trueを"hasError"という名前でセットし、JSP側からメッセージを呼び出す
            request.setAttribute("code", code);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);

        } else {    //認証出来たらログイン状態にしてトップページへリダイレクト

            // eに格納した社員データをlogin_employeeの名前でセッションスコープにセット
            request.getSession().setAttribute("login_employee", e);

            // セッションスコープにフラッシュメッセージをセットしトップページへリダイレクト
            request.getSession().setAttribute("flush", "ログインしました");
            response.sendRedirect(request.getContextPath() + "/");

        }

    }

}
