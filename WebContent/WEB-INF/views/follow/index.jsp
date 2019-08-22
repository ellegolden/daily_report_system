<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- follow/index.jsp --%>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>フォロー 一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>日報</th>
                </tr>
                    <c:choose>
                        <c:when test="${follow_flag == 1}">
                            <c:forEach var="employee" items="${employees}" varStatus="status">
                                <tr class="row${status.count % 2}">
                                <td><c:out value="${employee.code}" /></td>
                                <td><c:out value="${employee.name}" /></td>
                                <td><a href="<c:url value='/employees/show?id=${employee.id}' />">詳細を表示</a></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                    </c:choose>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${employees_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

    </c:param>
</c:import>