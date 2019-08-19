<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>日報管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
    </head>
    <body>
        <div id="wrapper">
            <div id="header">
                <div id="header_menu">
                <%-- 「日報管理システム」にTopPageIndexServletのリンクを設定 --%>
                <h1><a href="<c:url value='/' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;

                <%-- セッションスコープにlogin_employeeが入っていて
                  -- admin_flagが1(管理者の場合) のみ従業員管理のリンクを表示する --%>
                <c:if test="${sessionScope.login_employee != null}">
                    <c:if test="${sessionScope.login_employee.admin_flag == 1}">
                        <a href="<c:url value='/employees/index' />">従業員管理</a>&nbsp;
                    </c:if>
                    <a href="<c:url value='/reports/index' />">日報管理</a>&nbsp;
                </c:if>
                </div>

                <%-- ログイン中の"header"にはログインした社員の名前とログアウトのリンクを表示 --%>
                <c:if test="${sessionScope.login_employee != null}">
                    <div id="employee_name">
                        <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout' />">ログアウト</a>
                    </div>
                </c:if>
            </div>
            <%-- 各jspのcontentを表示 --%>
            <div id="content">
                ${param.content}
            </div>
            <div id="footer">
                by Kaoru Nakashima.
            </div>
        </div>
    </body>
</html>