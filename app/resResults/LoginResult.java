package resResults;

import java.util.ArrayList;
import java.util.List;

import resModles.ResNotice;
import resModles.ResUser;

public class LoginResult {
	public int code;
	public String msg;
	public List<ResUser> body;
	public List<ResNotice> notices;
	
	public LoginResult() {
		body = new ArrayList<ResUser>();
		notices = new ArrayList<ResNotice>();
	} 
}
