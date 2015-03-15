package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResNotice;

public class NoticeResult {

	public int code;
	public String msg;
	public List<ResNotice> body;
	
	public NoticeResult() {
		body = new ArrayList<ResNotice>();
	}
}
