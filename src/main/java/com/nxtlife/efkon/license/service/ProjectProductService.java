package com.nxtlife.efkon.license.service;

import java.util.List;

import org.springframework.core.io.Resource;

import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductGraphResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

public interface ProjectProductService {

	/**
	 * this method used to save product in a particular project.
	 *
	 * @param
	 * @param request
	 * @return {@link ProjectProductResponse} after saving product information in
	 *         project
	 * @throws ValidationException if project or product not exist or project and
	 *                             product ids already exist in project product
	 */
	public ProjectProductResponse save(ProjectProductRequest request);

	/**
	 * this method used to update product detail and license detail in a particular
	 * project.
	 *
	 * @param id
	 * @param request
	 * @return {@link ProjectProductResponse} after updating product information in
	 *         project
	 * @throws ValidationException                            if project or product
	 *                                                        not exist and product
	 *                                                        ids already exist in
	 *                                                        project product
	 * @throws com.nxtlife.efkon.license.ex.NotFoundException if project product id
	 *                                                        is not found
	 */
	public ProjectProductResponse update(Long id, ProjectProductRequest request);

	/**
	 * this method used to fetch all Project products
	 *
	 * @return list of <tt>ProjectProductResponse</tt>
	 */
	public List<ProjectProductResponse> findAll();

	/**
	 * this method is used to generate excel of all the products of all the projects
	 * 
	 * @return
	 */
	public Resource findAllExcel();

	/**
	 * fetch products of the project by project id
	 * 
	 * @param projectId
	 * @return list of {@link ProjectProductResponse}
	 */
	public List<ProjectProductResponse> findByProjectId(Long projectId);

	/**
	 * generate excel file of products of the project by project Id
	 * 
	 * @param productId
	 * @return
	 */
	public Resource findByProjectIdExcel(Long projectId);

	/**
	 * this method used to fetch project product response
	 * 
	 * @param id
	 * @return {@link ProjectProductResponse}
	 */
	public ProjectProductResponse findById(Long id);

	/**
	 * this method used to update status of project product
	 * 
	 * @param id
	 * @param comment
	 * @return {@link ProjectProductResponse}
	 */
	public ProjectProductResponse updateStatus(Long id, ProjectProductStatus status, String comment);

	public ProjectProductResponse renew(Long id, ProjectProductRequest request);

	/**
	 * this method used to delete product in project. It throws exception if project
	 * product id not found
	 *
	 * @param id
	 * @return {@link SuccessResponse} if product deleted successfully
	 * @throws NotFoundException if project product not found
	 */
	public SuccessResponse delete(Long id);

	/**
	 * this method is used to find the count of products by their status
	 * 
	 * @return {@link ProjectProductGraphResponse}
	 */
	public List<ProjectProductGraphResponse> findCountByStatus();

	/**
	 * this method is used to generate pdf of all the products of all the projects
	 * 
	 * @return
	 */
	public Resource findAllPdf();

	/**
	 * generate pdf file of products of the project by project Id
	 * 
	 * @param productId
	 * @return
	 */
	public Resource findByProjectIdPdf(Long projectId);

	public ProjectProductResponse undo(Long id, String comment);

	/**
	 * return customer name with email and its product, license count
	 * 
	 * @return list of {@link ProjectProductGraphResponse}
	 */
	public List<ProjectProductGraphResponse> findCountByApprovedStatusAndGroupByCustomerEmail();

	/**
	 * return project products for customer email
	 * 
	 * @param email
	 * @return list of {@link ProjectProductResponse}
	 */
	public List<ProjectProductResponse> findByApprovedStatusAndCustomerEmail(String email);

	public Resource findExcelByApprovedStatusAndCustomerEmail(String email);

	public Resource findPdfByApprovedStatusAndCustomerEmail(String email);

	public List<ProjectProductResponse> findByStatus(String name);

}
