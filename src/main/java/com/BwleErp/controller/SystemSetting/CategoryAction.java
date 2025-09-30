package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.Category;
import com.base.model.vo.BwleErp.CategoryVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CategoryAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if (method == null)
			method = "";
		//
		switch (method) {
		case "getCategory":
			this.doGetCategory(request, response);
			break;
		case "saveCategory":
			this.doSaveCategory(request, response);
			break;
		case "deleteCategory":
			this.doDeleteCategory(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		generalService.closeConnection();
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doGetCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("category_valid", 1).setTable_clazz(CategoryVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("category_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> companyList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("categoryList", companyList);
	}
	
	public void doSaveCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int category_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			System.out.println("====>"+edit_id);
			category_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		CategoryVo categoryVo = this.modelMapper.map(request.getAttribute("category"), CategoryVo.class);
		categoryVo.setCategory_id(null);
		if(category_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("category_id", category_id).setTable_clazz(Category.class);
			generalService.updateEntity(categoryVo, whereRelation);
		} else {
			int id = generalService.save(categoryVo);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("category_id", edit_id);
	}
	
	public void doDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int category_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			category_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(category_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("category_id", category_id).setUpdate("category_valid", 0).setTable_clazz(Category.class);
			generalService.update(whereRelation);
		}
	}

}
