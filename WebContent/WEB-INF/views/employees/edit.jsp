<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${employee != null}">
                <h2> id：${employee.id} の従業員情報 編集ページ </h2>
                <p>（パスワードは変更する場合のみ入力してください）</p>

                <form method="POST" action="<c:url value='/employees/update' />">
                    <c:import url="_form.jsp" />
                </form>

                <%-- href="#"でページのトップ(一番上)に移動する
                  -- confirmDestroyでメッセージの削除のダイアログを表示した後に、
                  -- キャンセルなどを選択した後、ページトップに移動する  --%>

                  <%-- onclick属性で、クリック際に「confirmDestroy()」を呼び出す --%>
                <p><a href="#" onclick="confirmDestroy();">この従業員情報を削除する</a></p>
                <form method="POST" action="<c:url value='/employees/destroy' />">
                    <input type="hidden" name="_token" value="${_token}" />
                </form>
                <script>
                function confirmDestroy(){
                    if(confirm("本当に削除してもよろしいですか？")){
                        document.forms[1].submit();
                        }
                    }
                </script>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value='/employees/index' />">一覧に戻る</a></p>
    </c:param>
</c:import>

