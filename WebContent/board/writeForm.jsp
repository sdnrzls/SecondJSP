<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="writePro.jsp" method="post">
	<table border=1>
		<tr>
			<td>이름</td>
			<td><input type="text" name="writer"></td>
		</tr>
		
		<tr>
			<td>제목</td>
			<td><input type="text" name="subject"></td>
		</tr>
		
		<tr>
			<td>이메일</td>
			<td><input type="email" name="email"></td>
		</tr>
		
		<tr>
			<td>내용</td>
			<td><textarea cols="100" rows="30" name="content"></textarea></td>
		</tr>
		
		<tr>
			<td>비밀번호</td>
			<td><input type="password" name="passwd"></td>
		</tr>
		
		<tr>
		<td colspan="2">
		<input type="submit" value="글쓰기">
		<input type="reset" value="다시작성">
		<input type="button" value="목록보기">
		</td>
		</tr>
		
	</table>
	</form>
</body>
</html>