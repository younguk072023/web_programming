<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<table align=center>
		<tr>
			<td colspan=2 align=center height=40><b>회원가입</b>
			</td>
		</tr>
		<form method="post" action="snsController?action=signup">
		<tr>
			<td align=right>아이디&nbsp;</td>
			<td><input type="text" name="id" placeholder="Email address"
				required></td>
		</tr>
		<tr>
			<td align=right>패스워드&nbsp;</td>
			<td><input type="password" name="password" required></td>
		</tr>
		<tr>
			<td align=right>이름&nbsp;</td>
			<td><input type="text" name="name" required></td>
		</tr>
		<tr>
			<td colspan=2 align=center height=50><input type="submit"
				value="회원가입하기"></td>
		</tr>
		</form>
	</table>
</body>
</html>