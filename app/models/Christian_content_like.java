/*package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.db.ebean.Model.Finder;

import play.db.ebean.Model;

@Entity
public class Christian_content_like extends Model{
	@Id
	public long id;
	
	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate; 
	 
	public long content_id;
	
	@ManyToOne
	public User user;
	
	private static final int lSize = 30;

	public static Finder<Long,Christian_content_like> find = new Finder<Long,Christian_content_like>(Long.class, Christian_content_like.class); 
	
	public Christian_content_like(User user, long content_id) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.content_id = content_id; 
		this.createDate = new Date(); 
	}
	
	public static int getUserLike(Long user_id, Long content_id) { 
		return find.where().eq("user_id", user_id).eq("content_id", content_id).findList().size();
	}
	
	public static Christian_content_like getLike (Long user_id, Long content_id) {
		return find.where().eq("user_id", user_id).eq("content_id", content_id).findUnique(); 
	}
	
	public static List<Christian_content_like> getLikes (Long content_id) {		
		return find.where().eq("content_id", content_id).orderBy("id desc").findList();			
	}

}*/
