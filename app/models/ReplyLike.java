package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class ReplyLike extends Model {
	@Id
	public long id;
	
	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate; 
	 
	public long reply_id;
	
	@ManyToOne
	public User user;
	
	private static final int rSize = 30;

	public static Finder<Long,ReplyLike> find = new Finder<Long,ReplyLike>(Long.class, ReplyLike.class); 
	
	
	public ReplyLike(User user, long reply_id) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.reply_id = reply_id; 
		this.createDate = new Date(); 
	}
	
	public static int getUserLike(Long user_id, Long reply_id) {
		return find.where().eq("status", 1).eq("user_id", user_id).eq("reply_id", reply_id).findPagingList(rSize).getPage(0).getList().size();
	}
	
	public static ReplyLike getLike (Long user_id, Long reply_id) {
		return find.where().eq("status", 1).eq("user_id", user_id).eq("reply_id", reply_id).findUnique(); 
	}
	
	public static List<ReplyLike> getLikes (Long reply_id) {		
		return find.where().eq("status", 1).eq("reply_id", reply_id) .orderBy("id desc").findList();			
	}

}
