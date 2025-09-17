package com.base.service.category;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.base.model.entity.category.Category;
import com.base.service.BaseService;

public interface CategoryService extends BaseService {
	
	public Category getCategory(int category_id) throws SQLException;
	
	public List<Category> getAllCategoryList() throws SQLException;
	
	public List<Category> getCategoryList(int[] category_ids) throws SQLException;
	
	public int saveCategory(Category category) throws Exception;

	public int saveCategory(HashMap<String, Object> insertData) throws Exception;
}
