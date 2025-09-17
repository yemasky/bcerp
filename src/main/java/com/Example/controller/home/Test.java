package com.Example.controller.home;

import java.lang.reflect.Field;
import java.util.List;

public class Test {
	public static void main(String args[]) {
		//House house = new House();
		//Class<?> houseSupper = house.getClass().getSuperclass();
        //System.out.println(houseSupper.getName());
        //System.out.println(house.getClass().getSuperclass().getName());
        //Class<?> thisClass = house.getClass().getSuperclass();
        //System.out.println(thisClass.getSuperclass().getName());
        //getSimpClass2(house);
        //getSimpClass3(House.class);
    }

	public static <T> List<T> getSimpClass(Object thisClass) {// && !thisClass.getClass().getName().equals("java.lang.Class")
		for (; thisClass != Object.class; thisClass = thisClass.getClass().getSuperclass()) {
			Field[] fields = thisClass.getClass().getDeclaredFields();
			System.out.println(thisClass.getClass().getName());
			for (int i = 0; i < fields.length; i++) {// 寻找该元素对应的字段属性
				Field field = fields[i];
				String fieldName = field.getName().toLowerCase();
				System.out.println(fieldName);
			}
		}
		return null;
	}
	
	public static <T> List<T> getSimpClass2(Object thisClass) {// && !thisClass.getClass().getName().equals("java.lang.Class")
		Class<?> thisClass2 = thisClass.getClass();
		for (; thisClass2 != Object.class; thisClass2 = thisClass2.getSuperclass()) {
			Field[] fields = thisClass2.getDeclaredFields();
			//System.out.println(thisClass2.getName());
			for (int i = 0; i < fields.length; i++) {// 寻找该元素对应的字段属性
				Field field = fields[i];
				String fieldName = field.getName().toLowerCase();
				System.out.println(fieldName);
			}
		}
		return null;
	}
	
	public static <T> List<T> getSimpClass3(Class<T> thisClass) {// && !thisClass.getClass().getName().equals("java.lang.Class")
		Class<?> thisClass2 = thisClass;
		for (; thisClass2 != Object.class; thisClass2 = thisClass2.getSuperclass()) {
			Field[] fields = thisClass2.getDeclaredFields();
			//System.out.println(thisClass2.getName());
			for (int i = 0; i < fields.length; i++) {// 寻找该元素对应的字段属性
				Field field = fields[i];
				String fieldName = field.getName().toLowerCase();
				System.out.println(fieldName);
			}
		}
		return null;
	}
}
