package cho.carbon.hc.copframe.spring.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class FileUtils {
	private final String absPath;
	private final String folderUri;
	Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	
	public FileUtils(String absPath, String folderUri) {
		this.absPath = absPath;
		this.folderUri = folderUri;
	}
	
	public String saveFile(String fileName, InputStream in) throws IOException{
		File file = new File(absPath + "/" + fileName);
		File folder = file.getParentFile();
		if(!folder.exists()){
			folder.mkdirs();
		}
		file.createNewFile();
		FileOutputStream fo = new FileOutputStream(file);
		FileCopyUtils.copy(in, fo);
		return folderUri + "/" + fileName;
	}
	
	public File getFile(FileHaunt fileHaunt) {
		File file = new File(absPath + "/" + fileHaunt.getCode());
		if(file.exists()) {
			return file;
		}else {//尝试加载
			try {
				this.saveFile(fileHaunt.getCode(), fileHaunt.getInputStream());
				file = new File(absPath + "/" + fileHaunt.getCode());
				if(file.exists()) {
					return file;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public FileInputStream getInputStream(FileHaunt fileHaunt) throws FileNotFoundException {
		File file = getFile(fileHaunt);
		if(file != null) {
			return new FileInputStream(file);
		}else {
			throw new FileNotFoundException();
		}
	}
	

	public String getFolderUri() {
		return this.folderUri;
	}
}
