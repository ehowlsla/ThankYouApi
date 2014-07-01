$ ->
	$("#login-btn").click () ->
		email = $("[name=email]").val()
		password = $("[name=password]").val()
		$.ajax(
			url: "/login"
			type: "POST"
			dataType: "json"
			data:
				email: email
				password: password
			error: () ->
				alert '로그인이 되지 않았습니다'
			success: (data) ->
				if data.code is 200
					location.href = '/home'
				else
					alert "아이디 혹은 비밀번호가 틀렸습니다."
			)
			