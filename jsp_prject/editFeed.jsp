<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>�����ϱ�</title>
</head>
<body>
    <form method="post" action="snsController?action=update" enctype="multipart/form-data">
        <input type="hidden" name="fid" value="${feed.fid}">
        <p>
            <label for="content">����:</label>
            <textarea name="content" id="content">${feed.content}</textarea>
        </p>
        <p>
            <label for="img">�̹���:</label>
            <input type="file" name="img" id="img">
        </p>
        <p>
            <img src="${pageContext.request.contextPath}/${feed.img}" alt="���� �̹���" style="max-width: 200px;">
        </p>
        <p>
            <button type="submit">�����ϱ�</button>
        </p>
    </form>
</body>
</html>
