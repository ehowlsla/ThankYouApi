package resModles;

import java.util.Date;

import models.Notice;

public class ResNotice {

	public long id;
	public String imageURL;
	public String message;	 
	public long redirect_id;	
	public long user_id;	
	public int status;	
	public Date createDate;

	public ResNotice(Notice obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.imageURL = obj.imageURL;
			this.message = obj.message;
			this.redirect_id = obj.redirect_id;
			this.user_id = obj.user_id;
			this.status = obj.status;
			this.createDate = obj.createDate;
		}
	}
}
