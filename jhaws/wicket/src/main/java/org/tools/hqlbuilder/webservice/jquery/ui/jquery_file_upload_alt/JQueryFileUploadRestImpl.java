package org.tools.hqlbuilder.webservice.jquery.ui.jquery_file_upload_alt;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @see http://www.mkyong.com/webservices/jax-rs/file-upload-example-in-resteasy/
 * @see http://kodejava.org/how-do-i-get-servlet-request-url-information/
 * @see http://stackoverflow.com/questions/4110146/resteasy-and-returning-to-a-jsp-page-with-a-model/4119495#4119495
 */
@Component
@Controller
public class JQueryFileUploadRestImpl implements JQueryFileUploadRest {
	private FileService fileService;

	/**
	 * @see jquery_file_upload.JQueryFileUploadRest#deleteUpload(java.lang.String)
	 */
	@Override
	public DeleteResult deleteUpload(String name) {
		try {
			fileService.deleteFiles(name);
			DeleteResult result = new DeleteResult();
			result.files.add(Collections.singletonMap("name", true));
			return result;
		} catch (IOException | UncheckedIOException ex) {
			throw new WebApplicationException(String.valueOf(ex), HttpURLConnection.HTTP_INTERNAL_ERROR);
		}
	}

	/**
	 * @see jquery_file_upload.JQueryFileUploadRest#download(java.lang.String)
	 */
	@Override
	public StreamingOutput download(String name) {
		try {
			return new FileDownload(fileService, fileService.getFile(name));
		} catch (IOException | UncheckedIOException ex) {
			throw new WebApplicationException(String.valueOf(ex), HttpURLConnection.HTTP_INTERNAL_ERROR);
		}
	}

	public FileService getFileService() {
		return fileService;
	}

	/**
	 * @see jquery_file_upload.JQueryFileUploadRest#getUploads()
	 */
	@Override
	public UploadResult getUploads() {
		return new UploadResult(fileService.getFiles());
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * @see jquery_file_upload.JQueryFileUploadRest#upload(javax.servlet.http.HttpServletRequest, org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput)
	 */
	@Override
	public UploadResult upload(HttpServletRequest request, MultipartFormDataInput input) {
		try {
			List<FileMeta> files = new ArrayList<>();
			List<InputPart> inputParts = input.getFormDataMap().get("files[]");
			String cp = java.nio.file.Paths.get(request.getRequestURI()).getParent().toString().replace('\\', '/');
			for (InputPart inputPart : inputParts) {
				MultivaluedMap<String, String> header = inputPart.getHeaders();
				try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
					FileMeta file = new FileMeta();
					file.name = JQueryFileUploadRest.getFileName(header);
					file.deleteUrl = cp + JQueryFileUploadRest.PATH_DELETE + "?" + JQueryFileUploadRest.PARAM_NAME + "=" + file.name;
					file.thumbnailUrl = cp + JQueryFileUploadRest.PATH_GET + "?" + JQueryFileUploadRest.PARAM_NAME + "=" + file.name;
					file.url = cp + JQueryFileUploadRest.PATH_GET + "?" + JQueryFileUploadRest.PARAM_NAME + "=" + file.name;
					file.size = fileService.add(file, inputStream);
					files.add(file);
				}
			}
			return new UploadResult(files);
		} catch (IOException | UncheckedIOException ex) {
			throw new WebApplicationException(String.valueOf(ex), HttpURLConnection.HTTP_INTERNAL_ERROR);
		}
	}
}
