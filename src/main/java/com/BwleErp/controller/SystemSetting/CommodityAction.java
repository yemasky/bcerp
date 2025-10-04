package com.BwleErp.controller.SystemSetting;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.CategoryCommodity;
import com.base.model.entity.BwleErp.SystemSetting.CategoryCommodityAttribute;
import com.base.model.entity.BwleErp.SystemSetting.CategorySystype;
import com.base.model.entity.BwleErp.SystemSetting.CategoryUnit;
import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.CommoditySpare;
import com.base.model.vo.PageVoEntity;
import com.base.model.vo.BwleErp.SystemSetting.CategoryCommodityVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import core.jdbc.mysql.WhereRelation;
import com.base.util.Utility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CommodityAction extends AbstractAction {
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
		case "getCommodity":
			this.doGetCommodity(request, response);
			break;
		case "saveCommodity":
			this.doSaveCommodity(request, response);
			break;
		case "deleteAuditing":
			this.doDeleteAuditing(request, response);
			break;
		case "getAttribute":
			this.doGetAttribute(request, response);
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

	public void doGetCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object _page = request.getAttribute("page");//post
		int page = 1;
		if (_page != null && !_page.equals("")) {
			page = Integer.parseInt(_page.toString());
		}
		Integer perPage = (Integer) request.getAttribute("perPage");
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("systype_valid", 1).setTable_clazz(CategorySystype.class);
		List<CategorySystype> systypeList = this.generalService.getEntityList(whereRelation);
		//单位
		whereRelation = new WhereRelation();
		whereRelation.EQ("unit_valid", 1).setTable_clazz(CategoryUnit.class);
		List<CategoryUnit> unitList = this.generalService.getEntityList(whereRelation);
		//
		PageVoEntity<CategoryCommodity> commodityPage = new PageVoEntity<>();
		if(perPage == null) {
			perPage = commodityPage.getPerPage();
		} else {
			commodityPage.setPerPage(perPage);
		}
		commodityPage.setCurrentPage(page);
		//pageVo.setPerPage(perPage);
		whereRelation = new WhereRelation();
		whereRelation.EQ("commodity_valid", 1).setTable_clazz(CategoryCommodity.class);
		commodityPage = this.generalService.getPageEntityList(whereRelation, commodityPage);
		List<CategoryCommodity> commodityList = commodityPage.getPageList();
		List<CategoryCommodityVo> commodityVoList = new ArrayList<>();
		Type type = new TypeToken<HashMap<String, CommoditySpare>>() {}.getType();
		List<Integer> commodityIdList = new ArrayList<>();
		for(CategoryCommodity commodity : commodityList) {
			CategoryCommodityVo commodityVo = new CategoryCommodityVo();
			BeanUtils.copyProperties(commodity, commodityVo);
			commodityVo.setCommodity_id(EncryptUtiliy.instance().intIDEncrypt(commodity.getCommodity_id()));
			HashMap<String, CommoditySpare> spareList = new Gson().fromJson(commodity.getCommodity_spare(), type);
			commodityVo.setCommodity_spare(spareList);
			commodityVoList.add(commodityVo);
			commodityIdList.add(commodity.getCommodity_id());
		}
		PageVoEntity<CategoryCommodityVo> commodityVoPage = new PageVoEntity<>();
		BeanUtils.copyProperties(commodityPage, commodityVoPage);
		commodityVoPage.setPageList(commodityVoList);
		//判断工作流
		String channel = request.getParameter("channel");
		int module_id = EncryptUtiliy.instance().intIDDecrypt(channel);
		whereRelation = new WhereRelation();
		whereRelation.EQ("module_id", module_id).EQ("auditing_valid", 1).setTable_clazz(Auditing.class);
		Auditing auditing = (Auditing) this.generalService.getEntity(whereRelation);
		//
		this.success.setItem("systypeList", systypeList);
		this.success.setItem("unitList", unitList);
		this.success.setItem("commodityPage", commodityVoPage);
	}

	public void doSaveCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int commodity_id = 0;
		if (edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			commodity_id = EncryptUtiliy.instance().intIDDecrypt(edit_id);
		}
		CategoryCommodityVo commodityVo = this.modelMapper.map(request.getAttribute("commodity"),
				CategoryCommodityVo.class);
		CategoryCommodity commodity = new CategoryCommodity();
		BeanUtils.copyProperties(commodityVo, commodity);
		commodity.setCommodity_spare(new Gson().toJson(commodityVo.getCommodity_spare()));
		System.out.println("====>" + new Gson().toJson(commodityVo.getCommodity_spare()));
		//
		Type type = new TypeToken<HashMap<String, CategoryCommodityAttribute>>() {}.getType();
		HashMap<String, CategoryCommodityAttribute> attributeList = this.modelMapper
				.map(request.getAttribute("attribute"), type);
		HashMap<String, CategoryCommodityAttribute> hsList = this.modelMapper.map(request.getAttribute("hs"), type);
		HashMap<String, CategoryCommodityAttribute> imagesList = this.modelMapper.map(request.getAttribute("images"),
				type);

		List<CategoryCommodityAttribute> insertAttribute = new ArrayList<>();
		this.generalService.setTransaction(true);
		if (commodity_id > 0) {// update
			//
			if (attributeList != null) {
				for (String k : attributeList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(attributeList.get(k), insertData);
					insertData.setAttribute_type(edit_id);
					Integer check = attributeList.get(k).getAttribute_check();
					if (check == null)
						insertData.setAttribute_check(0);
					insertData.setAttribute_type("attr");
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			if (hsList != null) {
				for (String k : hsList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(hsList.get(k), insertData);
					insertData.setAttribute_type("hs");
					insertData.setAttribute_check(0);
					insertData.setAttribute_enkey("");
					insertData.setAttribute_enval("");
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			if (imagesList != null) {
				for (String k : imagesList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(imagesList.get(k), insertData);
					insertData.setAttribute_type("images");
					insertData.setAttribute_check(0);
					insertData.setAttribute_enkey("");
					insertData.setAttribute_enval("");
					insertData.setAttribute_key(k);
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("commodity_id", commodity_id).setTable_clazz(CategoryCommodity.class);
			this.generalService.updateEntity(commodity, whereRelation);
			//
			new WhereRelation();
			whereRelation.EQ("commodity_id", commodity_id).setTable_clazz(CategoryCommodityAttribute.class);
			this.generalService.delete(whereRelation);
			//
			this.generalService.batchSave(insertAttribute);
		} else {
			commodity.setAdd_datetime(Utility.instance().getTodayDate());
			commodity_id = this.generalService.save(commodity);
			if (attributeList != null) {
				for (String k : attributeList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(attributeList.get(k), insertData);
					insertData.setAttribute_type(edit_id);
					Integer check = attributeList.get(k).getAttribute_check();
					if (check == null)
						insertData.setAttribute_check(0);
					insertData.setAttribute_type("attr");
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			if (hsList != null) {
				for (String k : hsList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(hsList.get(k), insertData);
					insertData.setAttribute_type("hs");
					insertData.setAttribute_check(0);
					insertData.setAttribute_enkey("");
					insertData.setAttribute_enval("");
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			if (imagesList != null) {
				for (String k : imagesList.keySet()) {
					CategoryCommodityAttribute insertData = new CategoryCommodityAttribute();
					BeanUtils.copyProperties(imagesList.get(k), insertData);
					insertData.setAttribute_type("images");
					insertData.setAttribute_check(0);
					insertData.setAttribute_enkey("");
					insertData.setAttribute_enval("");
					insertData.setAttribute_key(k);
					insertData.setCommodity_id(commodity_id);
					insertAttribute.add(insertData);
				}
			}
			this.generalService.batchSave(insertAttribute);
			edit_id = EncryptUtiliy.instance().intIDEncrypt(commodity_id);
			//
		}
		this.generalService.commit();
		this.success.setItem("auditing_id", edit_id);
	}

	public void doDeleteAuditing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int auditing_id = 0;
		if (delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			auditing_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if (auditing_id > 0) {// update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("auditing_id", auditing_id).setUpdate("auditing_valid", 0).setTable_clazz(Auditing.class);
			generalService.update(whereRelation);
		}
	}
	
	public void doGetAttribute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _id = request.getParameter("attr_id");
		int attr_id = 0;
		if (_id != null && !_id.equals("") && !_id.equals("undefined") && !_id.equals("0")) {
			attr_id = EncryptUtiliy.instance().intIDDecrypt(_id);
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.setTable_clazz(CategoryCommodityAttribute.class).EQ("commodity_id", attr_id);
			List<CategoryCommodityAttribute> attributeList = this.generalService.getEntityList(whereRelation);
			
			this.success.setItem("attributeList", attributeList);
		}
	}

}
