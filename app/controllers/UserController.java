package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


		return ok(new Gson().toJson(result));	 
	}

	public static Result join2() {    	
		LoginResult result = new LoginResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String udid = params.get("udid")[0];
		String app_version = params.get("app_version")[0];
		String os_version = params.get("os_version")[0]; 
		String phone = params.get("phone")[0]; 
		String device_id = params.get("device_id")[0]; 

		String strPhone = phone;

		if("0".equals(phone)) {
			strPhone = String.valueOf(System.currentTimeMillis());
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(strPhone.getBytes());
				strPhone = String.valueOf(thedigest);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		User tmp = User.getUserInfoPhoneDeviceID(strPhone, device_id);
		if(tmp == null) { 
			tmp = User.join(udid, app_version, os_version, strPhone, device_id);
		}


		ResUser user = new ResUser(tmp); 

		if(user != null) {
			result.code = HttpContants.OK_200;
			result.msg = "성공적으로 프로필 정보를 가져왔습니다.";            

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


		return ok(new Gson().toJson(result));	 
	}


	public static Result join_email() {    	
		LoginResult result = new LoginResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String email = params.get("email")[0];
		String password = params.get("password")[0];
		String app_version = params.get("app_version")[0];
		String os_version = params.get("os_version")[0]; 

		User tmp = User.getUserEmail(email);
		if(tmp == null) { 
			tmp = User.join_email(app_version, os_version, email, password);
			ResUser user = new ResUser(tmp); 

			if(user != null) {
				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 프로필 정보를 가져왔습니다.";            
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

	public static Result already_join_email() {    	
		LoginResult result = new LoginResult();

		Map<String, String[]> params = request().body().asFormUrlEncoded();
		Long user_id = Long.parseLong(params.get("user_id")[0]);
		String email = params.get("email")[0];
		String password = params.get("password")[0];
		String app_version = params.get("app_version")[0];
		String os_version = params.get("os_version")[0]; 

		User tmp = User.getUserInfo(user_id);
		if(tmp == null) { 
			tmp = User.join_email(app_version, os_version, email, password);
			ResUser user = new ResUser(tmp); 

			if(user != null) {
				result.code = HttpContants.CONTINUE_100;
				result.msg = "기존의 데이터가 없어서 새롭게 계정을 생성하였습니다.";            
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

			User user = User.getUserEmail(email);
			if(user != null){
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "같은 이메일ID가 이미 존재합니다.";
			}else{
				tmp.email = email;
				tmp.password = password;
				//tmp.update();
				tmp.save();

				result.code = HttpContants.OK_200;
				result.msg = "새로운 이메일 계정이 생성되었습니다.";
				result.body.add(new ResUser(tmp));
			}
		}

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

	public static Result login(String user_id, String udid, String app_version, String os_version) {    	
		LoginResult result = new LoginResult();

		//    	Map<String, String[]> params = request().body().asFormUrlEncoded();
		//    	Long user_id = Long.parseLong(params.get("user_id")[0]);
		//    	String app_version = params.get("app_version")[0];
		//    	String os_version = params.get("os_version")[0];
		//    	if(Float.parseFloat(app_version) < 1.11) {
		//    		result.code = HttpContants.FORBIDDEN_403;
		//            result.msg = "잠시후 새롭게 등록되는 1.11이상의 버전을 사용해주시기 바랍니다. 기존의 앱을 삭제 후 새로 설치하시기 바랍니다.";
		////            result.body.add(resUser);
		//    	} else {
		User user = User.getUserInfoUdid(Long.parseLong(user_id), udid);


		//        	System.out.println("user_id =" + user_id + ", udid = " + udid + " , user.id = " + user.id + ", user.udid = " + user.udid);

		if(user != null) {
			if(user.app_version != app_version || user.os_version != os_version) {
				user.app_version = app_version;
				user.os_version = os_version;
				//user.update();
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
			ResUser resUser = new ResUser(User.join(udid, app_version, os_version)); 

			result.code = HttpContants.CONTINUE_100;
			result.msg = "성공적으로 로그인 되었습니다.";
			result.body.add(resUser);
		}    
		//    	}


		return ok(new Gson().toJson(result));
	}


	public static Result login2(String user_id, String udid, String app_version, String os_version, String phone, String device_id) {    	
		LoginResult result = new LoginResult();

		User user = User.getUserInfoPhoneDeviceID(phone, device_id);

		if(user == null) {
			//업데이트 해야함 
			user = User.getUserInfo(Long.parseLong(user_id));
			if(user != null) {
				user.phone = user.phone;
				user.device_id = user.device_id;
				if(user.app_version != app_version || user.os_version != os_version) {
					user.app_version = app_version;
					user.os_version = os_version;
				}
				//user.update();
				user.save();
				user = User.getUserInfoPhoneDeviceID(phone, device_id);
				List<Notice> notices = Notice.getNotices(user.id, (long) 0);
				for(Notice obj : notices) {
					ResNotice value = new ResNotice(obj);
					result.notices.add(value);
				}

				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 로그인 되었습니다.";
				result.body.add(new ResUser(user));
			}  

		} else {
			result.code = HttpContants.OK_200;
			result.msg = "성공적으로 로그인 되었습니다.";
			result.body.add(new ResUser(user));
		}

		if(user == null) {
			ResUser resUser = new ResUser(User.join(udid, app_version, os_version, phone, device_id)); 

			result.code = HttpContants.CONTINUE_100;
			result.msg = "성공적으로 로그인 되었습니다.";
			result.body.add(resUser);
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
		if(User.getNicknameSize(user_id, nickname) > 0) {
			result.code = HttpContants.FORBIDDEN_403;
			result.msg = "닉네임이 다른사람과 중복됩니다. 다른 닉네임으로 변경해주세요.";
		} else {
			if(user != null) {
				user.memo = memo;
				user.job = job;
				user.birth = birth;
				user.nickname = nickname;
				user.city = city;
				user.gender = Integer.parseInt(gender);
				//user.update(); 
				user.save();

				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 변경되었습니다.";
				result.body.add(new ResUser(user));
			} else {
				result.code = HttpContants.FORBIDDEN_403;
				result.msg = "해당 유저가 없습니다.";
			}
		}


		return ok(new Gson().toJson(result));
	}

	public static Result login_email_auto(String user_id, String app_version, String os_version, String email, String password) {    	
		LoginResult result = new LoginResult();

		User user = User.getUserInfo(Long.parseLong(user_id));    	

		if(user == null) {
			result.code = HttpContants.CONTINUE_100;
			result.msg = "없는 계정입니다.회원가입을 다시해 주세요.";
		}else {
			if(user.status == 0){
				result.code = HttpContants.CONTINUE_100;
				result.msg = "자동로그인 실패 : 탈퇴한 회원입니다.";
			}else{
				result.code = HttpContants.OK_200;
				result.msg = "성공적으로 로그인 되었습니다.";
				result.body.add(new ResUser(user));
			}
		}


		return ok(new Gson().toJson(result));
	}


	public static Result login_email_first(String app_version, String os_version, String email, String password) {    	
		LoginResult result = new LoginResult();

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
					result.body.add(new ResUser(user));
				}else{
					result.code = HttpContants.CONTINUE_100;
					result.msg = "비밀번호가 틀렸습니다.";
				}
			}
		}
		return ok(new Gson().toJson(result));
	}


	public static Result withdraw(String app_version, String os_version, String email) {    	
		LoginResult result = new LoginResult();

		User user = User.getUserEmail(email);    	

		if(user == null) {
			result.code = HttpContants.CONTINUE_100;
			result.msg = "없는 이메일 주소입니다.";
		}else{
			user.status =0;
			//user.update();
			user.save();
			result.code = HttpContants.OK_200;
			result.msg = "탈퇴처리가 완료되었습니다.";
			result.body.add(new ResUser(user));
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

							//user.update();
							user.save();

							result.code = HttpContants.OK_200;
							result.msg = num + "번째 사진이 업로드 되었습니다.";
							result.body.add(new ResUser(user));

							if(preImageURL.length() > 0) {
								File preFile = new File(preImageURL);
								if(preFile.isFile())
									result.msg = num + "번째 사진이 수정되었습니다.";

								//                                        		if(preFile.delete()) {
								//                                        			result.msg = num + "번째 사진이 수정되었습니다.";
								//                                        		}
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

				//user.update();
				user.save();

				result.code = HttpContants.OK_200;
				result.msg = num + "번째 사진이 삭제되었습니다.";
				result.body.add(new ResUser(user));

				//        		if(file.delete()) {
				//        			result.code = HttpContants.OK_200;
				//        			result.msg = num + "번째 사진이 삭제되었습니다.";
				//        			result.body.add(new ResUser(user));
				//        		} else {
				//        			result.code = HttpContants.CONFLICT_409;
				//        			result.msg = num + "번째 사진은 이미 삭제되었습니다.";
				//        			result.body.add(new ResUser(user));
				//        		}
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
