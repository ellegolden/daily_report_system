<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>日報管理システムへようこそ</h2>
        <h3>【自分の日報 一覧】</h3>
        <table id="report_list">    <%-- table名 report_list --%>
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>      <%--class名 report_name --%>
                    <th class="report_date">日付</th>      <%--class名 report_date --%>
                    <th class="report_title">タイトル</th> <%--class名 report_title --%>
                    <th class="report_action">操作</th>    <%--class名 report_action --%>
                </tr>

                <%-- TopPageIndexServlet List<Report>でセットしたreportsを
                  -- itemsにセットし、<c:forEach>で1件ずつ取り出す --%>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                        <%-- ReportsShowServletに「<c:url value='/reports/show?id=」に取得したIDを与えて遷移する --%>

                    </tr>
                </c:forEach>
            </tbody>
        </table>


        <div id="pagination">
            （全 ${reports_count} 件）<br />
                <%-- カウンタを利用して繰り返し、16件目から2P目に移行させたいので-1 --%>
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                <%--現在表示中のページはリンクを指定しない --%>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                <%-- 各ページにリンクを指定 --%>
                    <c:otherwise>
                        <a href="<c:url value='/?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>