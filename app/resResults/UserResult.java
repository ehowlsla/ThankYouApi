package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResNotice;
import resModles.ResUser;

public class UserResult {
	public int code;
	public String msg;
	public List<ResUser> body;
	
	public UserResult() {
		body = new ArrayList<ResUser>();
	} 
}
