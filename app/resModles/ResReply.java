package resModles;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Reply;
import models.ReplyLike;

public class ResReply {

	public long id;
	public ResUser user;
	public long content_id;
	public String contents;
	public String createDate;
	public int status;
	public List<ReplyLike> likes;
	
	public ResReply(Reply obj) {
		// TODO Auto-generated constructor stub
		if(obj != null) {
			this.id = obj.id;
			this.user = new ResUser(obj.user);
			this.content_id = obj.content_id;
			this.contents = obj.contents;
			this.createDate = getDate(obj.createDate);
			this.status = obj.status;
			this.likes = new ArrayList<ReplyLike>();
		}
	}
	
	private String getDate(Date createDate) {
		String result ="";
		
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
		Date currentTime = new Date();
		
		long diff = currentTime.getTime() - createDate.getTime();
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		long diffHour = diff / (60 * 60 * 1000);
		long diffMinute = diff / (60 * 1000);
		long diffSecond = diff / (1000);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(createDate);
		
		if(diffDays > 1) {
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(currentTime);
			if(cal.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
				result += String.valueOf(cal.get(Calendar.YEAR)) + "년 ";
			 
			result += String.valueOf(cal.get(Calendar.MONTH) + 1) + "월 ";
			result += String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "일";
		} else if(diffDays == 1) {
			result += "어제 ";
			
			if(cal.get(Calendar.AM_PM) == cal.get(Calendar.AM)) result += "오전 ";
			else result += "오후 ";
			
			result += String.valueOf(cal.get(Calendar.HOUR)) + "시 ";
			result += String.valueOf(cal.get(Calendar.MINUTE)) + "분";			
		} else {
			if(diffHour > 0) {
				result += String.valueOf(diffHour) + "시간 전";
			} else {
				if(diffMinute > 0) {
					result += String.valueOf(diffMinute) + "분 전";
				} else {
					if(diffSecond > 5) {
						result += String.valueOf(diffSecond) + "초 전";
					} else {
						result = "방금 전";
					}
				}
			}
		}		
		
		return result;
	}
}
