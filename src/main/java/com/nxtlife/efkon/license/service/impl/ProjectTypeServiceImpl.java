package com.nxtlife.efkon.license.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectTypeJpaDao;
import com.nxtlife.efkon.license.entity.project.ProjectType;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProjectTypeService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

@Service("projectTypeServiceImpl")
@Transactional
public class ProjectTypeServiceImpl extends BaseService implements ProjectTypeService {

	private static Logger logger = LoggerFactory.getLogger(ProjectTypeResponse.class);

	@Autowired
	private ProjectTypeJpaDao projectTypeDao;

	@Autowired
	private ProjectJpaDao projectJpaDao;

	@PostConstruct
	private void init() {
		List<ProjectType> projectTypes = new ArrayList<>();
		if (!projectTypeDao.existsByName("Traffic Control")) {
			projectTypes.add(new ProjectType("Traffic Control"));
		}
		if (!projectTypeDao.existsByName("Swach Bharat")) {
			projectTypes.add(new ProjectType("Swach Bharat"));
		}
		projectTypeDao.saveAll(projectTypes);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_TYPE_CREATE)
	public ProjectTypeResponse save(String projectType) {
		if (projectTypeDao.existsByNameAndActive(projectType, true)) {
			throw new ValidationException("This project type already exist");
		}
		ProjectType projectTypeResponse = projectTypeDao.save(new ProjectType(projectType));
		return new ProjectTypeResponse(projectTypeResponse);
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_TYPE_FETCH)
	public List<ProjectTypeResponse> findAll() {
		return projectTypeDao.findAll().stream().map(ProjectTypeResponse::new).collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_TYPE_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!projectTypeDao.existsById(unmaskId)) {
			throw new NotFoundException("This project type not found");
		}
		if (projectJpaDao.existsByProjectTypeId(unmaskId)) {
			throw new ValidationException("This project type in use for projects. So you can't delete it");
		}
		projectTypeDao.deleteById(unmaskId, getUserId(), new Date());
		return new SuccessResponse(HttpStatus.OK.value(), "Successfully deleted");
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_TYPE_DELETE)
	public ProjectTypeResponse reactivate(Long id) {
		Long unmaskId = unmask(id);
		if (!projectTypeDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("project type (%s) not found", id));
		}
		int rows = projectTypeDao.reactivate(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("project type {} successfuly reactivated", unmaskId);
		}
		ProjectTypeResponse response = projectTypeDao.findResponseById(unmaskId);
		return response;
	}
}
