package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Content;
import models.User;

import org.h2.util.IOUtils;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import resModles.ResContent;
import resResults.ContentResult;
import utils.ThumbnailGenerator;
import Contants.HttpContants;

import com.google.gson.Gson;

public class ContentController extends Controller{
	
	public static Result list(String user_id, String last_id, String openLevel) {
		ContentResult result = new ContentResult();
		
//		Map<String, String[]> params = request().body().asFormUrlEncoded();
//		Long user_id = Long.parseLong(params.get("user_id")[0]);
//		Long last_id = Long.parseLong(params.get("last_id")[0]);
//    	int openLevel = Integer.parseInt(params.get("openLevel")[0]);
    	
    	List<Content> contents = Content.getContentList(Long.parseLong(user_id), Long.parseLong(last_id), Integer.parseInt(openLevel));
    	if(contents != null) {
    		result.code = HttpContants.OK_200;
    		result.msg = "타임라인 정보를 가져왔습니다.";
    		
//    		List<ResContent> list = new ArrayList<ResContent>();
    		for(Content obj : contents) {
    			ResContent content = new ResContent(obj);
    			result.body.add(content);
    		}
//    		result.body = list;
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
			result.msg = "타임라인 정보가 더이상 존재하지 않습니다."; 
    	}
    	
    	return ok(new Gson().toJson(result));
    }
    
    public static Result upload() {    	
    	ContentResult result = new ContentResult();
 
    	String imageURL1 = "";	 
    	String imageURL2 = "";	 
    	String imageURL3 = "";	 
    	String imageURL4 = "";
    	
    	int num = 1;
    	
    	if (request().body().asMultipartFormData() != null) {
            Map<String, String[]> params = request().body().asMultipartFormData().asFormUrlEncoded();
            
            Long user_id = Long.parseLong(params.get("user_id")[0]);
            String contents = params.get("contents")[0];
            int openLevel = Integer.parseInt(params.get("openLevel")[0]); 
            
            User user = User.getUserInfo(user_id);
            if(user != null) {
            	List<FilePart> uploadFiles = request().body().asMultipartFormData().getFiles();
                 
                for (FilePart part : uploadFiles) {
                        if (part != null) {
                        	 File file = part.getFile();
                             Date date = new Date();
                             SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                             String imageURL = "Images/Contents/" + user_id + "_" + format.format(date) + "_" + String.valueOf(num) + ".JPG"; 
                             String s_imageURL = "Images/Contents/thumbnail_" + user_id + "_" + format.format(date) + "_" + String.valueOf(num) + ".JPG"; 
                             
                             File saveFile = new File(imageURL);
                             FileInputStream is;
                             try {
                                     is = new FileInputStream(file);
                                     IOUtils.copy(is, new FileOutputStream(saveFile));   
                                     
                                     ThumbnailGenerator generator = new ThumbnailGenerator();
                                     generator.transform(imageURL, s_imageURL, 480, 480);  
                                     
                                     if(num == 1) { 
                                    	imageURL1 = s_imageURL;
                                     } else if(num == 2) { 
                                    	imageURL2 = s_imageURL;
                                     } else if(num == 3) { 
                                    	imageURL3 = s_imageURL;
                                     } else if(num == 4) { 
                                    	imageURL4 = s_imageURL;
                                     }
                                     num++;
                             } catch (Exception e) {
								// TODO: handle exception
							}
                        }
                }
                
                Content content = new Content(user, contents, imageURL1, imageURL2, imageURL3, imageURL4, openLevel);
                content.save();
                
                result.code = HttpContants.OK_200;
                result.msg = "성공적으로 업로드 되었습니다.";
                result.body.add(new ResContent(content));
            } else {
            	result.code = HttpContants.FORBIDDEN_403;
                result.msg = "해당 유저가 없습니다.";
            }
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
            result.msg = "멀티파트 형식이 아닙니다.";
    	}                        
    	
    	return ok(new Gson().toJson(result));
    }
    
    public static Result delete() {
    	ContentResult result = new ContentResult();
    	
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	Long content_id = Long.parseLong(params.get("content_id")[0]);     	
    	
    	Content content = Content.getContentDetail(content_id);
    	if(content != null) {
    		if(content.status == 0) {
    			result.code = HttpContants.FORBIDDEN_403;
                result.msg = "이미 삭제된 컨텐츠입니다.";
    		} else {
    			content.status = 0;
    			result.code = HttpContants.OK_200;
                result.msg = "성공적으로 삭제되었습니다.";
    		} 
    	} else {
        	result.code = HttpContants.FORBIDDEN_403;
            result.msg = "해당 유저가 없습니다.";
    	}
    		 
    	return ok(new Gson().toJson(result));
    }
    
    public static Result ban() {
    	ContentResult result = new ContentResult();
    	
    	Map<String, String[]> params = request().body().asFormUrlEncoded();
    	Long user_id = Long.parseLong(params.get("user_id")[0]);   
    	Long content_id = Long.parseLong(params.get("content_id")[0]);    
    	
    	Content content = Content.getContentDetail(content_id);
    	if(content != null) {
    		int viewCount = content.banCount + 1; 
    		content.banCount = viewCount + 1;
    		if(content.banCount > 3) {
    			content.status = 1;
    		}
    		
    		content.update();
    		
    		result.code = HttpContants.OK_200;
    		result.msg = "성공적으로 신고되었습니다.";
    		result.body.add(new ResContent(content));
    	} else {
    		result.code = HttpContants.FORBIDDEN_403;
    		result.msg = "잘못된 게시글이거나 이미 삭제되었습니다.";
    	}
    	
    	return ok(new Gson().toJson(result));
    }

}
