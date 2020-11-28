package com.nxtlife.efkon.license.view.version;

import com.nxtlife.efkon.license.entity.version.Version;
import com.nxtlife.efkon.license.view.Request;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

public class VersionRequest implements Request {


    @Schema(description = "version of the product code", example = "2.2")
    @NotEmpty(message = "version can't be empty")
    private String version;


    public Version toEntity() {
        Version version = new Version();
        version.setVersion(this.getVersion());
        return version;
    }

    public String getVersion() {
        return version;
    }


}
