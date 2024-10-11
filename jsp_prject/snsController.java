

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Servlet implementation class snsController
 */

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Servlet implementation class snsController
 */
@MultipartConfig
public class snsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	UsersDAO udao;
	FeedsDAO fdao;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		udao = new UsersDAO();
		fdao = new FeedsDAO();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String view = "";
		if(action == null) {
			// 기본값을 보여준다.
			List<Feeds> list;
			list = fdao.getAll();
			request.setAttribute("feedlist", list);
			getServletContext().getRequestDispatcher("/feedlist.jsp").forward(request, response);
			// view = getAll(request,response); 도 됨
			
		}else {
			switch (action) {
			case "login":
				view = login(request, response);
				break;
			case "signup":
				view = signup(request, response);
				break;
			case "logout":
				view = logout(request,response);
				break;
			case "write":
				view = write(request,response);
				break;
			case "getAll":
				view = getAll(request,response);
				break;
			case "delete":
                view = delete(request, response);
                break;
			case "update":
                view = update(request, response);
                break;
			 case "edit":
                 view = edit(request, response);
                 break;
			}
			getServletContext().getRequestDispatcher("/" + view).forward(request, response);
		}
	}
	/////////////////// 유저 dao 처리////////////////////////////////////////
	
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		return "logout.jsp";
	}
	public String login(HttpServletRequest request, HttpServletResponse response) {
		Users s = new Users();
		String name = request.getParameter("id");
		String pwd = request.getParameter("password");
		try {
			s = udao.login(name, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (s != null) {
			List<Feeds> list;
			list = fdao.getAll();
			request.setAttribute("feedlist", list);
			return "feedlist.jsp"; // 로그인 성공하면 바로 글 목록 보이게하기
		} else {
			request.setAttribute("error","로그인 실패");
			
			return "login.jsp"; // 실패하면 다시 로그인 화면 유지
			
		}


	}
	// 회원가입
	public String signup(HttpServletRequest request, HttpServletResponse response) {
//		String id = request.getParameter("id");
//	    String password = request.getParameter("password");
//	    
//	    // 정규표현식 검증하는 부분 
//	    String idRegex = "^[a-zA-Z]+$";
//	    String passwordRegex = "^[0-9]+$";
//	    
//	    if (!id.matches(idRegex)) {
//	        request.setAttribute("error", "아이디는 문자만 입력 가능합니다.");
//	        return "signup.jsp";
//	    }
//	    
//	    if (!password.matches(passwordRegex)) {
//	        request.setAttribute("error", "패스워드는 숫자만 입력 가능합니다.");
//	        return "signup.jsp";
//	    }	
		
		Users s = new Users();
		try {
			BeanUtils.populate(s, request.getParameterMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(udao.signup(s) != 0) {
//			HttpSession session = request.getSession();
//			session.setAttribute("user", s);
//			// 회원가입하면 세션에 저장하기
			
			List<Feeds> list;
			list = fdao.getAll();
			request.setAttribute("feedlist", list);
			return "feedlist.jsp"; // 성공하면 글목록 호출
		} else	{
			request.setAttribute("error", "회원가입 실패");
			return "login.jsp"; // 실패하면 로그인으로 돌아가기
		}
	}

	//////////////////////  FeedsDAO 처리 /////////////////////////////
	public String getAll(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Feeds> newsList = fdao.getAll();
            request.setAttribute("feedlist", newsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "feedlist.jsp"; // 모두 불러오기니까 글목록jsp파일 호출
    }
	public String write(HttpServletRequest request, HttpServletResponse response) {
		Feeds s = new Feeds();
		// 세션에 저장되어있는 아이디 가져옴
		String id = (String) request.getSession().getAttribute("login_id");
		
		try {
			Part filePart = request.getPart("img");
			String fileName = getFilename(filePart);
			if (filePart != null && !fileName.isEmpty()) {
				String uploadPath = getServletContext().getRealPath("/")+"img"+ File.separator + fileName;
				File uploadDir = new File(getServletContext().getRealPath("/")+"img");
				if(!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				filePart.write(uploadPath);
			}
			BeanUtils.populate(s, request.getParameterMap());
			s.setImg("img/"+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(fdao.write(s,id) != 0) {
			List<Feeds> list;
			list = fdao.getAll();
			request.setAttribute("feedlist", list);
			return	"feedlist.jsp";
		} else{	
			request.setAttribute("error", "글쓰기 실패");
			return "feedlist.jsp";
		}
	
		
	}
	private String getFilename(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String [] tokens = contentDisposition.split(";");
		for(String token : tokens) {
			if(token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=")+2,token.length()-1);
			}
		}
		return null;
	}
	//삭제 함수
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		// 해당 게시물의 fid를 가져옴
		int feedId = Integer.parseInt(request.getParameter("fid"));
        // 삭제한다.
		int result = fdao.delete(feedId);
		// 삭제 됐다면 feedlist 호출
        if (result > 0) {
            List<Feeds> list;
            list = fdao.getAll();
            request.setAttribute("feedlist", list);
            return "feedlist.jsp";
        } else {
            request.setAttribute("error", "삭제 실패");
            return "feedlist.jsp";
        }
    }
	// 수정함수
	public String update(HttpServletRequest request, HttpServletResponse response) {
        Feeds feed = new Feeds();
        // 수정할 사항 담을 객체 생성
        try {
            Part filePart = request.getPart("img");
            String fileName = getFilename(filePart);
            if (filePart != null && !fileName.isEmpty()) {
                String uploadPath = getServletContext().getRealPath("/") + "img" + File.separator + fileName;
                File uploadDir = new File(getServletContext().getRealPath("/") + "img");
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                filePart.write(uploadPath);
                feed.setImg("img/" + fileName);
            }
            BeanUtils.populate(feed, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 위 코드는 수정할 이미지와 새로운 내용 담는다.
        //아래는 쿼리 실행
        int result = fdao.update(feed);
        if (result > 0) {
            List<Feeds> list = fdao.getAll();
            request.setAttribute("feedlist", list);
            return "feedlist.jsp";
        } else {
            request.setAttribute("error", "수정 실패");
            return "editFeed.jsp";
        }
    }
	
	//수정을 하기 위해서는 해당 게시물을 찾아야한다. 
	public String edit(HttpServletRequest request, HttpServletResponse response) {
        int feedId = Integer.parseInt(request.getParameter("fid"));
        Feeds feed = fdao.getFeedById(feedId);
        // request.setAttribute("feed" <- 이거는 jsp파일에서 사용하는 이름과 동일해야됨, feed);
        request.setAttribute("feed", feed);
        return "editFeed.jsp";
    }

}
