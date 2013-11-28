package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResContent;
import resModles.ResReply;

public class ContentResult {
	public int code;
	public String msg;
	public List<ResContent> body;
	public List<ResReply> replies;
	
	public ContentResult() {
		body = new ArrayList<ResContent>();
		replies = new ArrayList<ResReply>();
	}
}
