package messages;

public class StartAuditMsg {

	
	private String userName;
	private String password;
	private String dbName;
	
	public StartAuditMsg(String user, String pass, String db) {
		userName = user;
		password = pass;
		dbName = db;
	}
	
	public String getUsername() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getDatabase() {
		return dbName;
	}
}
