package org.jhaws.common.net.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
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
        System.out.println(new Date());
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("attachment");
        List<String> fileNames = new ArrayList<>();
        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);
                System.out.println(fileName);
                fileNames.add(fileName);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                len.put(fileName, (long) inputStream.available());
                data.put(fileName, IOUtils.readFully(inputStream, inputStream.available()));
            } catch (Exception ex) {
                ex.printStackTrace();
                return Response.status(500).entity("" + ex).build();
            }
        }
        System.out.println(new Date());
        return Response.status(200).entity("Uploaded files : " + fileNames).build();
    }

    @Override
    public Response downloadForm(String file) {
        System.out.println(new Date());
        Response dl = dl(file);
        System.out.println(new Date());
        return dl;
    }

    private Response dl(String file) {
        System.out.println(new Date());
        try {
            ResponseBuilder response = Response.ok(new ByteArrayInputStream(data.get(file)));
            response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file);
            response.header(HttpHeaders.CONTENT_LENGTH, len.get(file));
            return response.build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(500).entity("" + ex).build();
        } finally {
            System.out.println(new Date());
        }
    }

    @Override
    public Response downloadGet(String file) {
        System.out.println(new Date());
        Response dl = dl(file);
        System.out.println(new Date());
        return dl;
    }

    @Override
    public StreamingOutput downloadStream(HttpServletResponse response, String file) {
        System.out.println(new Date());
        try {
            response.addIntHeader(HttpHeaders.CONTENT_LENGTH, (int) (long) len.get(file));
            InputStream in = new ByteArrayInputStream(data.get(file));
            return new StreamingOutput() {
                @Override
                public void write(OutputStream out) throws IOException, WebApplicationException {
                    StreamingResource.this.write(in, out);
                }
            };
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex);
        } finally {
            System.out.println(new Date());
        }
    }

    @Override
    public Response downloadStreamInResponse(String file) {
        System.out.println(new Date());
        try {
            InputStream in = new ByteArrayInputStream(data.get(file));
            ResponseBuilder response = Response.ok().entity(new StreamingOutput() {
                @Override
                public void write(OutputStream out) throws IOException, WebApplicationException {
                    StreamingResource.this.write(in, out);
                }
            });
            response.header(HttpHeaders.CONTENT_LENGTH, len.get(file));
            return response.build();
        } catch (RuntimeException ex) {
            throw new WebApplicationException(ex);
        } finally {
            System.out.println(new Date());
        }
    }

    private void write(InputStream in, OutputStream out) throws WebApplicationException {
        try {
            // CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE);
            // class1.putDataOnOutputStream(cbb.getOutputStream());
            // class2.processDataFromInputStream(cbb.getInputStream());
            org.apache.commons.io.IOUtils.copy(in, out);
            out.flush();
        } catch (RuntimeException | IOException ex) {
            throw new WebApplicationException(ex);
        }
    }

    @Override
    public List<String> list() {
        System.out.println(new Date());
        List<String> list = data.keySet().stream().map(fileName -> fileName + ":" + len.get(fileName)).collect(Collectors.toList());
        System.out.println(new Date());
        return list;
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst(HttpHeaders.CONTENT_DISPOSITION).split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    @Override
    public String uploadStream(String fileName, InputStream in) {
        System.out.println(new Date());
        try {
            len.put(fileName, (long) in.available());
            data.put(fileName, IOUtils.readFully(in, in.available()));
        } catch (RuntimeException | IOException ex) {
            throw new WebApplicationException(ex);
        }
        System.out.println(new Date());
        return null;
    }
}
