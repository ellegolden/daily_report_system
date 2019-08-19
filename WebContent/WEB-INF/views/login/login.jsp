<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <c:if test="${hasError}">
            <div id="flush_error">
                社員番号かパスワードが間違っています。
            </div>
        </c:if>
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <%--action属性で、送信先URLを指定する(/login) --%>
        <%--method属性で、送信方法を指定する(POSTはフォームの内容を送信先ページに“送る”)--%>
        <%--input type属性で、フォームの入力形式を決める --%>
        <%--input name属性で決められた名前にフォームに入力された値を紐付けて
        　　actionの送信先のリクエストスコープに送る --%>

        <%--type hidden属性で隠しデータをサーバーに送信する。 --%>
        <%--hidden value属性で指定した値がサーバーへ送信されるが、画面上には表示されない --%>

        <%--label for属性で、部品のidと紐付けるための値を設定できる(今回は文字列を表示するだけ) --%>

        <h2>ログイン</h2>
        <form method="POST" action="<c:url value='/login' /> ">
            <label for="code">社員番号</label><br />
            <input type="text" name="code" value="${code}" />
            <br />
            <br />

            <label for="password">パスワード</label><br />
            <input type="password" name="password">

            <input type="hidden" name="_token" value="${_token}" />
            <button type="submit">ログイン</button>
        </form>
    </c:param>
</c:import>