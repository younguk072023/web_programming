<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
	crossorigin="anonymous"></script>
</head>
<body>
	<div class="container w-75 mt-5 mx-auto">
		<h2>${news.title}</h2>
		<hr>
		<div class="card w-75 mx-auto">
			<img class="card-img-top" src="NewsController.nhn?uid=${news.id}">
			<div class="card-body">
				<h4 class="card-title">Date : ${news.date}</h4>
				<p class="card-text">Content: ${news.content}</p>
			</div>
		</div>
		<hr>
		<a href="javascript:history.back()" class="btn btn-primary"> << Back </a>
	</div>
</body>
</html>