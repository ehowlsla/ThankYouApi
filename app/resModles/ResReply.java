package resModles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Reply;
import models.ReplyLike;

public class ResReply {

	public long id;
	public ResUser user;
	public long content_id;
	public String contents;
	public Date createDate;
	public int status;
	public List<ReplyLike> likes;
	
	public ResReply(Reply obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.user = new ResUser(obj.user);
			this.content_id = obj.content_id;
			this.contents = obj.contents;
			this.createDate = obj.createDate;
			this.status = obj.status;
			this.likes = new ArrayList<ReplyLike>();
		}
	}
}
