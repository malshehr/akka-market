package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
		String trader = "'" + msg.getTrader() + "'";
		String company = "'" + msg.getCompany() + "'";
		float price = msg.getPrice();
		float wallet = msg.getWallet();
		try {
			Statement purchaseSql = conn.createStatement();
			purchaseSql.executeUpdate("INSERT INTO purchases VALUES (" + trader + ", " + company + ", " +  price + ", " + wallet + ");");
			purchaseSql.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void sell(SellMsg msg) {

	}

}
