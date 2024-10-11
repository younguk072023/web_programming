

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedsDAO {
	Connection conn = null;
	PreparedStatement pstmt;
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbookdb";
	public void open() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, "jwbook", "1234");
		}catch (Exception e) { e.printStackTrace(); }
	}
	public void close() {
		try {
			if (pstmt != null) {
	            pstmt.close();
	        }
	        if (conn != null) {
	            conn.close();
	        }
		}catch (SQLException e) { e.printStackTrace(); }
	}
	
	//////////////////////////////////////////////////////////////위에는 복붙g하기//////
	// 글 생성(create)
	public int write(Feeds s,String id) {
		open();
		int result=0;
		String sql = "INSERT INTO feeds(id, img, content,ts) "
				+ "values (?,?,?,CURRENT_TIMESTAMP(0))";
		//삽입하는 쿼리 작성 (create라고 생각하자)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, s.getImg());
			pstmt.setString(3, s.getContent());
			// 어차피 aid는 자동으로 생성되므로 set안해도됨
			result = pstmt.executeUpdate();
		}catch (SQLException e) { e.printStackTrace(); }
		finally { 
			if (pstmt != null) {
	            try {
	                pstmt.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
			close(); }
		return result;
	}
	//삭제 함수(delete)
	public int delete(int feedId) {
        open();
        int result = 0;
        String sql = "DELETE FROM feeds WHERE fid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            //여기에서 feedId를 set하는 이유는 데베에서 해당 게시물을 얻기 위해서
            pstmt.setInt(1, feedId);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }
	//수정 함수
	public int update(Feeds f) {
		open();
		int result = 0;
		// 이미지와 내용을 수정하기 위한 쿼리문
		String sql = "update feeds set img = ?, content = ? where fid=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, f.getImg());
			pstmt.setString(2, f.getContent());
			pstmt.setInt(3, f.getFid());
			// 여기도 삭제와 마찬가지로 setfid를 해줘야됨
			result = pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return result;
	}
	// readALL 조회 함수
	public List<Feeds> getAll(){
		open();
		List<Feeds> feedlist = new ArrayList<>(); // 리스트 만들어줌
		String sql = "SELECT * FROM feeds";
		try {
			pstmt = conn.prepareStatement(sql);
			try(ResultSet rs = pstmt.executeQuery()){
				// 전부 조회할 경우에는 while문사용
				while(rs.next()) {
					Feeds f =new Feeds();
					f.setFid(rs.getInt("fid"));
					f.setId(rs.getString("id")); f.setImg(rs.getString("img"));
					f.setContent(rs.getString("content")); f.setTs(rs.getTimestamp("ts").toString());
					
					feedlist.add(f); // 리스트에 추가
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return feedlist;
	}
	// 단일 조회 함수 by fid
	public Feeds getFeedById(int fid) {
        open();
        Feeds feed = null;
        String sql = "SELECT * FROM feeds WHERE fid = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fid);
            // 해당하는 인덱스만 있으면 됨
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                feed = new Feeds();
                feed.setFid(rs.getInt("fid"));
                feed.setId(rs.getString("id"));
                feed.setImg(rs.getString("img"));
                feed.setContent(rs.getString("content"));
                feed.setTs(rs.getTimestamp("ts").toString());
                // 모든 정보 가져온다.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return feed;
    }
	//아이디로 검색함 img를 blob형태로 업로드하기위한것
		public byte[] getImageById(String id) {
			open();
			byte[] imageBytes = null;
			ResultSet rs = null;
			String sql = "SELECT img from feeds where id=?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					//데이터베이스의 형태로 이름 바꿔줌
					Blob imageBlob = rs.getBlob("photo"); 
					imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
					imageBlob.free(); 
				} 
			}catch (SQLException e) { e.printStackTrace(); }
			finally { close(); }
			return imageBytes;
		}
}
