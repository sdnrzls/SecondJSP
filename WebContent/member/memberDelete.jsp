<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.member.MemberVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.member.MemberDAOImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");//utf-8로한글화
	MemberDAOImpl dao = MemberDAOImpl.getInstance();//getInstance로 하나의 인스터스공유 dao 객체생성
	
	String userid=request.getParameter("userid");//userid 가져옴
	dao.memberDel(userid);//삭제 메소드
	
	ArrayList<MemberVO> arr =dao.memberList();//memberList 를 MemberVO ArrayList에 값을 줌
	int count = dao.memberCount();// 갯수 메소드
	
	//JSON은 자바스크립트의 객체를 표현하는 방식으로, key-value 쌍으로 이루어진 데이터를 전달하기 위한 개방형 표준 포맷이다
	JSONObject mainObject = new JSONObject();//
	JSONArray jarr = new JSONArray();
	
	for(MemberVO vo : arr){
		String mode = vo.getAdmin()==0?"일반회원":"관리자";
		JSONObject obj = new JSONObject();
		obj.put("name",vo.getName());
		obj.put("userid",vo.getUserid());
		obj.put("mail",vo.getMail());
		obj.put("phone",vo.getPhone());
		obj.put("mode",mode);
		jarr.add(obj); //회원 데이터
	}
	
	JSONObject countObj = new JSONObject();
	
	countObj.put("count",count);//회원수
	mainObject.put("jarr",jarr);
	mainObject.put("cnt",countObj);
	
	out.println(mainObject.toString());
%>











