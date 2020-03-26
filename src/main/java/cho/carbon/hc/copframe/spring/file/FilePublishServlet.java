package cho.carbon.hc.copframe.spring.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StreamUtils;

public class FilePublishServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FilePublisher publisher = FilePublisher.getContextInstance();
		String key = req.getRequestURI().substring(req.getContextPath().length() + publisher.fileUtils.getFolderUri().length());
		String code = key;
		if(key.contains("/")) {
			code = key.substring(0, key.indexOf("/"));
		}
		if(publisher.fileNameMap.containsKey(code)) {
			String fileName = publisher.fileNameMap.get(code);
			FileInputStream input = publisher.fileUtils.getInputStream(code);
			StreamUtils.copy(input, resp.getOutputStream());
			resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			resp.setContentType("application/octet-stream");
		}
	}
}
