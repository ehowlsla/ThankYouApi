package resModles;

import java.util.ArrayList;
import java.util.List;

import models.Reply;
import models.ReplyLike;
import models.User;
import utils.TimeConvert;

public class ResReply {

	public long id;
	public ResUser user;
	public long content_id;
	public String contents;
	public String createDate;
	public int status;
	public List<ReplyLike> likes;
	
	public ResReply(Reply obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.user = new ResUser(User.find.where().eq("id", obj.user_id).findUnique());
			this.content_id = obj.content_id;
			this.contents = obj.contents;
			this.createDate = TimeConvert.getDate(obj.createDate);
			this.status = obj.status;
			this.likes = new ArrayList<ReplyLike>();
		}
	}
	 
}
