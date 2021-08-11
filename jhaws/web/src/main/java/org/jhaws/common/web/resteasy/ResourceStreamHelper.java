package org.jhaws.common.web.resteasy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.function.Consumer;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

public interface ResourceStreamHelper {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(ResourceStreamHelper.class);

    static UncheckedIOException ioExceptionAsUncheckedException(IOException ex) {
        return new UncheckedIOException(ex);
    }

    static RuntimeException checkedExceptionAsUncheckedException(Exception ex) {
        return new RuntimeException(ex);
    }

    static javax.ws.rs.core.StreamingOutput stream(Consumer<OutputStream> consumer, Consumer<Exception> inCaseOfException) {
        return out -> {
            try {
                consumer.accept(out);
            } catch (RuntimeException ex1) {
                logger.error(ex1.getMessage(), ex1);
                if (inCaseOfException != null) {
                    inCaseOfException.accept(ex1);/* throws WebApplicationException */
                }
            } catch (Exception ex1) {
                logger.error(ex1.getMessage(), ex1);
                if (inCaseOfException != null) {
                    inCaseOfException.accept(ex1);/* throws WebApplicationException */
                }
            }
        };
    }

    static javax.ws.rs.core.StreamingOutput stream(InputStream in, Consumer<Exception> inCaseOfException) {
        return out -> {
            try {
                IOUtils.copy(in, out);
                out.flush();
                out.close();
            } catch (IOException ex1) {
                logger.error(ex1.getMessage(), ex1);
                if (inCaseOfException != null) {
                    inCaseOfException.accept(ex1);/* throws WebApplicationException */
                }
            } catch (RuntimeException ex1) {
                logger.error(ex1.getMessage(), ex1);
                if (inCaseOfException != null) {
                    inCaseOfException.accept(ex1);/* throws WebApplicationException */
                }
            } catch (Exception ex1) {
                logger.error(ex1.getMessage(), ex1);
                if (inCaseOfException != null) {
                    inCaseOfException.accept(ex1);/* throws WebApplicationException */
                }
            }
        };
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, javax.ws.rs.core.MediaType mime,
            Consumer<OutputStream> consumer, Consumer<Exception> inCaseOfException) {
        return response(inline, length, filename, mime.toString(), consumer, inCaseOfException);
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, String mime, Consumer<OutputStream> consumer,
            Consumer<Exception> inCaseOfException) {
        javax.ws.rs.core.StreamingOutput entity = stream(consumer, inCaseOfException);
        return response(inline, length, filename, mime, entity);
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, javax.ws.rs.core.MediaType mime, byte[] in,
            Consumer<Exception> inCaseOfException) {
        return response(inline, length, filename, mime.toString(), new ByteArrayInputStream(in), inCaseOfException);
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, String mime, byte[] in,
            Consumer<Exception> inCaseOfException) {
        return response(inline, length, filename, mime, new ByteArrayInputStream(in), inCaseOfException);
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, javax.ws.rs.core.MediaType mime, InputStream in,
            Consumer<Exception> inCaseOfException) {
        return response(inline, length, filename, mime.toString(), in, inCaseOfException);
    }

    static javax.ws.rs.core.Response response(boolean inline, long length, String filename, String mime, InputStream in,
            Consumer<Exception> inCaseOfException) {
        javax.ws.rs.core.StreamingOutput entity = stream(in, inCaseOfException);
        return response(inline, length, filename, mime, entity);
    }

    static javax.ws.rs.core.Response response(//
            boolean inline // inline = bekijken of downloaden, enkel voor browser belangrijk
            , long length // mag -1 zijn voor onbekend
            , String filename // mag null zijn, dan 'UNKNOWN'
            , String mime // mime type
            , javax.ws.rs.core.StreamingOutput entity // zie twee stream methodes
    ) {
        ResponseBuilder response = Response//
                .ok()// zet 200 status
                .entity(entity)// zet stream als body/output
                .type(mime)//
        ;// zet ook HttpHeaders.CONTENT_TYPE

        // https: // developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition
        response.header(HttpHeaders.CONTENT_DISPOSITION, (//
        inline ? //
                "inline"// inline in browser pagina
                : //
                "attachment"// altijd downloaden
        ) + ";" + (// seperator
        "filename=\"" + (StringUtils.isNotBlank(filename) ? filename : "UNKNOWN") + "\"")// zet filename
        );

        // zorgt er voor dat clients/browsers weten hoe lang de stream is
        if (length > 0) response.header(HttpHeaders.CONTENT_LENGTH, length);

        // https://stackoverflow.com/questions/18337630/what-is-x-content-type-options-nosniff
        response.header("X-Content-Type-Options", "nosniff"); // zorgt er voor dat browser altijd luistert naar content-type

        // browser mag deze lang cachen (eind 2050)
        Date expires = new Date(2050 - 1900, 12 - 1, 31);
        response.expires(expires);

        return response.build();
    }

    /**
     * header sample { Content-Type=[image/png], Content-Disposition=[form-data; name="file"; filename="filename.extension"] }
     **/
    // get uploaded filename, is there a easy way in RESTEasy?
    static String getFileName(MultivaluedMap<String, String> header) {
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
}
