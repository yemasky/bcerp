package com.BwleErp.controller.index;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.BwleErp.config.Category;
import com.BwleErp.config.Config;
import com.base.controller.AbstractAction;
import com.base.model.dto.FileDTO;
import com.base.model.vo.upload.ChatFile;
import com.base.service.BwleErp.NewsService;
import com.base.service.BwleErp.UploadService;
import com.base.type.CheckedStatus;
import com.base.type.ErrorCode;
import com.base.type.UseType;
import com.base.util.EncryptUtiliy;
import com.google.gson.Gson;

import core.jdbc.mysql.WhereRelation;
import core.util.FileUpload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("xiaoqu.index.UploadAction")
public class UploadAction extends AbstractAction {
	private int member_id;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private NewsService newsService;

	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String _m = (String) request.getHeader("_m");
		if(_m != null && !_m.equals("")) {
			int member_id = EncryptUtiliy.instance().myIDDecrypt(_m);
			if (member_id > 0) {
				this.member_id = member_id;
				return this.status;
			}
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
		case "uploadAvatar":
			this.doUploadAvatar(request, response);
			break;	
		case "uploadIDCard":
			this.doUploadIDCard(request, response);
			break;
		case "uploadhouseAlbum":
			this.doUploadhouseAlbum(request, response);
			break;	
		case "uploadAuthHouse":
			this.doUploadAuthHouse(request, response);
			break;	
		case "uploadNewsAlbum":
			this.doUploadNewsAlbum(request, response);
			break;	
		case "uploadRijiAlbum":
			this.doUploadRijiAlbum(request, response);
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
	
	public void doUploadAvatar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			String avatar = fileList.get(0).getFile_url();
			HashMap<String, Object> updateData = new HashMap<>();
			updateData.put("member_avatar", avatar);
			//
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("member_id", this.member_id).EQ("file_use_type", UseType.Avatar).setUpdate("file_valid", 0);
			uploadService.update(whereRelation);
			uploadService.saveUploadFileDb(fileList, 0, 0, UseType.Avatar, "", this.member_id);
		}
		
		this.success.setItem("avatar_url", fileList.get(0).getFile_url());
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doUploadIDCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String use_type = request.getParameter("use_type");
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
			//检查之前认证情况
			//实名认证0未认证 1已上传等待认证 2已认证身份证 3已认证学信网 4已认证学信网已上传身份证 5身份证学信网全部认证

		
	
	}
	
	public void doDeleteImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delimg = request.getParameter("delimg");
		//String linked_id = request.getParameter("linked_id");
		//String category_id = request.getParameter("category_id");
		
		if(delimg != null && !delimg.equals("")) {
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("member_id", this.member_id).EQ("file_url", delimg);
			uploadService.deleteFile(whereRelation);
		}
		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doUploadhouseAlbum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String linked_id = request.getParameter("linked_id");
		String use_type = request.getParameter("use_type");
		String _i = request.getParameter("_i");
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			//
			int _house_id = EncryptUtiliy.instance().getIntIDDecrypt(linked_id);
			uploadService.saveUploadFileDb(fileList, Category.HouseForRent, _house_id, use_type, "", this.member_id);
			if (_i != null && _i.equals("0")) {
				String chat_file = request.getParameter("chat_file");
				ChatFile chatFile = new Gson().fromJson(chat_file, ChatFile.class);
				
				HashMap<String, Object> updateData = new HashMap<>();
				updateData.put("house_img_src", fileList.get(0).getFile_url());//
				if(chatFile.getType().equals("img")) {
					updateData.put("house_img_ext", "{\"h\":"+chatFile.getH()+",\"w\":"+chatFile.getW()+"}");
				} 
				this.success.setItem("linked_id", linked_id);
			}
			fileList.get(0).setFile_datetime(null);
			fileList.get(0).setFile_size(null);
			fileList.get(0).setFile_use_type(null);
			this.success.setItem("imagesList", fileList);
		}

		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doUploadAuthHouse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String linked_id = request.getParameter("linked_id");
		System.out.println("=>"+linked_id);
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			//
			int _house_id = EncryptUtiliy.instance().getIntIDDecrypt(linked_id);
			if(_house_id == 0) {
				throw new Exception("解析 linked_id 失败："+linked_id+"==>"+request.getAttribute("linked_id")+"==>"+request.getParameter("linked_id"));
			}
			uploadService.saveUploadFileDb(fileList, Category.HouseForRent, _house_id, UseType.AuthHouse, "", this.member_id);
			
			fileList.get(0).setFile_datetime(null);
			fileList.get(0).setFile_size(null);
			fileList.get(0).setFile_use_type(null);
			this.success.setItem("imagesList", fileList);
		}

		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doUploadNewsAlbum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String linked_id = request.getParameter("linked_id");
		String use_type = request.getParameter("use_type");
		String _i = request.getParameter("_i");
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			//
			int _news_id = EncryptUtiliy.instance().getIntIDDecrypt(linked_id);
			uploadService.saveUploadFileDb(fileList, Category.News, _news_id, use_type, "", this.member_id);
			if (_i != null && _i.equals("0")) {
				String chat_file = request.getParameter("chat_file");
				ChatFile chatFile = new Gson().fromJson(chat_file, ChatFile.class);
				
				HashMap<String, Object> updateData = new HashMap<>();
				updateData.put("news_img_src", fileList.get(0).getFile_url());//
				if(chatFile.getType().equals("img")) {
					updateData.put("news_img_ext", "{\"h\":"+chatFile.getH()+",\"w\":"+chatFile.getW()+"}");
				} 
				WhereRelation whereRelation = new WhereRelation();
				whereRelation.setUpdate(updateData).EQ("news_id", _news_id).EQ("member_id", this.member_id);
				newsService.update(whereRelation);
				//(updateData, _news_id, this.member_id);
				this.success.setItem("linked_id", linked_id);
			}
			fileList.get(0).setFile_datetime(null);
			fileList.get(0).setFile_size(null);
			fileList.get(0).setFile_use_type(null);
			this.success.setItem("imagesList", fileList);
		}

		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	public void doUploadRijiAlbum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String linked_id = request.getParameter("linked_id");
		String use_type = request.getParameter("use_type");
		String _i = request.getParameter("_i");
		ArrayList<FileDTO> fileList = FileUpload.instance().multiSpringUpload(request, Config.uploadPath);
		int size = fileList.size();
		if (size > 0) {
			//
			int _riji_id = EncryptUtiliy.instance().getIntIDDecrypt(linked_id);
			uploadService.saveUploadFileDb(fileList, Category.Riji, _riji_id, use_type, "", this.member_id);
			if (_i != null && _i.equals("0")) {
				String chat_file = request.getParameter("chat_file");
				ChatFile chatFile = new Gson().fromJson(chat_file, ChatFile.class);
				
				HashMap<String, Object> updateData = new HashMap<>();
				updateData.put("riji_img_src", fileList.get(0).getFile_url());//
				if(chatFile.getType().equals("img")) {
					updateData.put("riji_img_ext", "{\"h\":"+chatFile.getH()+",\"w\":"+chatFile.getW()+"}");
				} 
				WhereRelation whereRelation = new WhereRelation();
				whereRelation.setUpdate(updateData).EQ("riji_id", _riji_id).EQ("member_id", this.member_id);
				//(updateData, _news_id, this.member_id);
				this.success.setItem("linked_id", linked_id);
			}
			fileList.get(0).setFile_datetime(null);
			fileList.get(0).setFile_size(null);
			fileList.get(0).setFile_use_type(null);
			this.success.setItem("imagesList", fileList);
		}

		this.success.setSuccessCode(ErrorCode.__T_SUCCESS);
	}
	
	
}
