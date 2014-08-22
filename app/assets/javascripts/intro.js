$(function() {
	scroll_resize();

    $('#join-btn').click(function(){
        // 정규식 - 이메일 유효성 검사
        var regEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    	var email = $("[name=join-email]").val();
    	var password1 = $("[name=join-password_1]").val();
    	var password2 = $("[name=join-password_2]").val();
    	
    	if(!regEmail.test(email)) {
            alert('이메일 주소가 유효하지 않습니다');
            $("[name=join-email]").focus();
            return false;
         	}else{
         		if(password1 != password2){
         			alert('두개의 비밀번호가 일치하지 않습니다.');
         			$("[name=join-password_2]").focus();
                    return false;
         		}else{
         			if(password1 == ""){
         				alert("비밀번호가 비어있습니다.");
         				$("[name=join-password_1]").focus();
         				return false
         			}else{
         				var md5_passowrd = calcMD5(password2);
         				$.ajax({
         					url: "/join",
         					type: "POST",
         					data: {email: email, password : md5_passowrd},
         					dataType: "json",
         					error: function() {
            			
         					},
         					success: function(data) {
         						if(data.code == 200){
         							location.href = '/join_info';
         						}else{
         							alert(data.msg);
         						}
         					}				
         				}); 
         			}
         		}
         	}
       });
    
    
	$("#login-btn").click(function(){
		var email = $("[name=email]").val();
    	var password = $("[name=password]").val();
    	var md5_passowrd = calcMD5(password);
    	$.ajax({
			url: "/login",
			type: "POST",
			data: {email: email, password : md5_passowrd},
			dataType: "json",
			error: function() {
			
			},
			success: function(data) {
				if(data.code == 200){
					location.href = '/opendiary';
				}else{
					alert("아이디 혹은 비밀번호가 틀렸습니다.");
				}
			}				
		});
	});
	
});


function scroll_resize(){
	$(window).resize(function() {
		if($(window).height() <= 748) {
			$("footer").fadeOut(0);
		}else{
			$("footer").fadeIn(0);
		}
	});
}