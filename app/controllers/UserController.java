package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Notice;
import models.User;

import org.h2.util.IOUtils;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import resModles.ResNotice;
import resModles.ResUser;
import resResults.LoginResult;
import resResults.UserResult;
import utils.ThumbnailGenerator;
import Contants.HttpContants;

import com.google.gson.Gson;

public class UserController extends Controller{
	 
    
//    public static Result join(String udid, String app_version, String os_version, String token_key) { 
//    	ResUser user = new ResUser(User.join(udid, app_version, os_version, token_key));
//    	return ok(new Gson().toJson(user));	 
//    }
    
    public static Result join() {    	
    	LoginResult result = new LoginResult();
    	
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	String udid = params.get("udid")[0];
    	String app_version = params.get("app_version")[0];
    	String os_version = params.get("os_version")[0]; 
    	
    	User tmp = User.getUserUdid(udid);
    	if(tmp == null) tmp = User.join(udid, app_version, os_version);
    	  	  
    	ResUser user = new ResUser(tmp); 
    	
    	if(user != null) {
    		result.code = HttpContants.OK_200;
            result.msg = "성공적으로 프로필 정보를 가져왔습니다.";
            
            System.out.println(user.udid);
            
            
    		
    		result.body.add(user);
    		
    		System.out.println(result.code);
    		
    		List<Notice> notices = Notice.getNotices(user.id, (long) 0);
    		for(Notice obj : notices) {
    			ResNotice value = new ResNotice(obj);
    			result.notices.add(value);
    		}
    		
    		System.out.println(result.msg);
    	} else {

            System.out.println("22");
            
    		result.code = HttpContants.EXPECTATION_FAILED_417;
            result.msg = "알수없는 결과입니다.";
    	}
    	
    	System.out.println(result.msg);
    	
    	return ok(new Gson().toJson(result));	 
    }
    
    public static Result getProfile(String user_id) {
    	UserResult result = new UserResult();
    	
    	User user = User.getUserInfo(Long.parseLong(user_id));
    	if(user != null) {
    		result.code = HttpContants.OK_200;
            result.msg = "성공적으로 프로필 정보를 가져왔습니다.";
            
    		
            result.body.add(new ResUser(user));
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 유저가 존재하지 않습니다."; 
    	}
    	return ok(new Gson().toJson(result));
    }
    
    public static Result login(String user_id, String app_version, String os_version) {    	
    	LoginResult result = new LoginResult();
    	
//    	Map<String, String[]> params = request().body().asFormUrlEncoded();
//    	Long user_id = Long.parseLong(params.get("user_id")[0]);
//    	String app_version = params.get("app_version")[0];
//    	String os_version = params.get("os_version")[0];
    	
    	User user = User.getUserInfo(Long.parseLong(user_id));
    	if(user != null) {
    		if(user.app_version != app_version || user.os_version != os_version) {
    			user.app_version = app_version;
    			user.os_version = os_version;
    			user.save();
    		}
    		
    		List<Notice> notices = Notice.getNotices(user.id, (long) 0);
    		for(Notice obj : notices) {
    			ResNotice value = new ResNotice(obj);
    			result.notices.add(value);
    		}
    		
    		result.code = HttpContants.OK_200;
            result.msg = "성공적으로 로그인 되었습니다.";
            result.body.add(new ResUser(user));
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 유저가 존재하지 않습니다."; 
    	}    		
    	return ok(new Gson().toJson(result));
    }
    
    public static Result userUpdate() {
    	UserResult result = new UserResult();
    	
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	Long user_id = Long.parseLong(params.get("user_id")[0]);
    	String memo = params.get("memo")[0];
    	String job = params.get("job")[0];
    	String nickname = params.get("nickname")[0];
    	String gender = params.get("gender")[0];

    	String city = params.get("city")[0];
    	String birth = params.get("birth")[0];  
    	
    	
    	User user = User.getUserInfo(user_id);
    	if(user != null) {
    		user.memo = memo;
    		user.job = job;
    		user.birth = birth;
    		user.nickname = nickname;
    		user.city = city;
    		user.gender = Integer.parseInt(gender);
    		user.update(); 
    		
    		result.code = HttpContants.OK_200;
            result.msg = "성공적으로 변경되었습니다.";
            result.body.add(new ResUser(user));
    	} else {
        	result.code = HttpContants.FORBIDDEN_403;
            result.msg = "해당 유저가 없습니다.";
    	}
    		 
    	return ok(new Gson().toJson(result));
    }
    
    public static Result userImageUpdate() {
    	UserResult result = new UserResult();
    	
    	if (request().body().asMultipartFormData() != null) {
            Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
 
            
            long user_id = Long.parseLong(params.get("user_id")[0]);
            int num = Integer.parseInt(params.get("num")[0]);
            
            User user = User.getUserInfo(user_id);
            if(user != null) {
            	List<FilePart> uploadFiles = request().body().asMultipartFormData().getFiles();
                
                for (FilePart part : uploadFiles) {
                        if (part != null) {
                                File file = part.getFile();
                                Date date = new Date();
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                String imageURL = "Images/Users/" + user_id + "_" + format.format(date) + "_" + String.valueOf(num) + ".JPG"; 
                                String s_imageURL = "Images/Users/thumbnail_" + user_id + "_" + format.format(date) + "_" + String.valueOf(num) + ".JPG"; 
                                
                                File saveFile = new File(imageURL);
                                FileInputStream is;
                                try {
                                        is = new FileInputStream(file);
                                        IOUtils.copy(is, new FileOutputStream(saveFile));
                                        ThumbnailGenerator generator = new ThumbnailGenerator();
                                        generator.transform(imageURL, s_imageURL, 480, 480);  
                                        
                                        String preImageURL = "";
                                        
                                        if(num == 1) {
                                        	preImageURL = user.image_url1;
                                        	user.image_url1 = s_imageURL;
                                        } else if(num == 2) {
                                        	preImageURL = user.image_url2;
                                        	user.image_url2 = s_imageURL;
                                        } else if(num == 3) {
                                        	preImageURL = user.image_url3;
                                        	user.image_url3 = s_imageURL;
                                        } else if(num == 4) {
                                        	preImageURL = user.image_url4;
                                        	user.image_url4 = s_imageURL;
                                        }
                                        
                                        user.update();
                                        
                                        result.code = HttpContants.OK_200;
                                        result.msg = num + "번째 사진이 업로드 었습니다.";
                            			result.body.add(new ResUser(user));
                            			
                                        if(preImageURL.length() > 0) {
                                        	File preFile = new File(preImageURL);
                                        	if(preFile.isFile())
                                        		if(preFile.delete()) {
                                        			result.msg = num + "번째 사진이 수정되었습니다.";
                                        		}
                                        }
                                } catch (FileNotFoundException e) {
                                        // TODO Auto-generated catch block
                                	 result.code = HttpContants.EXPECTATION_FAILED_417;
                                     result.msg = "에러가 발생했습니다.\n" + e.getMessage();
                                     e.printStackTrace();
                                } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                	result.code = HttpContants.EXPECTATION_FAILED_417;
                                    result.msg = "에러가 발생했습니다.\n" + e.getMessage(); 
                                	e.printStackTrace();
                                } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                	result.code = HttpContants.EXPECTATION_FAILED_417;
                                    result.msg = "에러가 발생했습니다.\n" + e.getMessage();                                     
                                    e.printStackTrace();
                                } 
                        }
                }                
            } else {
            	result.code = HttpContants.FORBIDDEN_403;
                result.msg = "해당 유저가 없습니다.";
            }            	
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
            result.msg = "첨부된 파일이 없습니다.";
    	}
    	
    	return ok(new Gson().toJson(result));
    }
    	 
    
    public static Result userImageDelete() {

		UserResult result = new UserResult();
		
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	Long user_id = Long.parseLong(params.get("user_id")[0]);
    	int num = Integer.parseInt(params.get("num")[0]);
    	
    	User user = User.getUserInfo(user_id);

    	
    	
    	if(user != null) {
    		
    		String imageURL = "";
        	 
        	if(num == 1) imageURL = user.image_url1;
        	else if(num == 2) imageURL = user.image_url2;
        	else if(num == 3) imageURL = user.image_url3;
        	else if(num == 4) imageURL = user.image_url4;
        	
        	File file = new File(imageURL);
        	if(file.isFile()) {
        		
        		if(num == 1) user.image_url1 = "";
            	else if(num == 2) user.image_url2 = "";
            	else if(num == 3) user.image_url3 = "";
            	else if(num == 4) user.image_url4 = "";
    			 
    			user.update();
    			
        		if(file.delete()) {
        			result.code = HttpContants.OK_200;
        			result.msg = num + "번째 사진이 삭제되었습니다.";
        			result.body.add(new ResUser(user));
        		} else {
        			result.code = HttpContants.CONFLICT_409;
        			result.msg = num + "번째 사진은 이미 삭제되었습니다.";
        			result.body.add(new ResUser(user));
        		}
        	} else {
        		result.code = HttpContants.CONFLICT_409;
    			result.msg = num + "번째 사진은 존재하지 않습니다.";
    			result.body.add(new ResUser(user));
        	} 
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "해당 유저가 존재하지 않습니다."; 
    	}	
    	return ok(new Gson().toJson(result));
    } 
}
