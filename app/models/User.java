package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class User extends Model{
	
	private static final long serialVersionUID = 1L;

	@Id
	public long id;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String email;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String password;

	@Column(columnDefinition = "nvarchar(255)")
	public String nickname;

	@Column(columnDefinition = "nvarchar(100)")
	public String udid;

	@Column(columnDefinition = "text")
	public String memo;

	@Column(columnDefinition = "int")
	public int status;

	@Column(columnDefinition = "char(1)")
	public int gender;
	
	@Column(columnDefinition = "nvarchar(20)")
	public String birth;

	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date create_date;
	
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
	
	@Column(columnDefinition = "nvarchar(40)", unique=true)
	public String phone;
	
	@Column(columnDefinition = "nvarchar(40)", unique=true)
	public String device_id;
	

	public static Finder<Long,User> find = new Finder<Long,User>(Long.class, User.class); 
	
	public User(String udid, String app_version, String os_version) {
		// TODO Auto-generated constructor stub
		this.nickname = "";
		this.memo = "";
		this.udid = udid;
		this.status = 1;
		this.gender = 0;
		this.create_date = new Date();
		this.image_url1 = "";
		this.image_url2 = "";
		this.image_url3 = "";
		this.image_url4 = "";
		this.job = "";
		this.birth = "";
		this.device_id = "";
		this.phone = "";
		this.app_version = app_version;
		this.os_version = os_version;
		this.token_key = "";
		this.email = null;
		this.password = null;
	}
	
	public User(String udid, String app_version, String os_version, String phone, String device_id) {
		// TODO Auto-generated constructor stub
		this.nickname = "";
		this.memo = "";
		this.udid = udid;
		this.status = 1;
		this.gender = 0;
		this.create_date = new Date();
		this.image_url1 = "";
		this.image_url2 = "";
		this.image_url3 = "";
		this.image_url4 = "";
		this.phone = phone;
		this.device_id = device_id;
		this.job = "";
		this.birth = "";
		this.app_version = app_version;
		this.os_version = os_version;
		this.token_key = "";
		this.email = null;
		this.password = null;
	}
	
	
	public User(String app_version, String os_version, String email, String passowrd) {
		// TODO Auto-generated constructor stub
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String phone = format.format(date);
		String udid = format.format(date) + "_udid";
		String device_id = format.format(date) + "_device_id";
		this.nickname = "";
		this.memo = "";
		this.udid = udid;
		this.status = 1;
		this.gender = 0;
		this.create_date = new Date();
		this.image_url1 = "";
		this.image_url2 = "";
		this.image_url3 = "";
		this.image_url4 = "";
		this.job = "";
		this.birth = "";
		this.device_id = device_id;
		this.phone = phone;
		this.app_version = app_version;
		this.os_version = os_version;
		this.token_key = "";
		this.email = null;
		this.password = null;
	}
	
	public User(String email, String passowrd) {
		// TODO Auto-generated constructor stub
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String phone = format.format(date);
		String udid = format.format(date) + "_udid";
		String device_id = format.format(date) + "_device_id";
		this.nickname = "";
		this.memo = "";
		this.udid = udid;
		this.status = 1;
		this.gender = 0;
		this.create_date = new Date();
		this.image_url1 = "";
		this.image_url2 = "";
		this.image_url3 = "";
		this.image_url4 = "";
		this.job = "";
		this.birth = "";
		this.device_id = device_id;
		this.phone = phone;
		this.app_version = "";
		this.os_version = "";
		this.token_key = "";
		this.email = email;
		this.password = passowrd;
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
	
	public static User join(String udid, String app_version, String os_version, String phone, String device_id) {
		User user = User.getUserUdid(udid);
		if(user == null) {
			user = new User(udid, app_version, os_version, phone, device_id);
			user.save();
			user = User.getUserUdid(udid);
		}  
		
		return user;
	}
	
	public static User join_email(String app_version, String os_version, String email, String password) {
		User user = User.getUserEmail(email);
		if(user == null) {
			user = new User(app_version, os_version, email, password);
			user.save();
			user = User.getUserEmail(email);
		}  
		
		return user;
	}
	
	public static User join_email_web(String email, String password) {
		User user = User.getUserEmail(email);
		if(user == null) {
			user = new User(email, password);
			user.save();
			user = User.getUserEmail(email);
		}  
		
		return user;
	}
	
	public static User getUserEmail(String email) {
		User user = null;
		user = find.where().eq("email", email).findUnique(); 
		return user;
	}
	
	public static User getUserUdid(String udid) {
		User user = find.where().eq("udid", udid).findUnique(); 
		if(user != null)
			if("d41d8cd98f00b204e9800998ecf8427e".equals(user.udid)) user = null;
		return user;
	}
	 
	
	public static User getUserInfo(long user_id) {
		User user = find.where().eq("id",user_id).findUnique(); 
		return user;
	}
	
	public static User getUserEmail(String email, String password) {
		User user = null;
		
		if(!"".equals(email) && email.length() > 0) {
			user = find.where().eq("email", email).findUnique();
			if(!(user.password.equals(password))){
				user = null;
			}
		} 

		return user;
	}
	
	public static User getUserInfoPhoneDeviceID(String phone, String device_id) {
		User user = null;
		
		if(!"0".equals(phone) && phone.length() > 0) {
			user = find.where().eq("phone", phone).findUnique(); 
			if(user == null) {
				if(device_id.length() >0)
					user = find.where().eq("device_id", device_id).findUnique(); 
			}
		} else {
			if(device_id.length() > 0)
				user = find.where().eq("device_id", device_id).findUnique(); 
		}

		return user;
	}
	
	public static User getUserInfoUdid(long user_id, String udid) {
		return find.where().eq("id", user_id).findUnique(); 
	}
	
	public static int getNicknameSize(long user_id, String nickname) {
		return find.where().ne("id", user_id).eq("nickname", nickname).findList().size();
	}
	  
}
