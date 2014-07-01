package controllers;

import java.text.Normalizer.Form;
import java.util.List;
import java.util.Map;

import models.Content;
import models.ContentLike;
import models.User;
import Contants.HttpContants;

import com.google.gson.Gson;

import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResContent;
import resModles.ResUser;
import resResults.ContentResult;
import resResults.LoginResult;
import views.html.*;
import views.html.helper.form;

public class Application extends Controller {
	public static Result index() {
		
		String user = session("connected");
		  if(user != null) {
			  return redirect(routes.Application.home());
		    
		  } else {
			  return ok(index.render("감사일기"));
		  }
	}
	
	
	public static Result home() {
		String email = session("connected");
		
		LoginResult result = new LoginResult();
		User user = User.getUserEmail(email);
		ContentResult result2;
		List<Content> contents = Content.getContentList(
				user.id, Long.parseLong("0"),
				Long.parseLong("0"), 1);
		
		
		if(user == null) {
			result.code = HttpContants.CONTINUE_100;
			result.msg = "없는 이메일 주소입니다.";
		}else{

			if(user.status == 0){
				result.code = HttpContants.CONTINUE_100;
			}else{
					result.code = HttpContants.OK_200;
					result.msg = "성공적으로 로그인 되었습니다.";
					result.body.add(new ResUser(user));
					
					
					result2 = new ContentResult();
					if (contents != null) {
						result2.code = HttpContants.OK_200;
						result2.msg = "타임라인 정보를 가져왔습니다.";

						for (Content obj : contents) {

							ResContent content = new ResContent(obj);
							content.isLike = ContentLike.getUserLike(
									user.id, obj.id);
							result2.body.add(content);
						}
					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
			}
		}
		
		
		
		return ok(views.html.home.render(user, contents));
	}
	



	public static Result login() { 
		LoginResult result = new LoginResult();
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String email = params.get("email")[0];
		String password = params.get("password")[0];
		User user = User.getUserEmail(email);    	
		
		
		if(user == null) {
			result.code = HttpContants.CONTINUE_100;
			result.msg = "없는 이메일 주소입니다.";
		}else{

			if(user.status == 0){
				result.code = HttpContants.CONTINUE_100;
			}else{
				if(user.password.equals(password)){
					result.code = HttpContants.OK_200;
					result.msg = "성공적으로 로그인 되었습니다.";
					session("connected", user.email);
					result.body.add(new ResUser(user));
				}else{
					result.code = HttpContants.CONTINUE_100;
					result.msg = "비밀번호가 틀렸습니다.";
				}
			}
		}
		return ok(new Gson().toJson(result));
	}
    
}
