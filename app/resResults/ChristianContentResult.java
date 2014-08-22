package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResContent;
import resModles.ResContentLike;
import resModles.ResReply;

public class ChristianContentResult {
	public int code;
	public String msg;
	public List<ResContent> body;
	public List<ResReply> replies;
	public List<ResContentLike> likes; 
	
	public ChristianContentResult() {
		body = new ArrayList<ResContent>();
		replies = new ArrayList<ResReply>();
		likes = new ArrayList<ResContentLike>();
	}
}
