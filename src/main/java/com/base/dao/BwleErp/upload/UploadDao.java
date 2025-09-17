package com.base.dao.BwleErp.upload;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.base.dao.BaseDao;
import com.base.dao.DBQueryDao;
import com.base.model.entity.upload.UploadFile;

import core.jdbc.mysql.WhereRelation;

public class UploadDao extends BaseDao {
	DBQueryDao dBQueryDao;
	public UploadDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		dBQueryDao = new DBQueryDao(jdbcDsn);
	}

	public int saveUpload(UploadFile uploadFile) throws Exception {
		return (int) dBQueryDao.setDsn(this.jdbcDsn).intInsertObject(uploadFile);
	}

	public int batchSaveUpload(List<UploadFile> uploadFileList) throws Exception {
		//需转换成long java 默认从mysql int取出来是 BigInteger
		BigInteger upload_id = (BigInteger) dBQueryDao.setDsn(this.jdbcDsn).batchInsertObject(uploadFileList);
		return upload_id.intValue();
	}
	
	public List<UploadFile> getUpload(WhereRelation whereRelation) throws Exception {
		return dBQueryDao.setDsn(this.jdbcDsn).getEntityList(UploadFile.class, whereRelation);
	}
	public List<HashMap<String, Object>> getList(Object object, String fieId) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setField(fieId);
		List<HashMap<String, Object>> objectList = dBQueryDao.setDsn(this.jdbcDsn).table(object.getClass())
				.getList(object, whereRelation);
		
		return objectList;
	}
	public int deleteFile(WhereRelation whereRelation) throws Exception {
		return dBQueryDao.table(UploadFile.class).update(whereRelation);
	}
}
