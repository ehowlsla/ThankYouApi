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
public class Reply extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long id;

	@ManyToOne
	public User user;

	public long content_id;

	@Column(columnDefinition = "text")
	public String contents;

	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date create_at;

	public int status;

	private static final int rSize = 30;

	public static Finder<Long,Reply> find = new Finder<Long,Reply>(Long.class, Reply.class); 

	public Reply(User user, Long content_id, String contents) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.content_id = content_id;
		this.contents = contents;
		this.create_at = new Date();
		this.status = 1;
	}	

	public static Reply getReply(Long reply_id) {
		return find.where().eq("status", 1).eq("id", reply_id).findUnique(); 
	}

	public static List<Reply> getContentReplies (Long content_id, Long reply_id) {
		if(reply_id > 0)
			return find.where().eq("content_id", content_id).eq("status", 1).orderBy("id asc").findPagingList(rSize).getPage(0).getList();
		else
			return find.where().eq("content_id", content_id).gt("id", reply_id).eq("status", 1).orderBy("id asc").findPagingList(rSize).getPage(0).getList();
	}
  
}
