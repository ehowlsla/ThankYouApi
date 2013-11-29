package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.db.ebean.Model.Finder;

import play.db.ebean.Model;

@Entity
public class Like extends Model{
	@Id
	public long id;
	
	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate; 
	 
	public long content_id;
	
	@ManyToOne
	public User user;
	
	private static final int lSize = 30;

	public static Finder<Long,Like> find = new Finder<Long,Like>(Long.class, Like.class); 
	
	public Like(User user, long content_id) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.content_id = content_id; 
		this.createDate = new Date(); 
	}
	
	public static int getUserLike(Long user_id, Long content_id) {
		return find.where().eq("status", 1).eq("user_id", user_id).eq("content_id", content_id).findPagingList(lSize).getPage(0).getList().size();
	}
	
	public static Like getLike (Long user_id, Long content_id) {
		return find.where().eq("status", 1).eq("user_id", user_id).eq("content_id", content_id).findUnique(); 
	}
	
	public static List<Like> getLikes (Long content_id) {		
		return find.where().eq("status", 1).eq("content_id", content_id).orderBy("id desc").findList();			
	}

}
