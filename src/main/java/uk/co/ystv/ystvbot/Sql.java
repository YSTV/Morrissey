package uk.co.ystv.ystvbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sql {

	private static String url = "jdbc:postgresql://ystv.co.uk/" + Main.logins.get("sql").get("db") + "?user=" + Main.logins.get("sql").get("user") + "&password=" + Main.logins.get("sql").get("pass") + "&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
	private static Sql sql;

	public static Sql getInstance() {
		if (Sql.sql == null) {
			Sql.sql = new Sql();
		}
		return Sql.sql;
	}

	private Connection conn;

	public Sql() {
		try {
			reconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reconnect() throws Exception {
		Class.forName("org.postgresql.Driver").newInstance();
		this.conn = DriverManager.getConnection(Sql.url);
	}

	private boolean checkConnection() throws SQLException {
		return conn != null && !conn.isClosed();
	}

	private void checkAndFix() throws Exception {
		if (!checkConnection()) {
			reconnect();
		}
	}

	public int update(String sql) throws Exception {
		checkAndFix();
		try {
			final PreparedStatement pr = this.conn.prepareStatement(sql);
			int count = pr.executeUpdate();
			return count;
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int insert(String sql, Object[] values) throws Exception {
		checkAndFix();
		try {
			final PreparedStatement pr = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (values != null) {
				for (int i = 1; i <= values.length; i++) {
					this.setValue(pr, i, values[i - 1]);
				}
			}
			pr.executeUpdate();
			final ResultSet rs = pr.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public ResultSet query(String sql) throws Exception {
		return this.query(sql, null);
	}

	public ResultSet query(String sql, Object[] values) throws Exception {
		checkAndFix();
		try {
			final PreparedStatement pr = this.conn.prepareStatement(sql);
			if (values != null) {
				for (int i = 1; i <= values.length; i++) {
					this.setValue(pr, i, values[i - 1]);
				}
			}
			ResultSet results = pr.executeQuery();
			return results;
		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setValue(PreparedStatement pr, int index, Object value) throws SQLException {
		if (value instanceof String) {
			pr.setString(index, (String) value);
		} else if (value instanceof Boolean) {
			pr.setBoolean(index, (Boolean) value);
		}
	}

}
