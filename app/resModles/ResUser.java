package resModles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import models.User;

import play.data.format.Formats;

public class ResUser {
 
 
	public long id;
	public String nickname;
	public String udid; 
	public int status;
	public int gender;
	public Date create_at;
	public String image_url;
	public String app_version;
	public String os_version;

	public ResUser(User obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.nickname = obj.nickname;
			this.udid = obj.udid;
			this.image_url = obj.image_url;
			this.status = obj.status;
			this.gender = obj.gender;
			this.create_at = obj.created_at;
			this.app_version = obj.app_version;
			this.os_version = obj.os_version;
		}
	}
}
