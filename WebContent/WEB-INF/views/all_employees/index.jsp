<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- all_employees/index.jsp --%>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>社員 一覧</h2>
        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>操作</th>
                    <th>フォロー</th>
                </tr>
                <%-- を
                  -- itemsにセットし、<c:forEach>で1件ずつ取り出す --%>
                <c:forEach var="followChk" items="${followChks}">

                <%-- 表が複数行ある時、奇数行と偶数行で違うクラス(tr.row1,tr.row0)
                  -- とする事で、ストライプのように別の背景色のCSSを適用する  --%>
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${followChk.emp.code}" /></td>
                        <td><c:out value="${followChk.emp.name}" /></td>
                        <td>
                            <c:choose>
                            <c:when test="${followChk.chk}">
                                <form method="POST" action="<c:url value='/follow/destroy' />">
                                    <input type="hidden"  name="unfollow_id" value="${followChk.emp.id}" />
                                    <input type="hidden"  name="_token" value="${_token}" />
                                    <button type="submit">フォロー解除</button>
                                </form>

                            </c:when>
                            <c:otherwise>
                                <form method="POST" action="<c:url value='/follow/create' />">
                                    <input type="hidden" name="follow_id" value="${followChk.emp.id}" />
                                    <input type="hidden"  name="_token" value="${_token}" />
                                    <button type="submit">フォロー</button>
                                </form>
                            </c:otherwise>


                            </c:choose>
                        </td>
                        <td><c:choose>
                            <%--Delete_flag()が1であれば、削除済みを表示 --%>
                            <c:when test="${followChk.emp.delete_flag == 1}">
                                    (削除済み)
                                </c:when>

                                 <%--Delete_flag()が0であれば、/showへのリンクを表示 --%>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/show?id=${followChk.emp.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${employees_count} 件) <br />
            <c:forEach var="i" begin="1" end="${((employees_count -1) /15) +1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                       <a href="<c:url value='/all_employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>

        <p><a href="<c:url value='/employees/new' />">新規従業員の登録</a></p>

    </c:param>
</c:import>