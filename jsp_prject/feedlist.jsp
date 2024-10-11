<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%
	if(session.getAttribute("login_id")==null)
		session.setAttribute("login_id", request.getParameter("id"));
	%>
	
	<table align=center>
		<tr>
			<td colspan=2 align=left height=40><b>건강 관리 데이터</b></td>
			<form method="post" action="write.jsp">
			<td colspan=2 align=right height=40>
				<input type="submit" value="글쓰기"></td>
			</form>
			<form method="post" action="snsController?action=logout">
			<td colspan=2 align=right height=40><input type="submit"
				value="로그아웃"></td>
			</form>
		</tr>
		<c:forEach var="feeds" items="${feedlist}" varStatus="status">
			<tr>
				<td colspan=2 align=left height=40>사용자 : ${feeds.id}</td>
				<td colspan=2 align=right height=40>입력 날짜 : ${feeds.ts}</td>
			</tr>
			<tr>
				<td><img src="${pageContext.request.contextPath}/${feeds.img}"></td>
			</tr>
			<tr>
				<td><b>${feeds.content}</b></td>
			</tr>
			<tr>
				 <td>
                    <a href="snsController?action=delete&fid=${feeds.fid}" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
                </td>
                <td>
                   <a href="snsController?action=edit&fid=${feeds.fid}">수정</a>
                </td>
			</tr>
		</c:forEach>
		
	</table>
</body>
</html>