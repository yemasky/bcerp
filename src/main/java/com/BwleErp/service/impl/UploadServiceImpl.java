package com.BwleErp.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.BwleErp.config.Config;
import com.base.dao.GeneralDao;
import com.base.model.dto.FileDTO;
import com.base.model.entity.upload.UploadFile;
import com.base.model.vo.upload.UploadFileVo;
import com.base.service.BwleErp.UploadService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import com.base.util.Utility;

@Service("xiaoqu.UploadServiceImpl")
public class UploadServiceImpl implements UploadService {
	private GeneralDao genernalDao;

	public UploadServiceImpl() throws SQLException {
		this.genernalDao = new GeneralDao(Config.erpDSN);
	}


	@Override
	public int save(Object object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(WhereRelation whereRelation) throws SQLException {
		return 0;
		// TODO Auto-generated method stub
	}

	@Override
	public Object getEntityByObject(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getEntityList(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HashMap<String, Object>> getList(Object object, String fieId) throws SQLException {
		// TODO Auto-generated method stub
		return genernalDao.getList(object, fieId, null);
	}

	@Override
	public void saveUploadFileDb(ArrayList<FileDTO> fileList, int category_id, int linked_id, String use_type, String privacy, int employee_id) throws Exception {
		ArrayList<UploadFile> uploadFilesList = new ArrayList<>();
		FileDTO fileDTO;
		int size = fileList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				fileDTO = fileList.get(i);
				UploadFile uploadFile = new UploadFile();
				uploadFile.setEmployee_id(employee_id);
				uploadFile.setFile_name(fileDTO.getFile_name());
				uploadFile.setCategory_id(category_id);
				uploadFile.setFile_linked_id(linked_id);
				uploadFile.setFile_type(fileDTO.getFile_type());
				uploadFile.setFile_use_type(use_type);
				uploadFile.setFile_url(fileDTO.getFile_url());
				uploadFile.setFile_size(fileDTO.getFile_size());
				uploadFile.setFile_extend(fileDTO.getFile_extend());
				uploadFile.setFile_year(Integer.parseInt(Utility.instance().getYear()));
				uploadFile.setFile_month(Integer.parseInt(Utility.instance().getMonth()));
				uploadFile.setFile_datetime(fileDTO.getFile_datetime());
				uploadFile.setFile_valid(1);
				if(privacy != null && privacy.equals("private")) uploadFile.setFile_valid(2);
				uploadFilesList.add(uploadFile);
			}
			genernalDao.batchSave(uploadFilesList);
		}
	}
	
	@Override
	public int batchSaveUploadFile(List<UploadFile> uploadFileList) throws Exception {
		// TODO Auto-generated method stub
		 return genernalDao.batchSave(uploadFileList);
	}
	
	@Override
	public List<UploadFile> getUpload(WhereRelation whereRelation) throws Exception {
		return genernalDao.getEntityList(whereRelation);
	}
	
	@Override
	public List<UploadFileVo> getUploadVo(WhereRelation whereRelation) throws Exception {
		return genernalDao.getEntityList(whereRelation);
	}
	@Override
	public int deleteFile(WhereRelation whereRelation) throws Exception {
		UploadFile uploadFile = new UploadFile();
		uploadFile.setFile_valid(0);
		return genernalDao.updateEntity(uploadFile, whereRelation);
	}

	@Override
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return genernalDao.getEntityList(whereRelation);
	}

	@Override
	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt) throws SQLException {
		// TODO Auto-generated method stub
		return genernalDao.getList(whereRelation, needEncrypt);
	}


	@Override
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
