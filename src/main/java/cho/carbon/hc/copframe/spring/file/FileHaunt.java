package cho.carbon.hc.copframe.spring.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamSource;

public interface FileHaunt extends InputStreamSource{
	/**
	 * 获得文件的唯一标志
	 * @return
	 */
	default String getCode() {
		return null;
	}
	
	
	@Override
	default InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(getBytes());
	}
	/**
	 * 获得文件名
	 * @return
	 */
	String getFileName();
	/**
	 * 获得文件后缀
	 * @return
	 */
	default String getSuffix() {
		String fileName = getFileName();
		if(fileName != null) {
			if(fileName.contains(".")) {
				int dotIndex = fileName.lastIndexOf('.');
				if(dotIndex >= 0) {
					return fileName.substring(dotIndex + 1, fileName.length());
				}
			}
		}
		return "";
	}
	/**
	 * 文件内容是否为空
	 * @return
	 */
	boolean isEmpty();
	long getSize();
	byte[] getBytes() throws IOException;
	default FileType getType() {
		return FileType.UNKNOWN;
	}
}
