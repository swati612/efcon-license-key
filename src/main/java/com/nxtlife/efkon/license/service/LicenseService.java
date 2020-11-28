package com.nxtlife.efkon.license.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.efkon.license.view.license.LicenseRequest;
import com.nxtlife.efkon.license.view.license.LicenseResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductGraphResponse;

public interface LicenseService {

	// public LicenseResponse update(Long id, LicenseRequest request);

	/**
	 * this method is used to find the single license of a product
	 * 
	 * @param licenseId
	 * @return {@link LicenseResponse}
	 */
	public LicenseResponse findById(Long licenseId);

	/**
	 * this method is used to fetch all the license details
	 * 
	 * @return list of <tt>LicenseResponse</tt>
	 */

	public List<LicenseResponse> findAll();

	/**
	 * this method is used to create excel of all the license
	 * 
	 * @return
	 */
	public Resource findAllExcel();

	/**
	 * this method is used to generate license key of product in a project
	 * 
	 * @param id
	 * @param request
	 * @return {@link LicenseResponse}}
	 * @throws Exception
	 */

	public LicenseResponse generateKey(Long id, LicenseRequest request);

	/**
	 * this method is used to replace the generated license key
	 * 
	 * @param id
	 * @return {@link LicenseResponse}}
	 */
	public LicenseResponse replaceGenerateKey(Long id, LicenseRequest request);

	/**
	 * this method is used to find all the licenses of particular product in a
	 * project
	 * 
	 * @param projectId
	 * @param productId
	 * @return {@link LicenseResponse}
	 */
	public List<LicenseResponse> findByProjectIdAndProductId(Long projectId, Long productId);

	/**
	 * this method is used to find all the licenses of particular project
	 * 
	 * @param projectId
	 * @return {@link LicenseResponse}
	 */
	public List<LicenseResponse> findByProjectId(Long projectId);

	/**
	 * this method is used to generate the excel file containing licenses of
	 * particular product of particular project
	 * 
	 * @param projectId
	 * @param productId
	 * @return
	 */
	public Resource findLicensesByProjectIdAndProductIdExcel(Long projectId, Long productId);

	/**
	 * this method is used to generate the excel file containing licenses of all the
	 * products of particular project
	 * 
	 * @param projectId
	 * @return {@link Resource}
	 */
	public Resource findLicensesByProjectIdExcel(Long projectId);

	/**
	 * this method is used to find total active and expired licenses
	 * 
	 * @return {@link ProjectProductGraphResponse}
	 */
	public List<ProjectProductGraphResponse> findTotalActiveAndExpiredLicenses();

	/**
	 * this method is used to generate the pdf file containing licenses of all the
	 * products of particular project
	 * 
	 * @param projectId
	 * @return {@link Resource}
	 */
	public Resource findLicensesByProjectIdPdf(Long projectId);

	/**
	 * this method is used to generate the pdf file containing licenses of
	 * particular product of particular project
	 * 
	 * @param projectId
	 * @param productId
	 * @return {@link Resource}
	 */
	public Resource findLicensesByProjectIdAndProductIdPdf(Long projectId, Long productId);

	/**
	 * this method is used to create pdf of all the license
	 * 
	 * @return {@link Resource}
	 */
	public Resource findAllPdf();

	/**
	 * this method is used to generate license key by importing excel or uploading
	 * excel file
	 * 
	 * @param file
	 * @param projectProductId
	 * @return
	 */
	public List<LicenseResponse> generateLicenseKeyFromExcel(MultipartFile file, Long projectProductId);

	public List<LicenseResponse> licenseReportByEmail(String email);

	public Resource findLicensesReportExcelByEmail(String email);

	public Resource findLicensesReportPdfByEmail(String email);

	public List<LicenseResponse> findGeneratedLicenses(Long projectProductId);

}
