$(function() {

    $('.join-btn').click(function(){
    	alert("dd");
    	
        // 정규식 - 이메일 유효성 검사
    	var email = $("[name=join-email]").val();
    	var password = $("[name=join-password_2]").val();
    	
    	if(!regEmail.test(email)) {
            alert('이메일 주소가 유효하지 않습니다');
            u_email.focus();
            return false;
         	}else{
         		$.ajax({
        			url: "/join",
        			type: "POST",
        			data: {email: $("[name = join-email]").val(), password : $("[name = join-password_2]").val()},
        			dataType: "json",
        			error: function() {
        			
        			},
        			success: function(data) {
        				if(data.code == 200){
        					location.href = '/opendiary';
        				}else{
        					alert("접속에 실패했습니다.");
        				}
        			}				
        		}); 
         		
         	}
       });
});