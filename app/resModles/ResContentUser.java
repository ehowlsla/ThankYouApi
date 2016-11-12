package resModles;

import java.util.Date;

import models.User;

public class ResContentUser {
 
	public long id;
	public String nickname;
	public String image_url1;
	public String image_url2;
	public String image_url3;
	public String image_url4;


	public ResContentUser(User obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.nickname = obj.nickname;
			this.image_url1 = obj.image_url1;
			this.image_url2 = obj.image_url2;
			this.image_url3 = obj.image_url3;
			this.image_url4 = obj.image_url4;
		}
	}
}
