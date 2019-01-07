import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/ServletUpload")
@MultipartConfig(fileSizeThreshold=1024*1024*10,
					maxFileSize=1024*1024*50,
					maxRequestSize=1024*1024*100)
public class ServletUpload extends HttpServlet{
	private static final long serialVersionUID = 205242440643911308L;
	private static final String UPLOAD_DIR = "uploads";
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		
		File filesSaveDir = new File(uploadFilePath);
		if(!filesSaveDir.exists()) {
			filesSaveDir.mkdirs();
		}
		System.out.println("Upload FIle Directory=" + filesSaveDir.getAbsolutePath());
		
		String fileName = null;
		for(Part part : request.getParts()) {
			fileName = getFileName(part);
			File f = new File(fileName);
			part.write(uploadFilePath + File.separator + f.getName());
		}
		
		request.setAttribute("message", fileName + "FileUpload Successfully");
		
		getServletContext().getRequestDispatcher("/response.jsp").forward(request, response);
		
	}

	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("Header= " + contentDisp);
		
		String[] tokens = contentDisp.split(";");
		
		for(String token : tokens) {
			if(token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length()-1);
			}
		}
		return "";
	}

}
