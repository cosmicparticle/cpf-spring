package cho.carbon.hc.copframe.spring.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cho.carbon.hc.copframe.spring.CPFSpringContextLoader;

/**
 * 文件发布器
 * @author Copperfield
 * @date 2018年7月25日 下午3:32:13
 */
public class FilePublisher{
	
	private static FilePublisher instance; 
	static Logger logger = LoggerFactory.getLogger(FilePublisher.class);
	
	
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
	
	private Map<String, String> fileNameMap = new HashMap<>();
	private Map<String, String> fileSuffixMap = new HashMap<>();
	private Map<String, FileHaunt> fileHauntMap = new HashMap<>();
	private Map<String, byte[]> fileBytesMap = new HashMap<>();
	
	public String getFileName(String fileCode) {
		return fileNameMap.get(fileCode);
	}
	
	public String getFileSuffix(String fileCode) {
		return fileSuffixMap.get(fileCode);
	}
	
	public InputStream getFileBodyIS(String fileCode) {
		byte[] bytes = fileBytesMap.get(fileCode);
		if(bytes==null) {
			publish(fileHauntMap.get(fileCode));
		}
		if(bytes!=null) {
			return new ByteArrayInputStream(bytes);
		}else {
			return null;
		}
		
	}
	
	public void setFileUtils(FileUtils fileUtils) {
		this.fileUtils = fileUtils;
	}
	
	
	public String publish(FileHaunt file) {
		if(file.getCode() != null) {
			if(fileNameMap.containsKey(file.getCode())) {
				return getURL(file);
			}else {
				try {
					//this.fileUtils.saveFile(file.getCode(), file.getInputStream());
					fileNameMap.put(file.getCode(), file.getFileName());
					fileSuffixMap.put(file.getCode(), file.getSuffix());
					fileHauntMap.put(file.getCode(), file);
					if(fileBytesMap.size()>100) {//缓存100个文件
						fileBytesMap=new HashMap<>();
					}
					if(file.getBytes()!=null) {
						fileBytesMap.put(file.getCode(), file.getBytes());
						file.resetBytes();
					}
					
					return getURL(file);
				} catch (Exception e) {
					logger.error("", e);
				}
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
