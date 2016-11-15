package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class Notice extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long id;
	
	@Column(columnDefinition = "nvarchar(255)")
	public String imageURL;

	@Column(columnDefinition = "text")
	public String message;
	 
	public long redirect_id;
	
	public long user_id;
	
	public int status;
	
	@Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
	public Date createDate;
	
	private static int nSize = 30;

	public static Finder<Long,Notice> find = new Finder<Long,Notice>(Long.class, Notice.class); 

	public Notice(Long redirect_id, long user_id, String message, String imageURL) {
		// TODO Auto-generated constructor stub
		this.redirect_id = redirect_id;
		this.user_id = user_id;
		this.message = message;
		this.imageURL = imageURL;
		this.createDate = new Date();
		this.status = 1; //no read
	}	
	
	public static void updateRead (Long user_id, Long last_id) {
		if(last_id > 0) {
			for(Notice obj : find.where().eq("user_id", user_id).lt("last_id", last_id).eq("status", 1).findList()) {
				obj.status = 0;
				//obj.update();
				obj.save();
			}
		} else {
			for(Notice obj : find.where().eq("user_id", user_id).eq("status", 1).findList()) {
				obj.status = 0;
				//obj.update();
				obj.save();
			}
		}
		
	}

	 

	public static List<Notice> getNotices (Long user_id, Long last_id) {
		if(last_id > 0)
			return find.where().eq("user_id", user_id).lt("last_id", last_id).orderBy("id desc").setMaxRows(nSize).findList();
		else
			return find.where().eq("user_id", user_id).orderBy("id desc").setMaxRows(nSize).findList();
	}
}
