package resModles;

import java.util.Date;

import models.Reply;

public class ResReply {

	public long id;
	public ResUser user;
	public long content_id;
	public String contents;
	public Date createDate;
	public int status;
	
	public ResReply(Reply obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.user = new ResUser(obj.user);
			this.content_id = obj.content_id;
			this.contents = obj.contents;
			this.createDate = obj.createDate;
			this.status = obj.status;
		}
	}
}
