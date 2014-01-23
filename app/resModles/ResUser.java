package resModles;

import java.util.Date;

import models.User;

public class ResUser {
 
 
	public long id;
	public String nickname;
	public String udid; 
	public int status;
	public String memo;
	public int gender;
	public String city;
	public Date createDate;
	public String image_url1;
	public String image_url2;
	public String image_url3;
	public String image_url4;
	public String birth;
	public String job;
	public String token_key;
	public String app_version;
	public String os_version;
	public String phone;
	public String device_id;

	public ResUser(User obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.nickname = obj.nickname;
			this.udid = obj.udid;
			this.image_url1 = obj.image_url1;
			this.image_url2 = obj.image_url2;
			this.image_url3 = obj.image_url3;
			this.image_url4 = obj.image_url4;
			this.birth = obj.birth;
			this.city = obj.city;
			this.memo = obj.memo;
			this.job = obj.job;
			this.status = obj.status;
			this.gender = obj.gender;
			this.createDate = obj.createDate;
			this.token_key = obj.token_key;
			this.app_version = obj.app_version;
			this.os_version = obj.os_version;
			this.phone = obj.phone;
			this.device_id = obj.device_id;
		}
	}
}
