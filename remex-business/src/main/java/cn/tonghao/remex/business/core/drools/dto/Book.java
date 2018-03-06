package cn.tonghao.remex.business.core.drools.dto;

import java.io.Serializable;
import java.util.Set;

public class Book implements Serializable {
	private String name;

	// 书的分类
	private String clz;

	// 销售区域
	private String salesArea;

	// 出版日期距今时间（单位：Y）
	private int years;

	// 报价
	private double basePrice;

	// 对外售价
	private double salesPrice;

	private String discount;

	//进货商
	private Set<String> supplier;

	//可选属性
	private Set<String> routeFactors;

	public Book() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClz() {
		return clz;
	}

	public void setClz(String clz) {
		this.clz = clz;
	}

	public String getSalesArea() {
		return salesArea;
	}

	public void setSalesArea(String salesArea) {
		this.salesArea = salesArea;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Set<String> getSupplier() {
		return supplier;
	}

	public void setSupplier(Set<String> supplier) {
		this.supplier = supplier;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Set<String> getRouteFactors() {
		return routeFactors;
	}

	public void setRouteFactors(Set<String> routeFactors) {
		this.routeFactors = routeFactors;
	}

}