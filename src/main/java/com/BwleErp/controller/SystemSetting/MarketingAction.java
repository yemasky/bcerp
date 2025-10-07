package com.BwleErp.controller.SystemSetting;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.MarketingCollection;
import com.base.model.vo.BwleErp.SystemSetting.BuyerPaymentVo;
import com.base.model.vo.BwleErp.SystemSetting.MarketingCollectionVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * 付款方式 作者 CooC email yemasky@msn.com
 */
@Component
public class MarketingAction extends AbstractAction {
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
		if (method == null) method = "";
		//
		switch (method) {
		case "getSalesCollection"://
			this.doGetSalesCollection(request, response);
			break;
		case "saveSalesCollection"://
			this.doSaveSalesCollection(request, response);
			break;	
		case "deleteSalesCollection"://
			this.doDeleteSalesCollection(request, response);
			break;	
		case "getBuyerPayment"://
			this.doGetBuyerPayment(request, response);
			break;
		case "saveBuyerPayment"://
			this.doSaveBuyerPayment(request, response);
			break;	
		case "deleteBuyerPayment"://
			this.doDeleteBuyerPayment(request, response);
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
		
	public void doGetSalesCollection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(MarketingCollectionVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("collection_id", NeedEncrypt._ENCRYPT);
		 List<HashMap<String, Object>> collectionList = generalService.getList(whereRelation, needEncrypt);
		this.success.setItem("collectionList", collectionList);
	}
	
	public void doSaveSalesCollection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit_id = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id);
		}
		// TODO Auto-generated method stub
		MarketingCollectionVo collection = this.modelMapper.map(request.getAttribute("collection"), MarketingCollectionVo.class);
		WhereRelation whereRelation = new WhereRelation();
		collection.setEmployee_id((Integer) request.getAttribute("employee_id"));
		if(edit_id > 0) {//update
			whereRelation.EQ("collection_id", edit_id).setTable_clazz(MarketingCollection.class);
			generalService.updateEntity(collection, whereRelation);
		} else {
			int id = generalService.save(collection);
			_edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("currency_id", _edit_id);
		//
	}
	
	public void doDeleteSalesCollection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int collection_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			collection_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(collection_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("collection_id", collection_id).setUpdate("collection_valid", 0).setTable_clazz(MarketingCollection.class);
			generalService.update(whereRelation);
		}
	}
	
	public void doGetBuyerPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(BuyerPaymentVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("payment_id", NeedEncrypt._ENCRYPT);
		 List<HashMap<String, Object>> paymentList = generalService.getList(whereRelation, needEncrypt);
		this.success.setItem("paymentList", paymentList);
	}
	
	public void doSaveBuyerPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit_id = 0;
		if(_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id);
		}
		// TODO Auto-generated method stub
		BuyerPaymentVo payment = this.modelMapper.map(request.getAttribute("payment"), BuyerPaymentVo.class);
		WhereRelation whereRelation = new WhereRelation();
		payment.setEmployee_id((Integer) request.getAttribute("employee_id"));
		if(edit_id > 0) {//update
			whereRelation.EQ("payment_id", edit_id).setTable_clazz(BuyerPaymentVo.class);
			generalService.updateEntity(payment, whereRelation);
		} else {
			int id = generalService.save(payment);
			_edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("payment_id", _edit_id);
		//
	}
	
	public void doDeleteBuyerPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int payment_id = 0;
		if(delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			payment_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if(payment_id > 0) {//update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("payment_id", payment_id).setUpdate("payment_valid", 0).setTable_clazz(BuyerPaymentVo.class);
			generalService.update(whereRelation);
		}
	}
}
