package com.base.service.BwleErp;

import java.util.ArrayList;
import java.util.List;

import com.base.model.dto.FileDTO;
import com.base.model.entity.upload.UploadFile;
import com.base.model.vo.upload.UploadFileVo;
import com.base.service.BaseService;

import core.jdbc.mysql.WhereRelation;

public interface UploadService extends BaseService {

	void saveUploadFileDb(ArrayList<FileDTO> fileList, int category_id, int linked_id, String use_type, String t_privacy, int member_id) throws Exception;
	
	int batchSaveUploadFile(List<UploadFile> uploadFileList) throws Exception;

	List<UploadFile> getUpload(WhereRelation whereRelation) throws Exception;
	
	List<UploadFileVo> getUploadVo(WhereRelation whereRelation) throws Exception;

	int deleteFile(WhereRelation whereRelation) throws Exception;
}