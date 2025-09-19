package com.base.util;

import java.util.HashMap;
import java.util.List;

public class Utility {
	private static Utility utility = null;

	public static Utility instance() {
		if (utility == null)
			utility = new Utility();
		return utility;
	}

	public int[] StringToIntArray(String[] arrList) {// 查询用字符串数组转int数组
		int len = arrList.length;
		int[] array = new int[len];
		for (int i = 0; i < len; i++) {
			if (arrList[i] == null || arrList[i].equals(""))
				continue;
			array[i] = Integer.parseInt(arrList[i]);
		}
		return array;
	}
	
	public int[] hashMapListToIntArray(List<HashMap<String, Object>> moduleList, String key) {// 查询用字符串数组转int数组
		int size = moduleList.size();
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			Object val = moduleList.get(i).get(key);
			if (val == null || val.equals(""))
				continue;
			array[i] = Integer.parseInt(val.toString());
		}
		return array;
	}
}
