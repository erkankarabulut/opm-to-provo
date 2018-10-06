package Karma.query;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCquery {
	public static Properties getProperties() {
		Properties props = new Properties();
		/*
		InputStream is = null;
		try {
			is = JDBCquery.class.getResourceAsStream("C:\\maktas\\workspace\\LTSM\\jdbc_mysql.properties");
			props.load(is);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}	
		*/
		props.setProperty("driver","com.mysql.jdbc.Driver");
		props.setProperty("url","jdbc:mysql://10.1.32.229:3306/karma");
		props.setProperty("user","root");
		props.setProperty("password","123456");

		return props;
	}

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Properties props = getProperties();
		if (props != null) {
			String driver = props.getProperty("driver");
			String url = props.getProperty("url");
			String user = props.getProperty("user");
			String password = props.getProperty("password");

			try {
				Class.forName(driver);
                System.out.println("url = " + url +  " user = " + user + " password = " + password);
				conn = DriverManager.getConnection(url, user, password);
				stmt = conn.createStatement();

				// String sql = "show tables";
				// stmt.executeUpdate(sql);

//				String sql = "show tables";
				String sql = "select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-_-%';";
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					System.out.println(rs.getString(1));
					// System.out.print(rs.getInt(1) + "\n");
					// System.out.print(rs.getString(2) + "\t");
					// System.out.print(rs.getString(3) + "\n");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
