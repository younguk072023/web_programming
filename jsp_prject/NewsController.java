

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;


@MultipartConfig						//이 애노테이션은 서블릿이 멀티파트 요청을 처리할 수 있도록 합니다. 예를 들어, 파일 업로드 기능을 지원 	// 겁나 중요 빼먹지 않기
public class NewsController extends HttpServlet {
	private static final long serialVersionUID = 1L; 	//클래스의 직렬화 버전 지정
       
 
	private NewsDAO dao;							// 데이터베이스 접근 객체 선언
	
	public void init(ServletConfig config) throws ServletException{	// 서블릿 초기화
		super.init(config);											// 부모 클래스인 HttpServlet의 초기화 메서드를 호출
		dao = new NewsDAO();										// NewsDAO 객체를 생성합니다. 이를 통해 데이터베이스와 상호작용할 준비
		
}
	
	protected void service(HttpServletRequest request, HttpServletResponse response)	//HttpServlet의 service 메서드를 오버라이드하여 HTTP 요청을 처리
			throws ServletException, IOException {
		String action = request.getParameter("action");								// action 파라미터를 읽어오고, 뷰 이름을 저장할 변수를 선언합니다.
		String view = "";
		
		if(action == null) {				//  이미지 데이터를 응답으로 전송
			byte[] imageBytes = dao.getImageById(Integer.parseInt(request.getParameter("uid")));	// uid 파라미터로 전달된 ID를 사용하여 이미지를 가져옵니다.
			response.setContentType("image/jpeg");													// 응답의 컨텐츠 타입을 이미지로 설정
			OutputStream outputStream = response.getOutputStream();									// 응답 출력 스트림을 얻어옵니다.
			outputStream.write(imageBytes);															// 이미지 데이터를 출력 스트림에 씁니다.
			outputStream.close();																	// 출력 스트림을 닫습니다
			
		}
		else {
			switch ( action) {																		// action 값에 따라 적절한 메서드를 호출하고, 뷰 이름을 설정합니다
			case "addNews":
				view = addNews(request,response);
				break;
			case "getAll":
				view = getAll(request,response);
				break;
			case "getNews":
				view = getNews(request,response);
				break;
			case "delNews":
				view = delNews(request,response);
				break;
			}
			
			getServletContext().getRequestDispatcher("/" + view).forward(request, response);			// 지정된 뷰로 요청을 포워드합니다.
			
		}
		
		
	}
	
	
	
	public String addNews(HttpServletRequest request, HttpServletResponse response) {		
	    News n = new News();  // News 객체를 생성합니다
	    
	    try {
	        BeanUtils.populate(n, request.getParameterMap());  // 요청 파라미터를 News 객체에 매핑
	        Part filePart = request.getPart("imageFile");  // 이미지 파일을 요청에서 가져옵니다.
	        
	        if (filePart != null) {  // 파일을 읽고 바이트 배열로 변환한 후 News 객체에 설정
	            InputStream inputStream = filePart.getInputStream();						// 파일이 존재하면, 이를 읽어 바이트 배열로 변환하고, SerialBlob 객체로 변환하여 News 객체에 설정
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            byte[] buffer = new byte[4096];
	            int bytesRead = -1;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	            
	            byte[] imageData = outputStream.toByteArray();
	            
	            Blob img = new SerialBlob(imageData);
	            n.setImg(img);
	            
	            outputStream.close();
	            inputStream.close();
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }										//뉴스 항목 추가 및 결과 처리
	    if (dao.addNews(n) != 0) {  // 뉴스 항목을 데이터베이스에 추가하고, 결과에 따라 적절한 뷰 이름을 반환
	        return "NewsController.nhn?action=getAll";  // 공백 제거	절대 공백을 넣지마
	    } else {
	        request.setAttribute("error", "뉴스가 정상적으로 등록되지 않았습니다.");  // 마침표 추가
	        return "newsList.jsp";
	    }
	}

	
public String getAll(HttpServletRequest request, HttpServletResponse response) {
	List<News> list;				// List<News> 타입의 변수 list를 선언하여 데이터베이스로부터 가져온 뉴스 목록을 저장할 준비
	
	try {
		list = dao.getAll();		// dao.getAll() 메서드를 호출하여 데이터베이스에서 모든 뉴스 목록을 검색하고, 그 결과를 list에 저장
		request.setAttribute("newslist", list);	//가져온 뉴스 목록 list를 요청 객체의 속성으로 설정 , 이를 통해 JSP 또는 다른 뷰 페이지에서 뉴스 목록을 접근하고 렌더링
		
	}
	catch(Exception e) {
		e.printStackTrace();
		request.setAttribute("error", "뉴스 목록이 정상적으로 처리되지 않았습니다.");
	
	}
	return "newsList.jsp";
}
	
public String getNews(HttpServletRequest request, HttpServletResponse response) {
	int id =Integer.parseInt(request.getParameter("id"));				// 뉴스 ID 추출
	try {																//데이터베이스에서 뉴스 항목 검색
		News n = dao.getNews(id);										//메서드를 호출하여, 위에서 추출한 ID에 해당하는 뉴스 항목을 데이터베이스에서 검색 , 검색 결과는 News 타입의 객체 n에 저장
		request.setAttribute("news",n);								//요청 객체에 뉴스 항목 설정
		
	}
	catch(Exception e) {e.printStackTrace();
		request.setAttribute("error","뉴스를 정상적으로 가져오지 못했습니다.");
		return getAll(request, response);
	}
	return "newsView.jsp";
}




public String delNews(HttpServletRequest request, HttpServletResponse response) {
	int id = Integer.parseInt(request.getParameter("id"));					// 뉴스 id 추출
	
	try {
		dao.delNews(id);													// 메서드를 호출하여 데이터베이스에서 해당 ID를 가진 뉴스 항목을 삭제
	}
	catch(Exception e) {
		
		e.printStackTrace();
		request.setAttribute("error","뉴스를 정상적으로 삭제하지 못했습니다");
		 return getAll(request,response);
	}
	
	return "NewsController.nhn?action=getAll";								// 뷰 반환
}

}