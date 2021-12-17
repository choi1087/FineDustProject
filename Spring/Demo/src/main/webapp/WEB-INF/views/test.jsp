<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>test</title>
</head>
<body>

	<c:forEach items="${list}" var="test">
		
	
		nid = ${test.nid}<br>
		link = ${test.link}<br>
		emotion_like = ${test.emotion_like}<br>
		emotion_nice = ${test.emotion_nice}<br>
		emotion_sad = ${test.emotion_sad}<br>
		emotion_angry = ${test.emotion_angry}<br>
		emotion_wantAfter = ${test.emotion_wantAfter}<br>
		male = ${test.male}<br>
		female = ${test.female}<br>
		teen = ${test.teen}<br>
		twenty = ${test.twenty}<br>
		thirty = ${test.thirty}<br>
		fourty = ${test.fourty}<br>
		fifty = ${test.fifty}<br>
		overSixty = ${test.overSixty}<br>
		
	
	</c:forEach>

</body>

</html>