package com.BwleErp.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.BwleErp.config.Config;
import com.base.dao.category.CategoryDao;
import com.base.model.entity.category.Category;
import com.base.service.category.CategoryService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao;

    public CategoryServiceImpl() throws SQLException {
        categoryDao = new CategoryDao(Config.erpDSN);
    }

	@Override
	public int save(Object object) throws Exception {
		// TODO Auto-generated method stub
		return 0;
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
		return null;
	}

    @Override
    public Category getCategory(int category_id) throws SQLException {
        return categoryDao.getCategory(category_id);
    }

    @Cacheable(value="cache-24hour", key="'AllCategory'")
	@Override
	public List<Category> getAllCategoryList() throws SQLException {
		// TODO Auto-generated method stub
		return categoryDao.getAllCategoryList(true);
	}
	
    @Override
    public List<Category> getCategoryList(int[] category_ids) throws SQLException {
        return null;
    }

    @Override
    public int saveCategory(Category category) throws Exception {
        return categoryDao.saveCategory(category);
    }

    @Override
    public int saveCategory(HashMap<String, Object> insertData) throws Exception {
        return 0;
    }
    
    @Override
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
