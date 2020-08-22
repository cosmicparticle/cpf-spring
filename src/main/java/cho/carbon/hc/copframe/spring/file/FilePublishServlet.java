package cho.carbon.hc.copframe.spring.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StreamUtils;

public class FilePublishServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		FilePublisher publisher = FilePublisher.getContextInstance();
		String key = req.getRequestURI()
				.substring(req.getContextPath().length() + publisher.fileUtils.getFolderUri().length());
		String code = key;
		if (key.contains("/")) {
			code = key.substring(0, key.indexOf("/"));
		}
		if (publisher.containsCode(code)) {
			String fileName = publisher.getFileName(code);
			
			resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			
			String suffix=publisher.getFileSuffix(code);
			
			String contentType=contentTypeMap.get(suffix.toLowerCase());
			if(contentType==null) {
				resp.setContentType("application/octet-stream");
			}else {
				resp.setContentType(contentType);
				resp.setHeader("Content-Type", contentType);
				resp.setHeader("content-disposition", "attachment");
			}
			
			FileInputStream input = publisher.fileUtils.getInputStream(code);
			StreamUtils.copy(input, resp.getOutputStream());
			
		}
	}

	private static Map<String, String> contentTypeMap = null;

	static {
		contentTypeMap = new HashMap<>();
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("zip", "application/zip");
		contentTypeMap.put("gzip", "application/gzip");
		contentTypeMap.put("jar", "application/gzip");
		contentTypeMap.put("rar", "application/gzip");
		contentTypeMap.put("xls", "application/x-xls");
		contentTypeMap.put("xlsx", "application/x-xls");
		contentTypeMap.put("doc", "application/msword");
		contentTypeMap.put("dot", "application/msword");
		contentTypeMap.put("exe", "application/x-msdownload");
		contentTypeMap.put("dll", "application/x-msdownload");
		contentTypeMap.put("jpg", "application/x-jpg");
		contentTypeMap.put("ppt", "application/x-ppt");
		
		contentTypeMap.put("png", "image/png");
		contentTypeMap.put("gif", "image/gif");
		contentTypeMap.put("ico", "image/x-ico");
		
		contentTypeMap.put("mp3", "audio/mp3");
		
		contentTypeMap.put("asf", "video/x-ms-asf");
		contentTypeMap.put("avi", "video/avi");
		contentTypeMap.put("mp4", "video/mpeg4");
		contentTypeMap.put("wmv", "video/x-ms-wmv");
		contentTypeMap.put("mpv", "video/mpg");
		
	}
}
