package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import Contants.OpenLevel;

import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class Content extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long id;
 

	@Column(columnDefinition = "text")
	public String contents;

	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate;

 
	
	public int likeCount;

	public int replyCount;

	@Column(columnDefinition = "nvarchar(100)")
	public String imageURL1;
	
	@Column(columnDefinition = "nvarchar(100)")
	public String imageURL2;
	
	@Column(columnDefinition = "nvarchar(100)")
	public String imageURL3;
	
	@Column(columnDefinition = "nvarchar(100)")
	public String imageURL4;

	public int status;
	
	public int openLevel;
	
    public int banCount;
	 
	
	public static Finder<Long,Content> find = new Finder<Long,Content>(Long.class, Content.class); 

	@ManyToOne
	public User user;
	
	private static int pSize = 30;

	public Content(User user, String content, String imageURL1, String imageURL2, String imageURL3, String imageURL4, int openLevel) {
		// TODO Auto-generated constructor stub
		this.contents = content;
		this.createDate = new Date();
		this.likeCount = 0; 
		this.status = 1;
		this.user = user;
		this.imageURL1 = imageURL1;
		this.imageURL2 = imageURL2;
		this.imageURL3 = imageURL3;
		this.imageURL4 = imageURL4;
		this.replyCount = 0;
		this.banCount = 0;
		this.openLevel = openLevel; 
	}
	
	public static Content getContentDetail (Long content_id) {
		return find.where().eq("status", 1).eq("id", content_id).findUnique(); 
	}
	
	public static List<Content> getContentList (Long user_id, Long target_id, Long last_id, int openLevel) {
		if(target_id == 0) {
			if(openLevel == OpenLevel.LEVEL_ME) {
				if(last_id == 0)
					return find.where().eq("status", 1).eq("user_id", user_id).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
				else
					return find.where().eq("status", 1).eq("user_id", user_id).lt("id", last_id).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
			} else {
				if(last_id == 0)
					return find.where().eq("status", 1).eq("openLevel", openLevel).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
				else
					return find.where().eq("status", 1).lt("id", last_id).eq("openLevel", openLevel).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
			}
		} else {
			if(openLevel == OpenLevel.LEVEL_ME) {
				if(last_id == 0)
					return find.where().eq("status", 1).eq("user_id", target_id).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
				else
					return find.where().eq("status", 1).eq("user_id", target_id).lt("id", last_id).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
			} else {
				if(last_id == 0)
					return find.where().eq("status", 1).eq("user_id", target_id).eq("openLevel", openLevel).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
				else
					return find.where().eq("status", 1).eq("user_id", target_id).lt("id", last_id).eq("openLevel", openLevel).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
			}
		} 
	}
	 
	
//	public static Content deleteContent(String user_id, String content_id) {
//		Content content = find.where().eq("user_id", user_id).eq("id", content_id).findUnique();
//		if(content != null) {
//			content.status = 0;
//			content.update();
//		}
//		return content;
//	}
//	 
//	
//	public static Content upload (String user_id, String content, String imageURL1, String imageURL2, String imageURL3, String imageURL4, int openLevel) {
//		User user = User.getUserInfo(Long.parseLong(user_id));
//		Content contents = new Content(user, content, imageURL1, imageURL2, imageURL3, imageURL4, openLevel);
//		contents.save();
//		return contents;
//	}
}