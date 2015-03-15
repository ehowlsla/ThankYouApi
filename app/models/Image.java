package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import Contants.OpenLevel;
import play.data.format.Formats;
import play.db.ebean.Model;

@Entity
public class Image extends Model{
	
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



	public int status;
	
	public int open_level;
	
    public int banCount;
	 
	
	public static Finder<Long,Image> find = new Finder<Long,Image>(Long.class, Image.class); 

	@ManyToOne
	public User user;
	
	private static final int pSize = 30;

	public Image(User user, String content) {
		// TODO Auto-generated constructor stub
		this.contents = content;
		this.createDate = new Date();
		this.likeCount = 0; 
		this.status = 1;
		this.user = user;
		this.replyCount = 0;
		this.banCount = 0;
	}
	
	public static Image getContentDetail (Long content_id) {
		return find.where().eq("status", 1).eq("id", content_id).findUnique(); 
	}
	
	public static List<Image> getContentList (Long user_id, Long target_id, Long last_id, int openLevel) {		
		if(target_id == 0) {
				if(last_id == 0)
					return find.where().eq("status", 1).orderBy("id desc").setMaxRows(pSize).findList();
				else
					return find.where().eq("status", 1).lt("id", last_id).orderBy("id desc").setMaxRows(pSize).findList();
		}else {
				if(last_id == 0)
					return find.where().eq("status", 1).orderBy("id desc").setMaxRows(pSize).findList();
				else
					return find.where().eq("status", 1).lt("id", last_id).orderBy("id desc").setMaxRows(pSize).findList();
		} 
	}
}
