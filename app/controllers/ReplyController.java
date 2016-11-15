package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Content;
import models.Notice;
import models.Reply;
import models.ReplyLike;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResContent;
import resModles.ResReply;
import resResults.ContentResult;
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
	
	public static Result replyLike() {
		ReplyResult result = new ReplyResult();
		
		Map<String, String[]> params = request().body().asFormUrlEncoded();
    	Long user_id = Long.parseLong(params.get("user_id")[0]);    
    	Long reply_id = Long.parseLong(params.get("reply_id")[0]);     
    	
    	
    	Reply reply = Reply.getReply(reply_id);
    	if(reply != null) {
    		User user = User.getUserInfo(user_id);
    		if(user != null) {
    			ReplyLike replyLike = ReplyLike.getLike(user_id, reply_id);
        		if(replyLike != null) {
        			replyLike.delete();
        			int likeCount = reply.likeCount;
        			reply.likeCount = likeCount - 1;
        			//reply.update();
        			reply.save();
        			
                    result.msg = "추천을 취소하였습니다.";
        		} else {
        			replyLike = new ReplyLike(user, reply_id);
        			int likeCount = reply.likeCount;
        			reply.likeCount = likeCount + 1;
        			//reply.update();
        			reply.save();
        			
        			String nickname = user.nickname;
        			if(nickname.length() == 0) nickname = "익명";
        			
        			if(user_id != reply.user.id) {
        				Notice notice = new Notice(reply.content_id, reply.user.id, nickname + "님이 댓글을 좋아합니다.", user.image_url1);
        				notice.save();
        			}
        			

        			replyLike.save();
        			
                    result.msg = "추천하였습니다.";
        		}
        		 
        		result.code = HttpContants.OK_200;
                result.body.add(new ResReply(reply));
    		} else {
    			result.code = HttpContants.FORBIDDEN_403;
    			result.msg = "해당 유저가 존재하지 않습니다."; 
    		}
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 댓글이 존재하지 않습니다."; 
    	}
    	
    	 
    	return ok(new Gson().toJson(result));
	}
	
    
    public static Result upload() {
    	ContentResult result = new ContentResult();
		
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
				//content.update();
				content.save();
				
				Reply reply = new Reply(user, content_id, contents);
				reply.save();				
				
            	result.code = HttpContants.OK_200;
                result.msg = "성공적으로 등록되었습니다.";
				result.body.add(new ResContent(content));
				
				List<Reply> replies = Reply.getContentReplies(content.id, (long) 0);
				for(Reply obj : replies) {
					ResReply value = new ResReply(obj);
					result.replies.add(value);
				}
				
				if(user_id != content.user.id) {
					String nickname = user.nickname;
	    			if(nickname.length() == 0) nickname = "익명";
					
					Notice notice = new Notice(content.id, content.user.id, nickname + "님이 일기에 댓글을 달았습니다.", user.image_url1);
					notice.save();
				}
				
				 
				
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
    			//reply.update();
    			reply.save();
    			
        		Content content = Content.getContentDetail(reply.content_id);
    			if(content != null) {
    				int replyCount = content.replyCount;
    				if(replyCount > 0) {
    					content.replyCount = replyCount - 1;
    					//content.update();
    					content.save();
    					result.web_reply_count = content.replyCount;
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
