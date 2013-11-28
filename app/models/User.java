package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class User extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long id;
	
//	@Column(columnDefinition = "nvarchar(100)")
//	public String email;
//	
//	@Column(columnDefinition = "nvarchar(100)")
//	public String password;
	
	
	//email, password

	@Column(columnDefinition = "nvarchar(255)")
	public String nickname;

	@Column(columnDefinition = "nvarchar(100)")
	public String udid;

	@Column(columnDefinition = "text")
	public String memo;

//	@Column(columnDefinition = "int")
	public int status;

//	@Column(columnDefinition = "char(1)")
	public int gender;
	
	@Column(columnDefinition = "nvarchar(20)")
	public String birth;

	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate;
	
	@Column(columnDefinition = "nvarchar(100)")
	public String city;
	
	@Column(columnDefinition = "nvarchar(100)")
	public String job;

 
	
	@Column(columnDefinition = "nvarchar(255)")
	public String image_url1;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String image_url2;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String image_url3;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String image_url4;
	
	@Column(columnDefinition = "nvarchar(20)")
	public String app_version;
	
	@Column(columnDefinition = "nvarchar(20)")
	public String os_version;
	
	@Column(columnDefinition = "nvarchar(20)")
	public String token_key;
	
	 
	
	

	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
	
	public User(String udid, String app_version, String os_version) {
		// TODO Auto-generated constructor stub
		this.nickname = "";
		this.memo = "";
		this.status = 'Y';
		this.udid = udid;
		this.status = 1;
		this.gender = 0;
		this.createDate = new Date();
		this.image_url1 = "";
		this.image_url2 = "";
		this.image_url3 = "";
		this.image_url4 = "";
		this.job = "";
		this.birth = "";
		this.app_version = app_version;
		this.os_version = os_version;
		this.token_key = "";
	}
	
	public static User join(String udid, String app_version, String os_version) {
		User user = User.getUserUdid(udid);
		if(user == null) {
			user = new User(udid, app_version, os_version);
			user.save();
			user = User.getUserUdid(udid);
		}  
		
		return user;
	}
	
	public static User getUserUdid(String udid) {
		return find.where().eq("udid", udid).findUnique(); 
	}
	 
	
	public static User getUserInfo(long user_id) {
		return find.where().eq("id", Long.valueOf(user_id)).findUnique(); 
	}
	 
//	public static User update (User user) { 
//		User userInfo = getUserInfo(user.id);
//		if(userInfo != null) {
//			userInfo.nickname = user.nickname;
//			userInfo.memo = user.memo;
//			userInfo.status = user.status;
//			userInfo.udid = user.udid;
//			userInfo.status = user.status;
//			userInfo.gender = user.gender;
//			userInfo.created_at = user.created_at;
//			userInfo.image_url1 = user.image_url1;
//			userInfo.image_url2 = user.image_url2;
//			userInfo.image_url3 = user.image_url3;
//			userInfo.image_url4 = user.image_url4;
//			userInfo.job = user.job;
//			userInfo.birth = user.birth;
//			userInfo.app_version = user.app_version;
//			userInfo.os_version = user.os_version; 
//			userInfo.update();
//		}  
//		return userInfo;
//	}

}
