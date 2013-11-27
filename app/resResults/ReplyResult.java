package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResReply;

public class ReplyResult {
	public int code;
	public String msg;
	public List<ResReply> body;
	
	public ReplyResult() {
		body = new ArrayList<ResReply>();
	}

}
