

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDAO {
	Connection conn = null;
	PreparedStatement pstmt;
	
	final String J = "org.h2.Driver";
	final String JU = "jdbc:h2:tcp://localhost/~/jwbookdb";
	
	private void open() {
		try {
			Class.forName(J);
			conn = DriverManager.getConnection(JU,"jwbook","1234");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void close() {
		try {
			conn.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 회원가입(create)
	public int signup(Users u) {
		open();
		int result = 0;
		String sql = "insert into users(id,password,name,ts) values (?,?,?,current_timestamp(0))";
		try {
			pstmt = conn.prepareStatement(sql);
			// users는 aid가 primary key
			pstmt.setString(1, u.getId());
			pstmt.setString(2, u.getPassword());
			pstmt.setString(3, u.getName());
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return result;
	}
	// 로그인 부분(read)
	public Users login(String id,String password) {
		open();
		String sql = "select * from users where id= ? and password = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			pstmt.setString(2, password);
			try(ResultSet rs = pstmt.executeQuery()){
				// 단일 호출이기 때문에 if문으로 탐색
				if(rs.next()) {
					Users u = new Users();
					u.setId(rs.getString("id"));
					u.setName(rs.getString("name"));
					return u;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return null;
	}
	
	
}
