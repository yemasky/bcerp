package com.BwleErp.controller.SystemSetting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.CommDict;
import com.base.model.entity.BwleErp.SystemSetting.CommDictModule;
import com.base.model.entity.BwleErp.SystemSetting.module.Modules;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 字典 作者 CooC email yemasky@msn.com
 */
@Component
public class CommDictAction extends AbstractAction {
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
		case "getCommDict":
			this.doGetCommDict(request, response);
			break;
		case "saveCommDict":
			this.doSaveCommDict(request, response);
			break;
		case "deleteCommDict":

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

	public void doGetCommDict(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(CommDictModule.class);
		List<CommDictModule> dictModuleList = this.generalService.getEntityList(whereRelation);
		List<Integer> module_idsList = new ArrayList<>();
		for(CommDictModule CM : dictModuleList) {
			module_idsList.add(CM.getModule_id());
			module_idsList.add(CM.getModule_father_id());
		}
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Modules.class).IN("module_id", module_idsList).setField("module_id, module_name");
		List<Object> moduleList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(CommDict.class);
		List<CommDict> dictList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("dictModuleList", dictModuleList);
		this.success.setItem("moduleList", moduleList);
		this.success.setItem("dictList", dictList);
	}
	
	public void doSaveCommDict(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int dict_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			dict_id = Integer.parseInt(edit_id);
		}
		CommDict dict = this.modelMapper.map(request.getAttribute("dict"), CommDict.class);	
		if(dict.getDict_val() != null) dict.setDict_val(dict.getDict_val().trim());
		if(dict.getDict_linkage() != null) dict.setDict_linkage(dict.getDict_linkage().trim());
		if(dict_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("dict_id", dict_id).setTable_clazz(CommDict.class);
			this.generalService.updateEntity(dict, whereRelation);
		} else {
			dict.setDict_id(null);
			int id = this.generalService.save(dict);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("dict_id", edit_id);
	}
	
	public void doDeleteCommDict(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int dict_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			dict_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(dict_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("dict_id", dict_id).setUpdate("dict_valid", 0).setTable_clazz(CommDict.class);
			this.generalService.update(whereRelation);
		}
	}
	
}
