package com.BwleErp.controller.Procurement;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.BuyerPayment;
import com.base.model.entity.BwleErp.SystemSetting.Category;
import com.base.model.entity.BwleErp.SystemSetting.CategoryClassify;
import com.base.model.entity.BwleErp.SystemSetting.CategoryCommodity;
import com.base.model.entity.BwleErp.SystemSetting.CategorySystype;
import com.base.model.vo.BwleErp.Procurement.SupplierVo;
import com.base.model.vo.BwleErp.SystemSetting.CategoryVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 车种配置 作者 CooC email yemasky@msn.com
 */
@Component
public class SupplierAction extends AbstractAction {
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
		case "getSupplier":
			this.doGetSupplier(request, response);
			break;
		case "saveSupplier":
			this.doSaveSupplier(request, response);
			break;
		case "deleteSupplier":
			this.doDeleteSupplier(request, response);
			break;
		case "getPayment":
			this.doGetPayment(request, response);
			break;	
		case "getCommodity":
			this.doGetCommodity(request, response);
			break;
		case "getCategory":
			this.doGetCategory(request, response);
			break;	
		case "getClassify":
			this.doGetClassify(request, response);
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

	public void doGetSupplier(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("category_valid", 1).setTable_clazz(CategoryVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("category_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> categoryList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("categoryList", categoryList);
	}
	
	public void doSaveSupplier(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String edit_id = request.getParameter("edit_id");
		int supplier_id = 0;
		if(edit_id != null && !edit_id.equals("") && !edit_id.equals("undefined") && !edit_id.equals("0")) {
			supplier_id = Integer.parseInt(edit_id);;
		}
		SupplierVo supplierVo = this.modelMapper.map(request.getAttribute("supplier"), SupplierVo.class);
		supplierVo.setSupplier_id(null);
		if(supplier_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("category_id", supplier_id).setTable_clazz(Category.class);
			generalService.updateEntity(supplierVo, whereRelation);
		} else {
			supplier_id = generalService.save(supplierVo);
		}
		this.success.setItem("supplier_id", supplier_id);
	}
	
	public void doDeleteSupplier(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	
	public void doGetPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("payment_valid", 1).setTable_clazz(BuyerPayment.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> paymentList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("paymentList", paymentList);
	}
	
	public void doGetCommodity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("commodity_valid", 1).setTable_clazz(CategoryCommodity.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> commodityList = this.generalService.getList(whereRelation, needEncrypt);
		//
		whereRelation = new WhereRelation();
		whereRelation.EQ("systype_valid", 1).setTable_clazz(CategorySystype.class);
		needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> systypeList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("commodityList", commodityList);
		this.success.setItem("systypeList", systypeList);
	}
	
	public void doGetCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("category_valid", 1).setTable_clazz(Category.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		List<HashMap<String, Object>> categoryList = this.generalService.getList(whereRelation, needEncrypt);
		//
		this.success.setItem("categoryList", categoryList);
	}
	
	public void doGetClassify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 Object category_ids = request.getAttribute("category_ids");
		if(category_ids != null) {
			String[] ids = category_ids.toString().trim().split(",");
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("classify_valid", 1).IN("category_id", ids).setTable_clazz(CategoryClassify.class);
			NeedEncrypt needEncrypt = new NeedEncrypt();
			List<HashMap<String, Object>> classifyList = this.generalService.getList(whereRelation, needEncrypt);
			//
			this.success.setItem("classifyList", classifyList);
		}
	}
	///////////////////////
}
