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
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub

	}

	public void doSyncCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String url = "https://opengw.fumamx.com/auth-server/open/acquire_token";
		String appid = "I4UfdbMi";
		String appsecret = "40270f10408f94db496ed96c088ce84d1fc77b98";
		String tonkeCode = "fumamxTonkeCode";
		CacheManager cacheManager = CacheManager.create();// .getCacheManager(Config.cache24hour);
		Cache cache = cacheManager.getCache(Config.cache120min);//120分鐘 即2小時
		String token = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost;
		JSONObject json;
		String responses = "";
		FumamxToken fumamxToken = new FumamxToken();
		if (cache != null) {
			Element getGreeting = cache.get(tonkeCode);
			if (getGreeting != null) {//取出緩存
				Object obj = getGreeting.getObjectValue();
				if(obj instanceof FumamxToken) {
					fumamxToken = (FumamxToken) obj;
				}
			} else {
				httpPost = new HttpPost(url);
				json = new JSONObject();
				json.put("appId",appid);
				json.put("appSecret",appsecret);
				httpPost.addHeader("Content-Type", "application/json");
				httpPost.setEntity(new StringEntity(json.toString()));
				responses = httpClient.execute(httpPost,new BasicHttpClientResponseHandler());
				fumamxToken = new Gson().fromJson(responses, FumamxToken.class);
				//token = fumamxToken.getData().getAccessToken();
				//新緩存
				Element putGreeting = new Element(tonkeCode, fumamxToken);
				cache.put(putGreeting);
			}
			
		}
		token = fumamxToken.getData().getAccessToken();
		//查詢客戶 http://opengw.fumamx.com/open-platform-server/pure/get_detail_info
		// 列表 http://opengw.fumamx.com/bill-server/pure/get_page_list
		String listUrl = "https://opengw.fumamx.com/bill-server/pure/get_page_list";
		String detailUrl = "http://opengw.fumamx.com/open-platform-server/pure/get_detail_info";
		httpPost = new HttpPost(listUrl);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("accessToken", fumamxToken);
		//responses = httpClient.execute(httpPost,new BasicHttpClientResponseHandler());
		
		Map<String, Integer>map = new HashMap<>();
		Map<String, Object>params = new HashMap<>();
		map.put("from", 1);
		map.put("size", 100);
		params.put("page", map);
		params.put("moduleCode", "NewBF001");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("accessToken",token);//"b6065984d2de4a1fa47e9a13651dfc0f"
		RestTemplate template = new RestTemplate();
		HttpEntity<Object>httpEntity = new HttpEntity<>(params, headers); 
		Object ret = template.postForObject(listUrl,httpEntity, Object.class); 
		
		this.success.setItem("responses", ret);
		this.success.setItem("token", fumamxToken);
	}

	public void doGetSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	public void doSaveSalesPayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String _edit_id = request.getParameter("edit_id");
		int edit_id = 0;
		if (_edit_id != null && !_edit_id.equals("") && !_edit_id.equals("undefined") && !_edit_id.equals("null")) {
			edit_id = EncryptUtiliy.instance().intIDDecrypt(_edit_id);
		}
		// TODO Auto-generated method stub
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
