package com.BwleErp.controller.Marketing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.BwleErp.config.Config;
import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.SystemSetting.MarketingCollection;
import com.base.model.vo.BwleErp.Marketing.FumamxToken;
import com.base.model.vo.BwleErp.SystemSetting.MarketingCollectionVo;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;
import com.base.util.EncryptUtiliy;
import com.google.gson.Gson;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Component
public class CustomerAction extends AbstractAction {
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
		case "syncCustomer"://
			this.doSyncCustomer(request, response);
			break;
		case "saveSalesPayment"://
			this.doSaveSalesPayment(request, response);
			break;
		case "deleteSalesPayment"://
			this.doDeleteSalesPayment(request, response);
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

	public void doSyncCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		FumamxApiAction fumamxApi = new FumamxApiAction(this.generalService);
		Thread thread = new Thread(fumamxApi);
		thread.start();
	}

	public void doGetSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setTable_clazz(MarketingCollectionVo.class);
		NeedEncrypt needEncrypt = new NeedEncrypt();
		needEncrypt.setNeedEncrypt(true);
		needEncrypt.setNeedEncrypt("collection_id", NeedEncrypt._ENCRYPT);
		List<HashMap<String, Object>> collectionList = generalService.getList(whereRelation, needEncrypt);
		this.success.setItem("collectionList", collectionList);
	}

	public void doSaveSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit_id = 0;
		if (_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id);
		}
		
		MarketingCollectionVo collection = this.modelMapper.map(request.getAttribute("collection"),
				MarketingCollectionVo.class);
		WhereRelation whereRelation = new WhereRelation();
		collection.setEmployee_id((Integer) request.getAttribute("employee_id"));
		if (edit_id > 0) {// update
			whereRelation.EQ("collection_id", edit_id).setTable_clazz(MarketingCollection.class);
			generalService.updateEntity(collection, whereRelation);
		} else {
			int id = generalService.save(collection);
			_edit_id = EncryptUtiliy.instance().intIDEncrypt(id);
		}
		this.success.setItem("currency_id", _edit_id);
		//
	}

	public void doDeleteSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String delete_id = (String) request.getAttribute("delete_id");
		int collection_id = 0;
		if (delete_id != null && !delete_id.equals("") && !delete_id.equals("undefined") && !delete_id.equals("0")) {
			collection_id = EncryptUtiliy.instance().intIDDecrypt(delete_id);
		}
		if (collection_id > 0) {// update
			WhereRelation whereRelation = new WhereRelation();
			whereRelation.EQ("collection_id", collection_id).setUpdate("collection_valid", 0)
					.setTable_clazz(MarketingCollection.class);
			generalService.update(whereRelation);
		}
	}

}
