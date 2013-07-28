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

	private static final int rSize = 20;

	public static Finder<Long,Reply> find = new Finder<Long,Reply>(Long.class, Reply.class); 

	public Reply(User user, Content content, String contents) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.content_id = content.id;
		this.contents = contents;
		this.create_at = new Date();
		this.status = 1;
	}	


	public static List<Reply> getContentReplies (String content_idx, String reply_idx) {
		if("0".equals(reply_idx))
			return find.where().eq("content_id", Long.parseLong(content_idx)).eq("status", 1).orderBy("id asc").findPagingList(rSize).getPage(0).getList();
		else
			return find.where().eq("content_id", Long.parseLong(content_idx)).gt("id", reply_idx).eq("status", "Y").orderBy("id asc").findPagingList(rSize).getPage(0).getList();
	}
 
	public static Reply upload (String user_idx, String content_idx, String content) {
		User user = User.getUserInfo(user_idx);
		if(user == null) return null;

		Content contents = Content.getContentDetail(content_idx);
		int replyCount = contents.replyCount;
		contents.replyCount = replyCount + 1;
		contents.update();

		Reply reply = new Reply(user, contents, content);
		reply.save();
		return reply;
	}

	public static Reply deleteReply(String user_idx, String reply_idx) {
		Reply reply = find.where().eq("user_id", user_idx).eq("id", reply_idx).findUnique();
		reply.status = 'N';
		reply.save();

		Content contents = Content.getContentDetail(String.valueOf(reply.content_id));
		int replyCount = contents.replyCount;
		if(replyCount > 0) {
			contents.replyCount = replyCount - 1;
			contents.update();
		}

		return reply;
	}
}
