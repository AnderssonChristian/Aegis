package newtest;

import java.sql.*;

public class DbQueries {
	
	Database db;
	
	public DbQueries() {
		db = new Database();
	}
	
	public double getSiteScore(String url) {
		ResultSet result;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT score FROM websites WHERE url = ?");
			stmt.setString(1, url);
			result = stmt.executeQuery();
			result.next();
			return result.getDouble(1);
		} catch (SQLException e) {
			return -1.0;
		}
	}
	
	public double getUserTrustworthiness(int userId) {
		ResultSet result;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT trust FROM users WHERE user_id = ?");
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			result.next();
			return result.getDouble(1);
		} catch (SQLException e) {
			return -1.0;
		}
	}
	
	public int getUserVoteCount(int userId) {
		ResultSet result;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT COUNT(*) FROM votes WHERE user_id = ?");
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public double[] getSiteDivs(String url) {
		ResultSet result;
		double arr[] = new double[2];
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT score_divident, score_divisor FROM websites WHERE url = ?");
			stmt.setString(1, url);
			result = stmt.executeQuery();
			result.next();
			arr[0] = result.getDouble(1);
			arr[1] = result.getDouble(2);
			return arr;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public int updateSiteScore(String url, double score, double divident, double divisor) {
		int result = 0;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("UPDATE websites SET score = ?, score_divident = ?, score_divisor = ? WHERE url = ?");
			stmt.setDouble(1, score);
			stmt.setDouble(2, divident);
			stmt.setDouble(3, divisor);
			stmt.setString(4, url);
			result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public int updateUserTrust(int userId, double trust) {
		int result = 0;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("UPDATE users SET trust = ? WHERE user_id = ?");
			stmt.setDouble(1, trust);
			stmt.setInt(2, userId);
			result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			return -1;
		}	
	}
	
	public int insertUserRating(int userId, String url, int rating) {
		int result = 0;
		if (rating < 0 || rating > 100) return -1;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("INSERT INTO votes (user_id, web_id, score) SELECT ?, web_id, ? FROM websites WHERE url = ?");
			stmt.setInt(1, userId);			
			stmt.setDouble(2, rating);
			stmt.setString(3, url);
			result = stmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			return -1;
		}
	}

}
