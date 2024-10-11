<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>수정하기</title>
</head>
<body>
    <form method="post" action="snsController?action=update" enctype="multipart/form-data">
        <input type="hidden" name="fid" value="${feed.fid}">
        <p>
            <label for="content">내용:</label>
            <textarea name="content" id="content">${feed.content}</textarea>
        </p>
        <p>
            <label for="img">이미지:</label>
            <input type="file" name="img" id="img">
        </p>
        <p>
            <img src="${pageContext.request.contextPath}/${feed.img}" alt="현재 이미지" style="max-width: 200px;">
        </p>
        <p>
            <button type="submit">수정하기</button>
        </p>
    </form>
</body>
</html>
