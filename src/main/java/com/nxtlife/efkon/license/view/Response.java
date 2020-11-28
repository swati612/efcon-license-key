package com.nxtlife.efkon.license.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import com.nxtlife.efkon.license.util.LongObfuscator;

public interface Response {

	default Long mask(final Long number) {
		return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
	}

	public static void setFileResponseHeader(Resource resource, String contentType, HttpServletResponse response)
			throws IOException {
		response.setContentType(contentType);
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		response.addHeader("fileName", resource.getFilename());
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(resource.getFile());
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		out.close();
		if (!resource.getFile().delete()) {
			throw new IOException("Could not delete temporary file after processing: " + resource.getFilename());
		}
	}

	public static void setTemplateResponseHeader(Resource resource, String contentType, HttpServletResponse response)
			throws IOException {
		response.setContentType(contentType);
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		response.addHeader("fileName", resource.getFilename());
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(resource.getFile());
		// copy from in to out
		IOUtils.copy(in, out);
		out.flush();
		in.close();
		out.close();
	}

}
