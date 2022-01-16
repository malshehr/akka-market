package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import akka.actor.AbstractActor;
import akka.actor.Props;
import messages.PurchaseMsg;
import messages.SellMsg;
import messages.StartAuditMsg;

public class Audit extends AbstractActor {
	private String dbms = "mysql";
	private String serverName = "localhost";
	private String portNumber = "3306";
	private String dbName;
	private String userName;
	private String password;
	private Connection conn;

	public static Props props() {
		return Props.create(Audit.class);
	}

	public Receive createReceive() {
		return receiveBuilder().match(StartAuditMsg.class, this::startAudit).match(PurchaseMsg.class, this::purchase)
				.match(SellMsg.class, this::sell).build();
	}

	private void startAudit(StartAuditMsg msg) throws SQLException {
		dbName = msg.getDatabase();
		userName = msg.getUsername();
		password = msg.getPassword();

		conn = getConnection();
	}

	private Connection getConnection() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection("jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/" + dbName,
				connectionProps);
		System.out.println("Connected to database");
		return conn;
	}

	private void purchase(PurchaseMsg msg) {
		String timeStamp = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "'";
		String trader = "'" + msg.getTrader() + "'";
		String company = "'" + msg.getCompany() + "'";
		String style = "'" + msg.getTradingStyle().name() + "'";
		float price = msg.getPrice();
		
		try {
			Statement purchaseSql = conn.createStatement();
			purchaseSql.executeUpdate("INSERT INTO purchases VALUES (" + trader + ", " + company + ", " +  price + ", " + timeStamp + ", " +
			style + ");");
			purchaseSql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void sell(SellMsg msg) {
		String timeStamp = "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "'";
		String trader = "'" + msg.getTrader() + "'";
		String company = "'" + msg.getCompany() + "'";
		String style = "'" + msg.getTradingStyle().name() + "'";
		float price = msg.getPrice();
		float earnings = msg.getEarnings();
		
		try {
			Statement sellSql = conn.createStatement();
			sellSql.executeUpdate("INSERT INTO sells VALUES (" + trader + ", " + company + ", " +  price + ", " + earnings + ", " + timeStamp + ", " +
			style + ");");
			sellSql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
