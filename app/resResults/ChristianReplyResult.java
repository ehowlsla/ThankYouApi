package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResReply;

public class ChristianReplyResult {
	public int code;
	public String msg;
	public List<ResReply> body;
	
	public ChristianReplyResult() {
		body = new ArrayList<ResReply>();
	}

}
