var exp = /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/;
$(document).ready(function() {
	$("#send").click(function() {
		if($("#name").val()==""){
			alert("이름을 입력하세요");
			$("#name").focus();
			return false;
		}
		//아이디,비번,전화번호
		if($("#userid").val()==""){
			alert("아이디를 입력하세요");
			$("#userid").focus();
			return false;
		}
		if($("#pwd").val()==""){
			alert("비밀번호를 입력하세요");
			$("#pwd").focus();
			return false;
		}
		if($("#pwd").val()!=$("#pwd_check").val()){
			alert("비밀번호가 일치하지 않습니다");
			$("#pwd_check").focus();
			return false;
		}
		if($("#mail").val()==""){
			alert("이메일을 입력하세요");
			$("#mail").focus();
			return false;
		}
		if(!$("#phone").val().match(exp)){
			alert("전화번호 입력양식이 아닙니다");
			$("#phone").focus();
			return false;
		}
		
		$("#frm").submit();
	});//send
	
		$("#idBtn").click(function(){
			window.open("idCheck.jsp","","width=800 height=500");
		});//idbtn
		
		//아이디중복확인
		$("#idCheckBtn").click(function(){
			if($("#userid").val()==""){
				alert("아이디를 입력하세요");
				$("#userid").focus();
				return false;
			}
			$.ajax({
				type:"post",
				url :"idCheckPro.jsp",
				data : {"userid":$("#userid").val()},
				success:function(value){
					if(value.trim()=="yes"){
						alert("사용 가능한 아이디 입니다.")
						$(opener.document).find("#userid").val($("#userid").val());
						$(opener.document).find("#uid").val($("#userid").val());
						self.close();
					}else{
						alert("사용 불가능한 아이디 입니다.")
					}
				},
				error : function(e){
					alert("error"+e)
				}
				
				});//ajax
		
		})//idCheckBtn
})//document




