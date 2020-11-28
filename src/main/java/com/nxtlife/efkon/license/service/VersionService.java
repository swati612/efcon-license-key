package com.nxtlife.efkon.license.service;

import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.version.VersionRequest;
import com.nxtlife.efkon.license.view.version.VersionResponse;

import java.util.List;

public interface VersionService {

    /**
     * this method used to save version details. If version already exist in database
     * then it throws exception otherwise it return saved version details.
     *
     * @param request
     * @return {@Link VersionResponse}
     * @throws ValidationException if version already exist in database
     */
    public VersionResponse save(VersionRequest request);

    /**
     * this method used to get all versions.
     *
     * @return list of <tt>VersionResponse</tt>
     */
    public List<VersionResponse> findAll();

    /**
     * this method used to update version details. It throws exception if version id
     * isn't correct .
     *
     * @param id
     * @param request
     * @return {@link VersionResponse} after updating version information
     * @throws NotFoundException   if version id isn't correct
     * @throws ValidationException if updated version name is
     *                             already exist in database
     */

    public VersionResponse update(Long id, VersionRequest request);

    /**
     * this method used to delete version. It throws exception if version id not found
     *
     * @param id
     * @return {@link SuccessResponse} if version deleted successfully
     * @throws NotFoundException if version not found
     */
    public SuccessResponse delete(Long id);
}
