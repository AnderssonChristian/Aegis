package newtest;

import java.sql.*;

public class DbQueries {
	
	Database db;
	
	public DbQueries() {
		db = new Database();
	}
	
	public double[] getSiteScore(String url) {
		ResultSet result;
		double arr[] = new double[4];
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT score, user_score, server_score, 3rd_score FROM websites WHERE url = ?");
			stmt.setString(1, url);
			result = stmt.executeQuery();
			result.next();
			arr[0] = result.getDouble(1);
			arr[1] = result.getDouble(2);
			arr[2] = result.getDouble(3);
			arr[3] = result.getDouble(4);
			return arr;
		} catch (SQLException e) {
			return null;
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
	
	public int updateSiteScore(String url, double score, double userscore, double serverscore, double thirdscore, double divident, double divisor) {
		int result = 0;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("UPDATE websites SET score = ?, user_score = ?, server_score = ?, 3rd_score = ?, score_divident = ?, score_divisor = ? WHERE url = ?");
			stmt.setDouble(1, score);
			stmt.setDouble(2, userscore);
			stmt.setDouble(3, serverscore);
			stmt.setDouble(4, thirdscore);
			stmt.setDouble(5, divident);
			stmt.setDouble(6, divisor);
			stmt.setString(7, url);
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
	
	public int getUserId(String pluginId) {
		ResultSet result;
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT user_id FROM users WHERE plugin_id = ?");
			stmt.setString(1, pluginId);
			result = stmt.executeQuery();
			result.next();
			return result.getInt(1);
		} catch (SQLException e) {
			return -1;
		}
	}
	
	public boolean createUser(String pluginId){
		Boolean result;
		try{
			PreparedStatement stmt = db.conn().prepareStatement("INSERT INTO users (plugin_id) VALUES (?)");
			stmt.setString(1, pluginId);
			result = stmt.execute();
			return !result;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}		
	}
	
	public String[] getUserRating(int userId, String url) {
		ResultSet result;
		String arr[] = new String[10];
		try {
			PreparedStatement stmt = db.conn().prepareStatement("SELECT score, comment, chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8 FROM votes WHERE user_id = ? AND web_id = (SELECT web_id FROM websites WHERE url = ?)");
			stmt.setInt(1, userId);
			stmt.setString(2, url);
			result = stmt.executeQuery();
			if(result.next()) {
				arr[0] = result.getString(1);
				arr[1] = result.getString(2);
				arr[2] = result.getString(3);
				arr[3] = result.getString(4);
				arr[4] = result.getString(5);
				arr[5] = result.getString(6);
				arr[6] = result.getString(7);
				arr[7] = result.getString(8);
				arr[8] = result.getString(9);
				arr[9] = result.getString(10);
			} else {
				return null;
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public int insertUserRating(int userId, String url, int rating, String comment, String[] categories) {
		ResultSet rs;
		int result;
		if (rating < 0 || rating > 100) return -1;
		try {
			PreparedStatement webstmt = db.conn().prepareStatement("SELECT web_id FROM websites WHERE url = ?");		
			webstmt.setString(1, url);
			rs = webstmt.executeQuery();
			if (!rs.next()){ //no such url in database, return 0
				return 0;
			}
			String webId = rs.getString(1);
			
			PreparedStatement stmt = db.conn().prepareStatement("SELECT vote_id FROM votes WHERE user_id = ? AND web_id = ?");
			stmt.setInt(1, userId);
			stmt.setString(2, webId);
			rs = stmt.executeQuery();
			if (!rs.next()) { //no vote from this user on this site -- insert new row
				
				PreparedStatement stmt2 = db.conn().prepareStatement("INSERT INTO votes (user_id, score, comment, chk1, chk2, chk3, chk4, chk5, chk6, chk7, chk8, web_id) SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, web_id FROM websites WHERE url = ?");
				stmt2.setInt(1, userId);			
				stmt2.setInt(2, rating);
				stmt2.setString(3, comment);
				for (int i=0; i<8; i++){
					stmt2.setInt(i+4, Integer.parseInt(categories[i]));
				}
				stmt2.setString(12, url);
				boolean res = stmt2.execute();
				if (res) return 1; else return 0;
				
			} else { //vote from this user on this site already exists -- update vote
				
				String voteId = rs.getString(1);
				PreparedStatement stmt4 = db.conn().prepareStatement("UPDATE votes SET score = ?, comment = ? , chk1 = ?, chk2 = ?, chk3 = ?, chk4 = ?, chk5 = ?, chk6 = ?, chk7 = ?, chk8 = ? WHERE vote_id = ?");
				stmt4.setDouble(1, rating);
				stmt4.setString(2, comment);
				for (int i=0; i<8; i++){
					stmt4.setInt(i+3, Integer.parseInt(categories[i]));
				}
				stmt4.setString(11, voteId);
				result = stmt4.executeUpdate();
				return result;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public double[] getSiteCatPercent(String url) {
		ResultSet rs;
		PreparedStatement stmt;
		int total = 0;
		double temp1=0, temp2=0;
		double res[] = new double[8];
		try {
			stmt = db.conn().prepareStatement("SELECT COUNT(*), web_id FROM votes WHERE web_id = (SELECT web_id FROM websites WHERE url = ?)");
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			if (!rs.next()){ //no such url in database, return 0
				return null;
			}
			total = rs.getInt(1);
			int webId = rs.getInt(2);
			
			stmt = db.conn().prepareStatement("SELECT (SELECT COUNT(chk1) FROM votes WHERE chk1 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk2) FROM votes WHERE chk2 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk3) FROM votes WHERE chk3 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk4) FROM votes WHERE chk4 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk5) FROM votes WHERE chk5 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk6) FROM votes WHERE chk6 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk7) FROM votes WHERE chk7 = 1 AND web_id = ?)," +
													"(SELECT COUNT(chk8) FROM votes WHERE chk8 = 1 AND web_id = ?)");
			stmt.setInt(1, webId);
			stmt.setInt(2, webId);
			stmt.setInt(3, webId);
			stmt.setInt(4, webId);
			stmt.setInt(5, webId);
			stmt.setInt(6, webId);
			stmt.setInt(7, webId);
			stmt.setInt(8, webId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				temp1 = rs.getDouble(1);
				temp2 = rs.getDouble(6);
				for (int i=0; i<8; i++) {
					res[i] = rs.getDouble(i+1)/total;
				}
			} else {
				return null;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String[][] getSiteComments(String url){
		PreparedStatement stmt;
		ResultSet rs;
		String[][] result = new String[10][2];
		try {
			stmt = db.conn().prepareStatement("SELECT `comment`, updated_on FROM votes JOIN users ON votes.user_id = users.user_id WHERE web_id = (SELECT web_id FROM websites WHERE url = ?) AND LENGTH(`comment`) > 5 ORDER BY trust DESC, updated_on DESC LIMIT 10");
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("MMM d yyyy");
			int i = 0;
			while(rs.next()) {
				result[i][0] = rs.getString(1);
				String temp = date.format(rs.getTimestamp(2));
				result[i][1] = temp.substring(0, 1).toUpperCase() + temp.substring(1);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean siteExistsInDB(String url) {
		PreparedStatement stmt;
		ResultSet rs;
		try {
			stmt = db.conn().prepareStatement("SELECT url FROM websites WHERE url = ?");
			stmt.setString(1, url);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addNewSite(String url) {
		PreparedStatement stmt;
		boolean result;
		try {
			stmt = db.conn().prepareStatement("INSERT INTO websites (url, user_score, server_score, 3rd_score, score_divident, score_divisor) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setString(1, url);
			stmt.setString(2, "50");
			stmt.setString(3, "50");
			stmt.setString(4, "50");
			stmt.setString(5, "5000");
			stmt.setString(6, "100");
			result = stmt.execute();
			if (result) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateSiteVisits(String url, int userId) {
		PreparedStatement stmt;
		boolean result;
		try {
			stmt = db.conn().prepareStatement("INSERT INTO visits (user_id, web_id) " +
					                              "SELECT w.user_id, w.web_id FROM " +
					                                  "(SELECT ? as user_id, web_id FROM websites WHERE url = ?) w WHERE NOT EXISTS " +
					                                       "(SELECT 1 FROM visits v WHERE " +
					                                       "v.web_id = w.web_id AND " +
					                                       "v.user_id = w.user_id AND " +
					                                       "v.added_on >= NOW() - INTERVAL 6 HOUR" +
					                                   ")");
			stmt.setInt(1, userId);
			stmt.setString(2, url);
			result = stmt.execute();
			if (result) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
