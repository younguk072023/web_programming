<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%
		session.invalidate();
	%>
	<table align=center>
		<tr>
			<td>
			<b> 로그아웃을 완료하였습니다.</b>
			</td>
		</tr>
		<form method="post" action="snsController?action=login">
		<tr>
			<td colspan=2 align=center height=50><input type="submit"
				value="로그인하기"></td>
		</tr>
		</form>
	</table>
</body>
</html>