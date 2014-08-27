package controllers;

import java.text.Normalizer.Form;
import java.util.List;
import java.util.Map;

import models.Content;
import models.ContentLike;
import models.Notice;
import models.Reply;
import models.ReplyLike;
import models.User;
import Contants.HttpContants;
import akka.util.ReentrantGuard;

import com.google.gson.Gson;

import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResContent;
import resModles.ResContentLike;
import resModles.ResNotice;
import resModles.ResReply;
import resModles.ResUser;
import resResults.ContentResult;
import resResults.LoginResult;
import views.html.*;
import views.html.helper.form;

public class Application extends Controller {
	public static Result index() {
		if(session("connected")!= null){
			String user = session("connected");
			if(user != null) {
				return redirect(routes.Application.home());

			} else {
				return ok(index.render("감사일기"));
			}
		}else{
			return ok(index.render("감사일기"));
		}
	}
	
		public static Result today_qt() {
			if(session("connected")!= null){
				String user = session("connected");
				if(user != null) {
					return ok(views.html.today_qt.render(""));

				} else {
					return ok(index.render("감사일기"));
				}
			}else{
				return ok(index.render("감사일기"));
			}
		}


	public static Result home() {
		String email = session("connected");
		LoginResult result = null;
		User user = null;
		ContentResult result2 = null;
		List<Content> contents = null;
		List<Reply> replies = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
			result2 = null;
			contents = Content.getContentList(
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
							
							replies = Reply.getContentReplies(obj.id, (long)0);

							if(replies != null){
								for(Reply ob : replies){
									ResReply reply = new ResReply(ob);
									result2.replies.add(reply);
								}
							}
							
//							List<ContentLike> likes = ContentLike.getLikes(obj.id);
//							if(likes != null){
//								for (ContentLike o : likes) {
//									ResContentLike value = new ResContentLike(o);
//									result2.likes.add(value);
//								}
//							}
						}
					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
				}
			}
		}else{
			return redirect(routes.Application.index());
		}

//		return ok(new Gson().toJson(result2));
		return ok(views.html.home.render(user, result2));
	}
	
	
	public static Result like() {
		ContentResult result = new ContentResult();
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String content_id = params.get("content_id")[0];
		Content content = Content.getContentDetail(Long.parseLong(content_id));
		if (content != null) {

			List<ContentLike> likes = ContentLike.getLikes(Long
					.parseLong(content_id));
			for (ContentLike obj : likes) {
				ResContentLike value = new ResContentLike(obj);
				result.likes.add(value);
			}

			result.code = HttpContants.OK_200;
			result.msg = "성공적으로 일기 정보를 가져왔습니다.";
			result.body.add(new ResContent(content));
		} else {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 일기가 존재하지 않습니다.";
		}

//			return ok(new Gson().toJson(result));

			return ok(views.html.like.render(result));
		}
	
	
	public static Result more() {
		String email = session("connected");
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String target_id = params.get("target_id")[0];
		String target_content_id = params.get("target_content_id")[0];
		String openLevel = params.get("openLevel")[0];
		LoginResult result = null;
		User user = null;
		ContentResult result2 = null;
		List<Content> contents = null;
		List<Reply> replies = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
			result2 = null;
			contents = Content.getContentList(
					user.id, Long.parseLong(target_id),
					Long.parseLong(target_content_id), Integer.parseInt(openLevel));
			if(user == null) {
				result.code = HttpContants.CONTINUE_100;
				result.msg = "없는 이메일 주소입니다.";
			}else{
				if(user.status == 0){
					result.code = HttpContants.CONTINUE_100;
				}else{
					result.code = HttpContants.OK_200;
					result.msg = "성공적으로 가져왔습니다.";
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
							
							replies = Reply.getContentReplies(obj.id, (long)0);

							if(replies != null){
								for(Reply ob : replies){
									ResReply reply = new ResReply(ob);
									result2.replies.add(reply);
								}
							}
						}
					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
				}
			}
		}else{
			return redirect(routes.Application.index());
		}

		return ok(views.html.add.render(user, result2));
	}


	public static Result opendiary() {
		String email = session("connected");
		LoginResult result = null;
		User user = null;
		ContentResult result2 = null;
		List<Content> contents = null;
		List<Reply> replies = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
			result2 = null;
			contents = Content.getContentList(
					user.id, Long.parseLong("0"),
					Long.parseLong("0"), 2);

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

							replies = Reply.getContentReplies(obj.id, (long)0);

							if(replies != null){
								for(Reply ob : replies){
									ResReply reply = new ResReply(ob);
									result2.replies.add(reply);
								}
							}
							result2.body.add(content);
						}

					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
				}
			}
		}else{
			return redirect(routes.Application.index());
		}

//						return ok(new Gson().toJson(result2));	 
		return ok(views.html.opendiary.render(user, result2));
	}


	public static Result neighbordiary(String target_id) {
		String email = session("connected");
		LoginResult result = null;
		User user, target_user = null;
		ContentResult result2 = null;
		List<Content> contents = null;
		List<Reply> replies = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
			result2 = null;
			contents = Content.getContentList(
					user.id, Long.parseLong(target_id),
					Long.parseLong("0"), 2);

			if(user == null) {
				result.code = HttpContants.CONTINUE_100;
				result.msg = "없는 이메일 주소입니다.";
			}else{

				if(user.status == 0){
					result.code = HttpContants.CONTINUE_100;
				}else{
					result.code = HttpContants.OK_200;
					result.body.add(new ResUser(user));
					target_user = target_user.getUserInfo(Long.parseLong(target_id));
					result2 = new ContentResult();
					if (contents != null) {
						result2.code = HttpContants.OK_200;
						result2.msg = "타임라인 정보를 가져왔습니다.";

						for (Content obj : contents) {


							ResContent content = new ResContent(obj);
							content.isLike = ContentLike.getUserLike(
									user.id, obj.id);

							replies = Reply.getContentReplies(obj.id, (long)0);

							if(replies != null){
								for(Reply ob : replies){
									ResReply reply = new ResReply(ob);
									result2.replies.add(reply);
								}
							}
							result2.body.add(content);
						}

					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
				}
			}
		}else{
			return redirect(routes.Application.index());
		}
		//		return ok(new Gson().toJson(result));
		return ok(views.html.neighbordiary.render(user, result2, target_user));
	}
	
	
	public static Result christian() {
		String email = session("connected");
		LoginResult result = null;
		User user, target_user = null;
		ContentResult result2 = null;
		List<Content> contents = null;
		List<Reply> replies = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
			result2 = null;
			contents = Content.getContentList(
					user.id, Long.parseLong("1558"),
					Long.parseLong("0"), 2);

			if(user == null) {
				result.code = HttpContants.CONTINUE_100;
				result.msg = "없는 이메일 주소입니다.";
			}else{

				if(user.status == 0){
					result.code = HttpContants.CONTINUE_100;
				}else{
					result.code = HttpContants.OK_200;
					result.body.add(new ResUser(user));
					target_user = target_user.getUserInfo(Long.parseLong("1558"));
					result2 = new ContentResult();
					if (contents != null) {
						result2.code = HttpContants.OK_200;
						result2.msg = "타임라인 정보를 가져왔습니다.";

						for (Content obj : contents) {


							ResContent content = new ResContent(obj);
							content.isLike = ContentLike.getUserLike(
									user.id, obj.id);

							replies = Reply.getContentReplies(obj.id, (long)0);

							if(replies != null){
								for(Reply ob : replies){
									ResReply reply = new ResReply(ob);
									result2.replies.add(reply);
								}
							}
							result2.body.add(content);
						}

					} else {
						result.code = HttpContants.FORBIDDEN_403;
						result.msg = "타임라인 정보가 더이상 존재하지 않습니다.";
					}
				}
			}
		}else{
			return redirect(routes.Application.index());
		}
		return ok(views.html.christian.render(user, result2, target_user));
	}



	public static Result login_web() { 
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


	public static Result join_web() {    	
		LoginResult result = new LoginResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String email = params.get("email")[0];
		String password = params.get("password")[0];

		User tmp = User.getUserEmail(email);
		if(tmp == null) { 
			tmp = User.join_email_web(email, password);
			ResUser user = new ResUser(tmp); 

			if(user != null) {
				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 프로필 정보를 가져왔습니다.";   
				session("connected", user.email);
				result.body.add(user);
				List<Notice> notices = Notice.getNotices(user.id, (long) 0);
				for(Notice obj : notices) {
					ResNotice value = new ResNotice(obj);
					result.notices.add(value);
				} 
			} else {
				result.code = HttpContants.EXPECTATION_FAILED_417;
				result.msg = "알수없는 결과입니다.";
			}
		}else{
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "같은 이메일ID가 이미 존재합니다.";
		}

		return ok(new Gson().toJson(result));	 
	}
	
	
	public static Result join_info() {   
		String email = session("connected");
		LoginResult result = null;
		User user = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
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
				}
			}
		}else{
			return redirect(routes.Application.index());
		}


		return ok(views.html.join.render(user));	 
	}
	
	public static Result profile_image() {   
		String email = session("connected");
		LoginResult result = null;
		User user = null;
		if(email != null){
			result = new LoginResult();
			user = User.getUserEmail(email);
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
				}
			}
		}else{
			return redirect(routes.Application.index());
		}
		return ok(views.html.profile_image.render(user));	 
	}

	public static Result logout() {
		session().clear();
		//	       flash("success", "You've been logged out");
		return redirect(
				routes.Application.index()
				);
	}

}
