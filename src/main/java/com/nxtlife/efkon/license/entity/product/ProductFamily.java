package com.nxtlife.efkon.license.entity.product;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "product_family")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class ProductFamily extends BaseEntity implements Serializable {

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "code can't be null")
	private String code;

	private String description;

	@OneToMany(mappedBy = "productFamily", cascade = CascadeType.ALL)
	private Set<ProductCode> productCodes;

	@OneToMany(mappedBy = "productFamily", cascade = CascadeType.ALL)
	private Set<ProductDetail> productDetails;

	public ProductFamily() {
		super();
	}

	public ProductFamily(@NotNull(message = "name can't be null") String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ProductCode> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(Set<ProductCode> productCodes) {
		this.productCodes = productCodes;
	}

	public Set<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Set<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

}
