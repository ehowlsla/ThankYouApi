package controllers;

import java.util.Map;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import resModles.ResUser;

import com.google.gson.Gson;

public class Application extends Controller {

    
    
    public static Result login(String udid) {
    	User user = User.getUserInfo(udid);    	
    	return ok(new Gson().toJson(user));
    }
    
    public static Result join(String udid, String app_version, String os_version) {
    	//For Build Play Ebean to Mysql
    	ResUser user = new ResUser(User.join(udid, app_version, os_version));
    	return ok(new Gson().toJson(user));	
 
    }
    
    public static Result join() {
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	String udid = params.get("udid")[0];
    	String app_version = params.get("app_version")[0];
    	String os_version = params.get("os_version")[0];
    	  
    	ResUser user = new ResUser(User.join(udid, app_version, os_version));
    	return ok(new Gson().toJson(user));	 
    }
    
    public static Result userUpdate() {
    	return ok();
    }
    
    public static Result contentList() {
    	return ok();
    }
    
    public static Result contentUpload() {
    	return ok();
    }
    
    public static Result contentDelete() {
    	return ok();
    }
    
    public static Result contentBan() {
    	return ok();
    }
    
    public static Result replyList() {
    	return ok();
    }
    
    public static Result replyUpload() {
    	return ok();
    }
    
    public static Result replyDelete() {
    	return ok();
    }
    
    public static Result replyBan() {
    	return ok();
    }
    
    public static Result contentGood() {
    	return ok();
    }
    

}
