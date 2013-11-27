package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Content;
import models.Reply;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResReply;
import resResults.ReplyResult;
import Contants.HttpContants;

import com.google.gson.Gson;

public class ReplyController extends Controller{
	
	public static Result list(String content_id, String last_id) {
		ReplyResult result = new ReplyResult();
		
//		Map<String, String[]> params = request().body().asFormUrlEncoded(); 
//		Long content_id = Long.parseLong(params.get("content_id")[0]); 
//		Long last_id = Long.parseLong(params.get("last_id")[0]); 
    	
    	List<Reply> replies = Reply.getContentReplies(Long.parseLong(content_id), Long.parseLong(last_id));
    	if(replies != null) {
    		result.code = HttpContants.OK_200;
    		result.msg = "댓글 정보를 가져왔습니다.";
    		
    		List<ResReply> list = new ArrayList<ResReply>();
    		for(Reply obj : replies) {
    			ResReply reply = new ResReply(obj);
    			list.add(reply);
    		}
    		result.body = list;
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "댓글이 없습니다."; 
    	}
    	
    	return ok(new Gson().toJson(result));
    }
    
    public static Result upload() {
    	ReplyResult result = new ReplyResult();
		
		Map<String, String[]> params = request().body().asFormUrlEncoded(); 
		Long user_id = Long.parseLong(params.get("user_id")[0]); 
		Long content_id = Long.parseLong(params.get("content_id")[0]); 
		String contents = params.get("contents")[0]; 
		
		User user = User.getUserInfo(user_id);
		 
		if(user != null) {
			Content content = Content.getContentDetail(content_id);
			if(content != null) {
				int replyCount = content.replyCount;
				content.replyCount = replyCount + 1;
				content.update();
				
				Reply reply = new Reply(user, content_id, contents);
				reply.save();				

            	result.code = HttpContants.OK_200;
                result.msg = "성공적으로 등록되었습니다.";
			} else {
            	result.code = HttpContants.FORBIDDEN_403;
                result.msg = "해당 게시글이 없습니다.";
			} 
		} else {
			result.code = HttpContants.FORBIDDEN_403;
            result.msg = "해당 유저가 없습니다.";
		}
		
    	return ok(new Gson().toJson(result));
    }
    
    public static Result delete() {
    	ReplyResult result = new ReplyResult();
    	
    	Map<String, String[]> params = request().body().asFormUrlEncoded(); 
		Long user_id = Long.parseLong(params.get("user_id")[0]); 
		Long reply_id = Long.parseLong(params.get("reply_id")[0]); 
		
    	Reply reply = Reply.getReply(reply_id);
    	if(reply != null) {
    		if(reply.user.id == user_id) {
    			reply.status = 0;
    			reply.update();
    			
        		Content content = Content.getContentDetail(reply.content_id);
    			if(content != null) {
    				int replyCount = content.replyCount;
    				if(replyCount > 0) {
    					content.replyCount = replyCount - 1;
    					content.update();
    				}
    			}
    			
    			result.code = HttpContants.OK_200;
                result.msg = "성공적으로 삭제되었습니다.";	
    		} else {
    			result.code = HttpContants.FORBIDDEN_403;
                result.msg = "작성자가 아닙니다.";
    		}
    				
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
            result.msg = "이미 삭제된 댓글입니다.";
    	} 
    	
    	return ok(new Gson().toJson(result));
    }
}
