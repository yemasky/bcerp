package core.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.base.model.dto.FileDTO;
import com.base.util.ImagesUtil;

//import javax.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

public class FileUpload {
	public static FileUpload instance() {
		return new FileUpload();
	}
	//上传路径
	public String getDatePath(String diskPath) {
		Date date = new Date();
		String datePath = new SimpleDateFormat("yyyy").format(date);
		diskPath += datePath;
		File file = new File(diskPath);// 如果不存在,创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		datePath = new SimpleDateFormat("MMdd").format(date);
		diskPath += "/" + datePath;// 如果不存在,创建文件夹
		file = new File(diskPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return diskPath + "/";
	}
	
	public String formatDate(String formatData) {
        Date date = new Date();//formatData = "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat format = new SimpleDateFormat(formatData);//
        String dateStr = format.format(date);
        return dateStr;
    }
	/*
	 * spring 上传文件
	 */
	public ArrayList<FileDTO> multiSpringUpload(HttpServletRequest request, String savePath) throws Exception { 
		int employee_id = (int) request.getAttribute("employee_id");
		String diskPath = this.getDatePath(savePath);
		String urlPath = diskPath.replace(savePath, "");
		// long startTime = System.currentTimeMillis();
		ArrayList<FileDTO> fileDTOList = new ArrayList<FileDTO>();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		//CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		//ServletContext servletContext = request.getSession().getServletContext();
		//if (servletContext.isMultipart(request)) {// 检查form中是否有enctype="multipart/form-data"
		if(multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				//System.out.println("=====================file=>"+file);
				if (file != null) {
					String originalFilename = file.getOriginalFilename();
					int n = originalFilename.lastIndexOf(".");
					String newName = employee_id + this.formatDate("HHmmss")+(int)((Math.random()*9+1)*100000);
					String suffix =  originalFilename.substring(n);
					String path = diskPath + newName + suffix;
					// 上传
					file.transferTo(new File(path));
					//压缩 生成小图
					ImagesUtil.instance().compress(path);
					//ImagesUtil.instance().reduceImage();
					
					FileDTO fileDTO = new FileDTO();
					//fileDTO.setFile_name(Encrypt.md5Lower(originalFilename));
					fileDTO.setFile_type(file.getContentType());
					fileDTO.setFile_size((int) file.getSize());
					fileDTO.setFile_url(urlPath + newName + suffix);
					fileDTO.setFile_datetime(this.formatDate("yyyy-MM-dd HH:mm:ss"));
					//扩展信息
					
					fileDTOList.add(fileDTO);
				}
			}
		} else {
			throw new Exception("上传失败没有找到 multipartResolver.isMultipart.");
		}
		// long endTime = System.currentTimeMillis();
		// System.out.println("运行时间：" + String.valueOf(endTime - startTime)+"ms");
		return fileDTOList;
	}


}
