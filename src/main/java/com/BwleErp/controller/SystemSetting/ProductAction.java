package com.BwleErp.controller.SystemSetting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.CategoryBoxSandard;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 产品箱规 作者 CooC email yemasky@msn.com
 */
@Component
public class ProductAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = (String) request.getAttribute("method");
		if (method == null)
			method = "";
		//
		switch (method) {
		case "getBox":
			this.doGetBox(request, response);
			break;
		case "saveBox":
			this.doSaveBox(request, response);
			break;
		case "deleteBox":
			this.doDeleteBox(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

	}

	public void doGetBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(CategoryBoxSandard.class);
		List<CategoryBoxSandard> boxList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("boxList", boxList);
	}
	
	public void doSaveBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int box_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			box_id = Integer.parseInt(edit_id);
		}
		CategoryBoxSandard box = this.modelMapper.map(request.getAttribute("box"), CategoryBoxSandard.class);
		if(box_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("box_id", box_id).setTable_clazz(Auditing.class);
			generalService.updateEntity(box, whereRelation);
		} else {
			box.setBox_id(null);
			box_id = generalService.save(box);
		}
		this.success.setItem("box_id", box_id);
	}
	
	public void doDeleteBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int box_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			box_id = Integer.parseInt(delete_id);
		}
		if(box_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("box_id", box_id).setUpdate("box_valid", 0).setTable_clazz(CategoryBoxSandard.class);
			generalService.update(whereRelation);
		}
	}
	
}
