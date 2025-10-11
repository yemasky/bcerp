package com.BwleErp.controller.ProductMGT;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.Auditing;
import com.base.model.entity.BwleErp.SystemSetting.CategoryBoxSandard;
import com.base.model.entity.BwleErp.SystemSetting.CategoryClassify;
import com.base.model.entity.BwleErp.SystemSetting.CategoryCommodity;
import com.base.model.entity.BwleErp.SystemSetting.CategoryCommodityAttribute;
import com.base.model.entity.BwleErp.SystemSetting.CategorySystype;
import com.base.model.entity.BwleErp.SystemSetting.CategoryUnit;
import com.base.model.entity.BwleErp.SystemSetting.VehicleModel;
import com.base.model.vo.BwleErp.SystemSetting.CategoryVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 产品箱规 作者 CooC email yemasky@msn.com
 */
@Component
public class ProductLibAction extends AbstractAction {
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
		case "getProduct":
			this.doGetgetProduct(request, response);
			break;
		case "saveProduct":
			this.doSaveProduct(request, response);
			break;
		case "deleteProduct":
			this.doDeleteProduct(request, response);
			break;
		case "getCommodity":
			this.doGetCommodity(request, response);
			break;	
		case "getCommodityAttr":
			this.doGetCommodityAttr(request, response);
			break;	
		case "getClassify":
			this.doGetClassify(request, response);
			break;	
		case "getVehicleModel":
			this.doGetVehicleModel(request, response);
			break;		
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doGetgetProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("systype_valid", 1).setTable_clazz(CategorySystype.class);
		List<CategorySystype> systypeList = this.generalService.getEntityList(whereRelation);
		//
		whereRelation = new WhereRelation();
		whereRelation.EQ("category_valid", 1).setTable_clazz(CategoryVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> categoryList = this.generalService.getList(whereRelation, needEncrypt);
		//单位
		whereRelation = new WhereRelation();
		whereRelation.EQ("unit_valid", 1).setTable_clazz(CategoryUnit.class);
		List<CategoryUnit> unitList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("categoryList", categoryList);
		this.success.setItem("systypeList", systypeList);
		this.success.setItem("unitList", unitList);
	}
	
	public void doSaveProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int product_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			product_id = Integer.parseInt(edit_id);
		}
		CategoryBoxSandard box = this.modelMapper.map(request.getAttribute("box"), CategoryBoxSandard.class);
		if(product_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("product_id", product_id).setTable_clazz(Auditing.class);
			generalService.updateEntity(box, whereRelation);
		} else {
			box.setBox_id(null);
			product_id = generalService.save(box);
		}
		this.success.setItem("product_id", product_id);
	}
	
	public void doDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int product_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			product_id = Integer.parseInt(delete_id);
		}
		if(product_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("product_id", product_id).setUpdate("box_valid", 0).setTable_clazz(CategoryBoxSandard.class);
			generalService.update(whereRelation);
		}
	}
	
	public void doGetCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("commodity_valid", 1).setTable_clazz(CategoryCommodity.class);
		List<CategoryCommodity> commodityList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("commodityList", commodityList);
	}
	
	public void doGetCommodityAttr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String commodity_id = (String) request.getParameter("commodity_id");
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("commodity_id", commodity_id).setTable_clazz(CategoryCommodityAttribute.class);
		List<CategoryCommodityAttribute> commodityAttrList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("commodityAttrList", commodityAttrList);
	}
	
	public void doGetClassify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("classify_valid", 1).setTable_clazz(CategoryClassify.class);
		List<CategoryClassify> classifyList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("classifyList", classifyList);
	}
	
	public void doGetVehicleModel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("vehicle_valid", 1).setTable_clazz(VehicleModel.class);
		List<VehicleModel> vehicleModelList = this.generalService.getEntityList(whereRelation);
		whereRelation = new WhereRelation();
		whereRelation.EQ("classify_valid", 1).setTable_clazz(CategoryClassify.class);
		List<CategoryClassify> classifyList = this.generalService.getEntityList(whereRelation);
		//
		this.success.setItem("classifyList", classifyList);
		this.success.setItem("vehicleModelList", vehicleModelList);
	}
}
