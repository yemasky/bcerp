package com.base.dao.category;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import com.base.dao.BaseDao;
import com.base.dao.DBQueryDao;
import com.base.model.entity.category.Category;
import com.base.model.entity.upload.UploadFile;

import core.jdbc.mysql.WhereRelation;

public class CategoryDao extends BaseDao {
	DBQueryDao dBQueryDao;

	public CategoryDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		dBQueryDao = new DBQueryDao(jdbcDsn);
	}

	public int saveCategory(Category category) throws Exception {
		BigInteger category_id = (BigInteger) dBQueryDao.setDsn(this.jdbcDsn).intInsertObject(category);
		return category_id.intValue();
	}

	public Category getCategory(int category_id) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("category_id", category_id);
		return dBQueryDao.getEntity(Category.class, whereRelation);
	}
	
	public List<Category> getAllCategoryList(boolean category_valid) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("category_valid", category_valid);
		return dBQueryDao.getEntityList(Category.class, whereRelation);
	}
	
	public List<Category> getCategoryList(List<UploadFile> uploadFileList) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		return dBQueryDao.setDsn(this.jdbcDsn).getEntityList(Category.class, whereRelation);
	}
}
