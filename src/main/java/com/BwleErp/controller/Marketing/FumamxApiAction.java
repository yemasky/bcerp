package com.BwleErp.controller.Marketing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.BwleErp.config.Config;
import com.base.model.entity.BwleErp.Marketing.FumamxApi;
import com.base.model.vo.BwleErp.Marketing.FumamxToken;
import com.base.service.GeneralService;
import com.google.gson.Gson;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class FumamxApiAction implements Runnable {
	private GeneralService generalService;

	protected FumamxApiAction(GeneralService generalService) {
		this.generalService = generalService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			FumamxToken fumamxToken = this.getToken();
			this.getCustomerList(fumamxToken, 5, 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FumamxToken getToken() throws Exception {
		String url = "https://opengw.fumamx.com/auth-server/open/acquire_token";
		String appid = "I4UfdbMi";
		String appsecret = "40270f10408f94db496ed96c088ce84d1fc77b98";
		String tokenCode = "fumamxTonkeCode";
		CacheManager cacheManager = CacheManager.create();// .getCacheManager(Config.cache24hour);
		Cache cache = cacheManager.getCache(Config.cache120min);// 120分鐘 即2小時
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost;
		String responses = "";
		FumamxToken fumamxToken = new FumamxToken();
		if (cache != null) {
			Element getGreeting = cache.get(tokenCode);
			if (getGreeting != null) {// 取出緩存
				Object obj = getGreeting.getObjectValue();
				if (obj instanceof FumamxToken) {
					fumamxToken = (FumamxToken) obj;
				}
				if(fumamxToken.getData() != null && fumamxToken.getData().getAccessToken() != null) return fumamxToken;
			} 
			httpPost = new HttpPost(url);
			JSONObject json = new JSONObject();
			json.put("appId", appid);
			json.put("appSecret", appsecret);
			httpPost.addHeader("Content-Type", "application/json");
			httpPost.setEntity(new StringEntity(json.toString()));
			//httpPost.setEntity(new StringEntity("{'appId':'"+appid+"','':'"+appsecret+"'}"));
			responses = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
			fumamxToken = new Gson().fromJson(responses, FumamxToken.class);
			// 新緩存
			Element putGreeting = new Element(tokenCode, fumamxToken);
			cache.put(putGreeting);
		}
		// cache.remove("key1");
		return fumamxToken;
	}

	private void getCustomerList(FumamxToken fumamxToken, int from, int page) {
		String listUrl = "https://opengw.fumamx.com/bill-server/pure/get_page_list";
		String detailUrl = "http://opengw.fumamx.com/open-platform-server/pure/get_detail_info";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost;
		httpPost = new HttpPost(listUrl);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("accessToken", fumamxToken.getData().getAccessToken());
		httpPost.setEntity(new StringEntity("{'moduleCode':'NewBF001','page':{'from':'"+from+"','size':'"+page+"'}}"));
		String responses = "";
		try {
			//responses = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
			HttpClientResponseHandler<String> responseHandler = new HttpResponseHandler();
			responses = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Integer> map = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		map.put("from", 1);
		map.put("size", 1);
		params.put("page", map);
		params.put("moduleCode", "NewBF001");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("accessToken", fumamxToken.getData().getAccessToken());// "b6065984d2de4a1fa47e9a13651dfc0f"
		RestTemplate template = new RestTemplate();
		HttpEntity<Object> httpEntity = new HttpEntity<>(params, headers);
		//Object ret = template.postForObject(listUrl,httpEntity, Object.class);
		//JSONObject json = new JSONObject(ret);
		JSONObject json = new JSONObject(responses);
		//System.out.println("=-----------===>"+ret);
		//System.out.println("=-----------===>"+responses);
		JSONObject list = (JSONObject) json.get("data");
		JSONArray customerList = list.getJSONArray("list");
		List<FumamxApi> fumamxApiList = new ArrayList<>();
		customerList.forEach(customer ->{
			JSONObject jsonObject = (JSONObject) customer;
			Long key_id = jsonObject.getLong("key_id");
			FumamxApi fumamxApi = new FumamxApi();
			fumamxApi.setFumamx_type(1);
			fumamxApi.setKey_id(key_id);
			fumamxApi.setVal(jsonObject.toString());
			fumamxApiList.add(fumamxApi);
		});
		try {
			this.generalService.batchSave(fumamxApiList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int totalNum = list.getInt("totalNum");
		if(from*page >= totalNum) return;
		from = from + 1;
		try {
			Thread.sleep(5000);//休息5秒
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getCustomerList(fumamxToken, from, page);
		//System.out.println("------>"+customerList.toString());

	}

	private void getCustomerDetail(FumamxToken fumamxToken) {
		String detailUrl = "https://opengw.fumamx.com/open-platform-server/pure/get_detail_info";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost;
		httpPost = new HttpPost(detailUrl);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("accessToken", fumamxToken.getData().getAccessToken());
		httpPost.setEntity(new StringEntity("{'moduleCode':'NewBF001','billCode':'Bulgaria-NIK','structId':'1'}"));
		String responses = "";
		try {
			//responses = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
			HttpClientResponseHandler<String> responseHandler = new HttpResponseHandler();
			responses = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Integer> map = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		map.put("from", 1);
		map.put("size", 1);
		params.put("page", map);
		params.put("moduleCode", "NewBF001");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("accessToken", fumamxToken.getData().getAccessToken());// "b6065984d2de4a1fa47e9a13651dfc0f"
		RestTemplate template = new RestTemplate();
		HttpEntity<Object> httpEntity = new HttpEntity<>(params, headers);
		//Object ret = template.postForObject(listUrl,httpEntity, Object.class);
		//JSONObject json = new JSONObject(ret);
		JSONObject json = new JSONObject(responses);
		//System.out.println("=-----------===>"+ret);
		//System.out.println("=-----------===>"+responses);
		JSONObject customer = (JSONObject) json.get("data");
		
		try {
			Thread.sleep(5000);//休息5秒
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("------>"+customerList.toString());

	}

}
 
class HttpResponseHandler implements HttpClientResponseHandler<String> {
	@Override
	public String handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
		// TODO Auto-generated method stub
		int status = response.getCode();
		if (status >= 200 && status < 300) {
			org.apache.hc.core5.http.HttpEntity entity = response.getEntity();
			if (entity == null) {
				return "";
			} else {
				return EntityUtils.toString(entity);
			}
		} else {
			return "" + status;
		}
	}
}
