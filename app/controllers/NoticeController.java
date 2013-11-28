package controllers;

import java.util.List;

import models.Notice;
import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResNotice;
import resResults.NoticeResult;
import Contants.HttpContants;

import com.google.gson.Gson;

public class NoticeController extends Controller{

	public static Result list(String user_id, String last_id) {
		NoticeResult result = new NoticeResult(); 
    	
    	List<Notice> notices = Notice.getNotices(Long.parseLong(user_id), Long.parseLong(last_id));
     	if(notices != null) {
    		result.code = HttpContants.OK_200;
    		result.msg = "알림 정보를 가져왔습니다.";
    		 
    		for(Notice obj : notices) {
    			ResNotice value = new ResNotice(obj);
    			result.body.add(value);
    		} 
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "타임라인 정보가 더이상 존재하지 않습니다."; 
    	}
    	 
    	return ok(new Gson().toJson(result));
    }
}
