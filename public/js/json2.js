$(function() {


	$("#content-box > a").click( function(e) {

					});
	
	$("#reply_td a").click( function(e) {
				e.preventDefault();
				var reply_id = $(this).attr("name");
				$(document).on('click.control',
								function(e) {
									if ($(e.target).parents('.reply-delete-box').length == 0 || ( $(e.target).attr("class") != undefined && $(e.target).attr("class").index(".reply-delete-box") < 0)) {
										$(".reply-delete-box").remove();
										$(document).unbind('click.control');}
								});

					$("body").append("<div class='reply-delete-box hidden'><ul class='list'><li><a id = 'reply_delete_confirm'>댓글삭제</a></li></ul></div>");
					$(".reply-delete-box").css({
						top : $(this).offset().top + 18,
						left : $(this).offset().left - 40
					});
					$(".reply-delete-box").removeClass("hidden");
					$(".reply-delete-box").fadeIn(300);
					
					$("#reply_delete_confirm").click(function(){
						$.ajax({
							url: "/reply/delete",
							type: "POST",
							data: {user_id : @user.id, reply_id : reply_id},
							dataType: "json",
							success: function(data) {
								$("body").append("<div id='toast'><b>댓글이 삭제되었습니다.</b></div>");
								var timer = setInterval(function () {
									$("#toast").remove();
									clearInterval(timer);
								}, 1000);
								location.reload();
							}
						});
					});
					return false;
			});
	
	
	
	$("[name=like]").click(function() {
		$.ajax({
			url: "/content/like",
			type: "POST",
			data: { content_id: $(this).attr("content_id"), user_id : $(this).attr("user_id") },
			dataType: "json",
			error: function() {
			
			},
			success: function(data) {
				location.reload();
			}				
		}); 			
	});

	$("[name=replying]").keypress(function(e) {
		if(e.which == 13) 
			$(this).parent().find("[name=repple]").click()			
		
			
	});
	
	$("[name=repple]").click(function() {
		$.ajax({
			url: "/reply/upload",
			type: "POST",
			data: {user_id : $(this).attr("user_id"), content_id: $(this).attr("content_id"), contents: $(this).parent().find("[name=replying]").val()},
			dataType: "json",
			error: function() {
			
			},
			success: function(data) {
				location.reload();
			}				
		}); 			
	});
	
	$(".fileData").change(function(){
        readURL(this, $(this).attr('name'));
        
    });

});