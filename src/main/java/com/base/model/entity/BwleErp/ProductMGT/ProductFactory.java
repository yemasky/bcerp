package com.base.model.entity.BwleErp.ProductMGT;

import core.custom_interface.Table;

@Table(name = "product_factory", isAnnotationField = true)
public class ProductFactory {
    private Integer product_id;
    private Integer dict_id;
    private Integer factory_dict_id;
    private String factory_name;
    private String factory_num;
    private String engine_num;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public Integer getDict_id() {
		return dict_id;
	}
	public void setDict_id(Integer dict_id) {
		this.dict_id = dict_id;
	}
	public Integer getFactory_dict_id() {
		return factory_dict_id;
	}
	public void setFactory_dict_id(Integer factory_dict_id) {
		this.factory_dict_id = factory_dict_id;
	}
	public String getFactory_name() {
		return factory_name;
	}
	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}
	public String getFactory_num() {
		return factory_num;
	}
	public void setFactory_num(String factory_num) {
		this.factory_num = factory_num;
	}
	public String getEngine_num() {
		return engine_num;
	}
	public void setEngine_num(String engine_num) {
		this.engine_num = engine_num;
	}
	
}
