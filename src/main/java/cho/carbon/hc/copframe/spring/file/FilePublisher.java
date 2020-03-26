package cho.carbon.hc.copframe.spring.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cho.carbon.hc.copframe.spring.CPFSpringContextLoader;

/**
 * 文件发布器
 * @author Copperfield
 * @date 2018年7月25日 下午3:32:13
 */
public class FilePublisher{
	
	private static FilePublisher instance; 
	static Logger logger = Logger.getLogger(FilePublisher.class);
	
	
	/**
	 * 如果在容器内有注入对象，那么就返回这个对象，否则就调用{@link #getInstance()}方法
	 * @return
	 */
	public static FilePublisher getContextInstance() {
		if(CPFSpringContextLoader.getContext() != null) {
			FilePublisher publisher = CPFSpringContextLoader.getContext().getBean(FilePublisher.class);
			if(publisher != null) {
				return publisher;
			}
		}
		return getInstance();
		
	}
	
	public static FilePublisher getInstance() {
		synchronized (FilePublisher.class) {
			if(instance == null) {
				instance = new FilePublisher();
				FileUtils fileUtils = new FileUtils(".", "/download-files/");
				instance.setFileUtils(fileUtils);
			}
		}
		return instance;
	}
	
	FileUtils fileUtils;
	
	Map<String, String> fileNameMap = new HashMap<>();
	
	public void setFileUtils(FileUtils fileUtils) {
		this.fileUtils = fileUtils;
	}
	
	
	public String publish(FileHaunt file) {
		if(file.getCode() != null) {
			if(fileNameMap.containsKey(file.getCode())) {
				File f = this.fileUtils.getFile(file.getCode());
				if(f != null && f.exists()) {
					return getURL(file);
				}
			}
			try {
				this.fileUtils.saveFile(file.getCode(), file.getInputStream());
				fileNameMap.put(file.getCode(), file.getFileName());
				return getURL(file);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return null;
	}
	
	public boolean containsCode(String fileCode) {
		return fileNameMap.containsKey(fileCode);
	}

	private String getURL(FileHaunt file) {
		if(file != null) {
			return "." + this.fileUtils.getFolderUri() + file.getCode() + "/" + file.getFileName();
		}
		return null;
	}


}
