package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResContent;

public class ContentResult {
	public int code;
	public String msg;
	public List<ResContent> body;
	
	public ContentResult() {
		body = new ArrayList<ResContent>();
	}
}
