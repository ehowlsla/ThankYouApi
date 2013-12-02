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
public class ContentLike extends Model{
	@Id
	public long id;
	
	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate; 
	 
	public long content_id;
	
	@ManyToOne
	public User user;
	
	private static final int lSize = 30;

	public static Finder<Long,ContentLike> find = new Finder<Long,ContentLike>(Long.class, ContentLike.class); 
	
	public ContentLike(User user, long content_id) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.content_id = content_id; 
		this.createDate = new Date(); 
	}
	
	public static int getUserLike(Long user_id, Long content_id) { 
		return find.where().eq("user_id", user_id).eq("content_id", content_id).findList().size();
	}
	
	public static ContentLike getLike (Long user_id, Long content_id) {
		return find.where().eq("user_id", user_id).eq("content_id", content_id).findUnique(); 
	}
	
	public static List<ContentLike> getLikes (Long content_id) {		
		return find.where().eq("content_id", content_id).orderBy("id desc").findList();			
	}

}
