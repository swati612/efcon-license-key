package com.nxtlife.efkon.license.entity.version;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.license.entity.product.ProductDetail;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.license.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "version")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Version extends BaseEntity implements Serializable {

	@NotEmpty(message = "version can't be null or empty")
	@Column(unique = true)
	private String version;

	@OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
	private Set<ProductDetail> productDetails;

	public Version() {
		super();
	}

	public Version(@NotEmpty(message = "version can't be null or empty") String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Set<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

}
