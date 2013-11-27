package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

	@Column(columnDefinition = "nvarchar(255)")
	public String title;

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
	 
	
	public static Finder<Long,Content> find = new Finder<Long,Content>(Long.class, Content.class); 

	@ManyToOne
	public User user;
	
	private static int pSize = 20;

	public Content(User user, String title, String content, String imageURL1, String imageURL2, String imageURL3, String imageURL4, int openLevel) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.contents = content;
		this.createDate = new Date();
		this.likeCount = 0; 
		this.status = 1;
		this.user = user;
		this.imageURL1 = imageURL1;
		this.imageURL1 = imageURL2;
		this.imageURL1 = imageURL3;
		this.imageURL1 = imageURL4;
		this.replyCount = 0;
		this.openLevel = openLevel; 
	}
	
	public static List<Content> getContentList (String content_idx, String openLevel) {
		if(content_idx.equals("0"))
			return find.where().eq("status", 1).le("openLevel", Integer.valueOf(openLevel)).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
		else
			return find.where().eq("status", 1).lt("id", Integer.valueOf(content_idx)).le("openLevel", Integer.valueOf(openLevel)).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
	}
	 
	
	public static Content deleteContent(String user_idx, String content_idx) {
		Content content = find.where().eq("user_id", user_idx).eq("id", content_idx).findUnique();
		if(content != null) {
			content.status = 0;
			content.update();
		}
		return content;
	}
	 
	
	public static Content upload (String user_idx, String title, String content, String imageURL1, String imageURL2, String imageURL3, String imageURL4, int openLevel) {
		User user = User.getUserInfo(user_idx);
		Content contents = new Content(user, title, content, imageURL1, imageURL2, imageURL3, imageURL4, openLevel);
		contents.save();
		return contents;
	}
}
