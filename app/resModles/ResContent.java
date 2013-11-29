package resModles;

import java.util.Date;

import models.Content;

public class ResContent {
	
	public long id;  
	public String contents; 
	public Date createDate;	
	public int likeCount;
	public int replyCount; 
	public String imageURL1;	 
	public String imageURL2;	 
	public String imageURL3;	 
	public String imageURL4;
	public int status;	
	public int openLevel;
	public ResUser user;
	public int isLike;

	public ResContent(Content obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id; 
			this.contents = obj.contents;
			this.createDate = obj.createDate;
			this.likeCount = obj.likeCount;
			this.replyCount = obj.replyCount;
			this.imageURL1 = obj.imageURL1;
			this.imageURL2 = obj.imageURL2;
			this.imageURL3 = obj.imageURL3;
			this.imageURL4 = obj.imageURL4;
			this.status = obj.status;
			this.openLevel = obj.status;
			this.user = new ResUser(obj.user);
			this.isLike = 0;
		}
	}
}
