package com.BwleErp.controller.ProductMGT;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.ProductMGT.Product;
import com.base.model.entity.BwleErp.ProductMGT.ProductAttribute;
import com.base.model.entity.BwleErp.ProductMGT.ProductClassify;
import com.base.model.entity.BwleErp.ProductMGT.ProductFactory;
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
import com.base.util.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
		
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
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
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		

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
		//产品
		whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(Product.class);
		List<Product> productList = this.generalService.getEntityList(whereRelation);
		
		//
		this.success.setItem("categoryList", categoryList);
		this.success.setItem("systypeList", systypeList);
		this.success.setItem("unitList", unitList);
		this.success.setItem("productList", productList);
	}
	
	public void doSaveProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int product_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			product_id = Integer.parseInt(edit_id);
		}
		Product product = this.modelMapper.map(request.getAttribute("product"), Product.class);
		Type type = new TypeToken<HashMap<String, ProductAttribute>>() {}.getType();
		Type pcType = new TypeToken<HashMap<String, ProductClassify>>() {}.getType();
		Type pfType = new TypeToken<HashMap<String, ProductFactory>>() {}.getType();
		Type stringType = new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType();
		Type listType = new TypeToken<List<HashMap<String, String>>>() {}.getType();
		Type stringListType = new TypeToken<HashMap<String, List<String>>>() {}.getType();
		HashMap<String, ProductAttribute> imagesAttr = this.modelMapper.map(request.getAttribute("images"), type);
		HashMap<String, ProductAttribute> productAttr = this.modelMapper.map(request.getAttribute("productAttr"), type);
		HashMap<String, ProductClassify> productClassify = this.modelMapper.map(request.getAttribute("classify"), pcType);
		HashMap<String, List<String>> oems = this.modelMapper.map(request.getAttribute("oem"), stringListType);
		List<HashMap<String, String>> factoryNum = this.modelMapper.map(request.getAttribute("factoryNum"), listType);
		HashMap<String, HashMap<String, String>> engineNum = this.modelMapper.map(request.getAttribute("engineNum"), stringType);
		HashMap<String,ProductFactory> pfactory = this.modelMapper.map(request.getAttribute("pfactory"), pfType);
		Gson gson = new Gson();
		this.generalService.setTransaction(true);
		if(product_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("product_id", product_id).setTable_clazz(Auditing.class);
			this.generalService.updateEntity(product, whereRelation);
		} else {
			product.setProduct_id(null);
			product.setEmployee_id((int)request.getAttribute("employee_id"));
			product.setProduct_add_date(Utility.instance().getTodayDate());
			product_id = this.generalService.save(product);
			List<ProductAttribute> attrList = new ArrayList<>();
			for(String key : imagesAttr.keySet()) {
				ProductAttribute attr = imagesAttr.get(key);
				attr.setProduct_id(product_id);
				attrList.add(attr);
			}
			for(String key : productAttr.keySet()) {
				ProductAttribute attr = productAttr.get(key);
				attr.setProduct_id(product_id);
				attr.setAttribute_type("attr");
				attr.setAttribute_id(Integer.parseInt(key));
				attrList.add(attr);
			}
			this.generalService.batchSave(attrList);
			List<ProductClassify> classifyList = new ArrayList<>();
			for(String key : productClassify.keySet()) {
				ProductClassify attr = productClassify.get(key);
				attr.setProduct_id(product_id);
				attr.setOems(gson.toJson(oems.get(key)));
				classifyList.add(attr);
			}
			this.generalService.batchSave(classifyList);
			List<ProductFactory> pfactoryList = new ArrayList<>();
			
			for(String key : pfactory.keySet()) {
				ProductFactory attr = pfactory.get(key);
				attr.setProduct_id(product_id);
				attr.setEngine_num(gson.toJson(engineNum.get(key)));
				attr.setFactory_num(gson.toJson(factoryNum.get(Integer.parseInt(key))));
				pfactoryList.add(attr);
			};
			this.generalService.batchSave(pfactoryList);
		}
		this.generalService.commit();
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
