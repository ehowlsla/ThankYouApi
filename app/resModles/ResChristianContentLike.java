package resModles;

import java.util.Date;

import models.ContentLike;
import models.User;

public class ResChristianContentLike {

	public long id; 
	public Date createDate; 	 
	public long content_id;	 
	public ResUser user;
	
	public ResChristianContentLike(ContentLike obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.createDate = obj.createDate;
			this.content_id = obj.content_id;
			this.user = new ResUser(obj.user);
		}
	}
}
