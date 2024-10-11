


import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
Connection conn = null;// 데이터베이스 연결을 위한 Connection 객체 , 초기에는 null로 설정하여 연결이 아직 이루어지지 않았음을 나타냅니다.
PreparedStatement pstmt;// SQL 쿼리를 실행하기 위한 PreparedStatement 객체를 선언



final String JDBC_DRIVER = "org.h2.Driver";						// 기본 데이터 베이스 연결을 위한 준비
final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbookdb";

public void open() {
	try {
		Class.forName(JDBC_DRIVER);			// JDBC 드라이버 클래스를 로드합니다. JDBC_DRIVER는 드라이버 클래스의 이름을 나타내는 문자열
		conn = DriverManager.getConnection(JDBC_URL, "jwbook", "1234");		//데이터베이스에 연결을 설정
	} catch (Exception e) {					// 예외처리
		e.printStackTrace();
		}
}


public void close() {
	try {
			pstmt.close();					// 객체를 닫습니다
			conn.close();					// 객체를 닫습니다
	}catch (SQLException e) {				// 예외 처리
	e.printStackTrace();
	
}
}
public int addNews(News n) {
	open();			// 데이터 베이스 열기
	int result = 0; //변수 초기화
    String sql = "INSERT INTO news (title, img, date, content) VALUES (?, ?, CURRENT_TIMESTAMP(0), ?)";		// SQL 쿼리 작성
    try {
        pstmt = conn.prepareStatement(sql);			// PreparedStatement 생성 및 값 설정:
        pstmt.setString(1, n.getTitle());
        pstmt.setBlob(2, n.getImg());
        pstmt.setString(3, n.getContent());
        result = pstmt.executeUpdate();				// 쿼리 실행 및 결과 저장
    } catch (SQLException e) {						// 예외 처리
        e.printStackTrace();
    } finally {										// 리소스 해제
        close();
    }
    return result;									//값 리턴
}

public List<News> getAll() throws SQLException{
	open();											// 데이터 베이스 열기
	List<News> newsList = new ArrayList<> ();		// 뉴스 기사 목록을 저장할 리스트 생성
	String sql = "SELECT * FROM news"; 		// SQL쿼리 생성
	try {
		pstmt = conn.prepareStatement(sql);													// PreparedStatement 생성 및 쿼리 실행
		try (ResultSet rs = pstmt.executeQuery()){
			while(rs.next()) {																//  반복문을 사용하여 ResultSet의 모든 행을 순회합니다.
	
				News n = new News();
				n.setId(rs.getInt("id"));		n.setTitle(rs.getString("title"));			//  행에 대해, 새로운 News 객체 n을 생성하고, ResultSet에서 가져온 데이터(id, title, img, cdate)로 n을 초기화
				n.setImg(rs.getBlob("img"));	n.setDate(rs.getTimestamp("date").toString());
				newsList.add(n);															// 각 News 객체 n을 newsList에 추가합니다
			}
			
			
		}
		
		
	}
	catch(Exception e) {e.printStackTrace();}											//예외처리
	
	finally {close();}
	
return newsList;
}

public News getNews(int id) {
	open();
	News n = null;										// 뉴스 기사 객체 초기화
    ResultSet rs = null;														
    String sql = "SELECT id, title, img, PARSEDATETIME(date, 'yyyy-MM-dd HH:mm:ss') as cdate, content FROM news WHERE id = ?";
    try {
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			n = new News();
			n.setId(rs.getInt("id"));
            n.setTitle(rs.getString("title"));
            n.setImg(rs.getBlob("img"));
            n.setDate(rs.getString("cdate"));
            n.setContent(rs.getString("content"));
		} else {
            throw new SQLException("No news article found with id: " + id);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        close();
    }
    return n;
}

public byte[] getImageById(int id) {									// 이 코드의 기능은 주어진 id를 사용하여 데이터베이스에서 이미지를 가져오는 것
	open();																//이 코드는 주어진 id를 사용하여 데이터베이스에서 이미지를 안전하게 가져오고, 예외 상황을 처리하며, 자원을 적절히 정리
	byte[] imageBytes = null;											// 변수 초기화
	ResultSet rs = null;
	String sql = "SELECT img FROM news WHERE id = ?";					//SQL 쿼리 준비
	try {
		pstmt = conn.prepareStatement(sql);								// 변수 설정
		pstmt.setInt(1, id);
        rs = pstmt.executeQuery();													
        if (rs.next()) {												//구문을 통해 결과 집합의 첫 번째 행으로 이동
            Blob blob = rs.getBlob("img");								// img 컬럼의 값을 Blob 객체로 가져옵니다.
            int blobLength = (int) blob.length();						//  Blob의 길이를 가져옵니다.
            imageBytes = blob.getBytes(1, blobLength);					// Blob 데이터를 바이트 배열로 변환하여 imageBytes에 저장
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        close();
    }
    return imageBytes;
}

public void delNews(int id) {
	open();
	String sql = "DELETE FROM news WHERE id = ?";			// 쿼리 준비
	try {
		pstmt= conn.prepareStatement(sql);					// 변수값 할당
		pstmt.setInt(1, id);
		if(pstmt.executeUpdate()==0) {
			throw new RuntimeException("error");			// 뉴스 기사가 없을시 에러 발생
		}
	}
		catch(SQLException e) {e.printStackTrace();
		}
		finally {close();}
	}






}