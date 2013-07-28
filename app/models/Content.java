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

	public int viewCount;
	
	public int recCount;

	public int replyCount;

	@Column(columnDefinition = "nvarchar(100)")
	public String imageURL;

	public int status;
	
	public int is_open_community;
	
	public int is_open_facebook;
	
	public static Finder<Long,Content> find = new Finder<Long,Content>(Long.class, Content.class); 

	@ManyToOne
	public User user;
	
	private static int pSize = 20;

	public Content(User user, String title, String content, String imageURL, int is_open_community, int is_open_facebook) {
		// TODO Auto-generated constructor stub
		this.title = title;
		this.contents = content;
		this.createDate = new Date();
		this.recCount = 0;
		this.viewCount = 0;
		this.status = 1;
		this.user = user;
		this.imageURL = imageURL;
		this.replyCount = 0;
		this.is_open_community = is_open_community;
		this.is_open_facebook = is_open_facebook;
	}
	
	public static List<Content> getContentListAll (String content_idx) {
		if(content_idx.equals("0"))
			return find.where().eq("status", 1).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
		else
			return find.where().eq("status", 1).lt("id", Integer.valueOf(content_idx)).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
	}
	
	public static List<Content> getContentListFacebook (String content_idx) {
		if(content_idx.equals("0"))
			return find.where().eq("status", 1).eq("is_open_facebook", 1).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
		else
			return find.where().eq("status", 1).eq("is_open_facebook", 1).lt("id", Integer.valueOf(content_idx)).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
	}
	
	public static List<Content> getContentListCommunity (String content_idx) {
		if(content_idx.equals("0"))
			return find.where().eq("status", 1).eq("is_open_community", 1).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
		else
			return find.where().eq("status", 1).eq("is_open_community", 1).lt("id", Integer.valueOf(content_idx)).orderBy("id desc").findPagingList(pSize).getPage(0).getList();
	}
	
	public static Content deleteContent(String user_idx, String content_idx) {
		Content content = find.where().eq("user_id", user_idx).eq("id", content_idx).findUnique();
		content.status = 0;
		content.update();
		return content;
	}
	
	public static Content getContentDetail (String content_idx) {
		Content content = find.where().eq("status", 1).eq("id", Long.valueOf(content_idx)).findUnique();
		int count = content.viewCount;
		content.viewCount = count + 1;
		content.update();
		return content;	
	}
	
	public static Content upload (String user_idx, String title, String content, String imageURL, int is_open_community, int is_open_facebook) {
		User user = User.getUserInfo(user_idx);
		Content contents = new Content(user, title, content, imageURL, is_open_community, is_open_facebook);
		contents.save();
		return contents;
	}
}
