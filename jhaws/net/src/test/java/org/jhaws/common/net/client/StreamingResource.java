package org.jhaws.common.net.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

// http://www.mastertheboss.com/jboss-frameworks/resteasy/using-rest-services-to-manage-download-and-upload-of-files
// https://www.mkyong.com/webservices/jax-rs/file-upload-example-in-resteasy/
// https://stackoverflow.com/questions/4875780/resteasy-multipart-data-form-file-upload-on-gae
// https://stackoverflow.com/questions/10808246/resteasy-client-framework-file-upload
// http://www.benchresources.net/resteasy-jax-rs-web-service-for-uploadingdownloading-image-file-java-client/
// http://www.benchresources.net/resteasy-jax-rs-web-service-for-uploadingdownloading-zip-file-java-client/
// https://issues.jboss.org/browse/RESTEASY-874
// https://examples.javacodegeeks.com/enterprise-java/rest/resteasy/resteasy-file-upload-example/
@Component
@Controller
public class StreamingResource implements StreamingResourceI {
    protected Map<String, byte[]> data = new HashMap<>();

    protected Map<String, Long> len = new HashMap<>();

    public StreamingResource() {
        super();
    }

    // Response uploadFile(@MultipartForm POJO form)

    @Override
    public Response uploadForm(HttpServletRequest request, MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("attachment");
        StringBuilder s = new StringBuilder("[");
        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                System.out.println(fileName);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                data.put(fileName, IOUtils.toByteArray(inputStream));
                len.put(fileName, (long) data.get(fileName).length);
                if (s.length() > 1) {
                    s.append(",");
                }
                s.append("\"").append(fileName).append(":").append(len.get(fileName)).append("\"");
            } catch (Exception ex) {
                ex.printStackTrace();
                return Response.status(500).entity("" + ex).build();
            }
        }

        return Response.status(200).entity(s.append("]")).build();
    }

    @Override
    public Response downloadForm(String file) {
        return response(file);
    }

    private Response response(String file) {
        StreamingOutput entity = stream(file);
        ResponseBuilder response = Response.ok(entity, MediaType.APPLICATION_OCTET_STREAM);
        response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"");
        response.header(HttpHeaders.CONTENT_LENGTH, data.get(file).length);
        return response.build();
    }

    private StreamingOutput stream(String file) {
        InputStream in = new BufferedInputStream(new ByteArrayInputStream(data.get(file)));
        StreamingOutput entity = out -> {
            org.apache.commons.io.IOUtils.copy(in, out);
            out.flush();
            out.close();
        };
        return entity;
    }

    @Override
    public Response downloadGet(String file) {
        return response(file);
    }

    @Override
    @Deprecated
    public StreamingOutput downloadStream(javax.servlet.http.HttpServletResponse response, String file) {
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"");
        response.addIntHeader(HttpHeaders.CONTENT_LENGTH, (int) (long) len.get(file));
        return stream(file);
    }

    @Override
    public Response downloadStreamInResponse(String file) {
        return response(file);
    }

    @Override
    public List<String> list() {
        List<String> list = data.keySet().stream().map(fileName -> fileName + ":" + len.get(fileName)).collect(Collectors.toList());
        return list;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst(HttpHeaders.CONTENT_DISPOSITION).split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replace("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    @Override
    public String uploadStream(String fileName, InputStream inputStream) {
        StringBuilder s = new StringBuilder("[");
        try {
            data.put(fileName, IOUtils.toByteArray(inputStream));
            len.put(fileName, (long) data.get(fileName).length);
            if (s.length() > 1) {
                s.append(",");
            }
            s.append("\"").append(fileName).append(":").append(len.get(fileName)).append("\"");
        } catch (RuntimeException | IOException ex) {
            throw new WebApplicationException(ex);
        }
        return s.append("]").toString();
    }
}
