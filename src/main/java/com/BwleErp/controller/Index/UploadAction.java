package com.BwleErp.controller.Index;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BwleErp.config.Config;
import com.base.controller.AbstractAction;
import com.base.model.dto.FileDTO;
import com.base.model.vo.KEditorUImages;
import com.base.service.BwleErp.UploadService;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;

import core.jdbc.mysql.WhereRelation;
import core.util.FileUpload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("BwleErp.index.UploadAction")
public class UploadAction extends AbstractAction {
	private int employee_id;
	@Autowired
	private UploadService uploadService;

	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub    
		int employee_id = (int)request.getAttribute("employee_id");
		if (employee_id > 0) {
			this.employee_id = employee_id;
			return this.status;
		}
		
		this.status.setError(ErrorCode.__F_NO_LOGIN);;
		this.status.setStatus(false);
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if(method == null) method = "";
		//
		switch (method) {
		case "uploadCompanyLogo":
			this.doUploadImages(request, response);
			break;	
		case "uploadEmployeeAvatar":
			this.doUploadImages(request, response);
			break;		
		case "fileManager":
			this.doFileManager(request, response);
			break;	
		case "deleteImg":
			this.doDeleteImg(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}		
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void doUploadImages(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			//
			String UseType = request.getParameter("UseType");
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("employee_id", this.employee_id).EQ("file_use_type", UseType).setUpdate("file_valid", 0);
			//uploadService.update(whereRelation);
			uploadService.saveUploadFileDb(fileList, 0, 0, UseType, "", this.employee_id);
			KEditorUImages kImages = new KEditorUImages();
			kImages.setUrl(fileList.get(0).getFile_url());
			this.success.setItem("images", kImages);
			this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
			return;
		}
		this.success.setErrorCode(ErrorCode.__F_UPLOAD);
		
	
	}
	
	public void doFileManager(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}
	
	public void doDeleteImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delimg = request.getParameter("delimg");
		//String linked_id = request.getParameter("linked_id");
		//String category_id = request.getParameter("category_id");
		
		if(delimg != null && !delimg.equals("")) {
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("employee_id", this.employee_id).EQ("file_url", delimg);
			uploadService.deleteFile(whereRelation);
		}
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
}
