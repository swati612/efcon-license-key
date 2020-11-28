package com.nxtlife.efkon.license.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.efkon.license.entity.project.product.ProjectProductRequestComment;
import com.nxtlife.efkon.license.view.project.product.ProjectProductCommentResponse;

public interface ProjectProductRequestCommentJpaDao extends JpaRepository<ProjectProductRequestComment, Long> {

	List<ProjectProductCommentResponse> findByProjectProductLicenseRequestId(Long unmaskId);

}
