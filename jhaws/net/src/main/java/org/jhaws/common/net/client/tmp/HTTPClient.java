package org.jhaws.common.net.client.tmp;

import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.client.utils.URIUtils.resolve;
import static org.apache.http.util.EntityUtils.consumeQuietly;
import static org.apache.http.util.EntityUtils.toByteArray;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.security.SecureMe;
import org.jhaws.common.io.security.Security;
import org.jhaws.common.net.client.HTTPClientDefaults;
import org.jhaws.common.net.client.forms.FileInput;
import org.jhaws.common.net.client.forms.Form;

/**
 * @see https://hc.apache.org/
 * @see https://hc.apache.org/httpcomponents-client-ga/tutorial/pdf/httpclient-tutorial.pdf
 */
public class HTTPClient {
    public static class Response implements Serializable {
        private static final long serialVersionUID = 1806430557697629499L;

        private int statusCode;

        private byte[] content;

        private URI uri;

        private final Map<String, List<Object>> headers = new HashMap<>();

        private Locale locale;

        private long contentLength;

        private String contentEncoding;

        private String contentType;

        protected void addHeader(String key, Object value) {
            List<Object> list = headers.get(key);
            if (list == null) {
                list = new ArrayList<>();
                headers.put(key, list);
            }
            list.add(value);
        }

        public Map<String, List<Object>> getHeaders() {
            return Collections.unmodifiableMap(headers);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public byte[] getContent() {
            return content;
        }

        public String getContentString() {
            return content == null ? null : new String(content);
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public List<Form> getForms() {
            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode node;
            try {
                node = cleaner.clean(new ByteArrayInputStream(getContent()));
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            List<? extends TagNode> formlist = node.getElementListByName("form", true);
            List<Form> forms = new ArrayList<Form>();
            for (TagNode formnode : formlist) {
                forms.add(new Form(uri, formnode));
            }
            return forms;
        }
        
        public Form getForm(String id) {
        	return getForms().stream().filter(f -> f.getId().equals(id)).findFirst().get();
        }

        @Override
        public String toString() {
            return statusCode + "," + (content != null) + "," + headers;
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        public long getContentLength() {
            return contentLength;
        }

        public void setContentLength(long contentLength) {
            this.contentLength = contentLength;
        }

        public String getContentEncoding() {
            return contentEncoding;
        }

        public void setContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }

    public static class Params implements Serializable {
        private static final long serialVersionUID = -8834915649537196310L;

        protected URI uri;

        public Params() {
            super();
        }

        public Params(URI uri) {
            setUri(uri);
        }

        public Params(String uri) {
            setUri(uri);
        }

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }

        public void setUri(String uri) {
            this.uri = URI.create(uri);
        }
    }

    public static class GetParams extends Params {
        private static final long serialVersionUID = 4305650301682256528L;

        protected Map<String, List<String>> formValues = new HashMap<String, List<String>>();

        public GetParams() {
            super();
        }

        public GetParams(URI uri) {
            setUri(uri);
        }

        public GetParams(String uri) {
            setUri(uri);
        }

        public Map<String, List<String>> getFormValues() {
            return formValues;
        }

        public void setFormValues(Map<String, List<String>> formValues) {
            this.formValues = formValues;
        }

        public void addFormValue(String key, String value) {
            List<String> list = formValues.get(key);
            if (list == null) {
                list = new ArrayList<>();
                formValues.put(key, list);
            }
            list.add(value);
        }
    }

    public static class DeleteParams extends GetParams {
        private static final long serialVersionUID = 7789744009919283043L;

        public DeleteParams() {
            super();
        }

        public DeleteParams(URI uri) {
            super(uri);
        }

        public DeleteParams(String uri) {
            super(uri);
        }
    }

    public static class HeadParams extends GetParams {
        private static final long serialVersionUID = -6947796698944347163L;

        public HeadParams() {
            super();
        }

        public HeadParams(URI uri) {
            super(uri);
        }

        public HeadParams(String uri) {
            super(uri);
        }
    }

    public static class OptionsParams extends GetParams {
        private static final long serialVersionUID = -3335823798615631641L;

        public OptionsParams() {
            super();
        }

        public OptionsParams(URI uri) {
            super(uri);
        }

        public OptionsParams(String uri) {
            super(uri);
        }
    }

    public static class TraceParams extends GetParams {
        private static final long serialVersionUID = -113043755384527216L;

        public TraceParams() {
            super();
        }

        public TraceParams(URI uri) {
            super(uri);
        }

        public TraceParams(String uri) {
            super(uri);
        }
    }

    public static class PutParams extends GetParams {
        private static final long serialVersionUID = -6103040334975043729L;

        protected String body;

        protected String mime;

        public PutParams() {
            super();
        }

        public PutParams(URI uri) {
            super(uri);
        }

        public PutParams(String uri) {
            super(uri);
        }

        public PutParams(String uri, String body, String mime) {
            super(uri);
            this.body = body;
            this.mime = mime;
        }

        public PutParams(URI uri, String body, String mime) {
            super(uri);
            this.body = body;
            this.mime = mime;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getMime() {
            return mime;
        }

        public void setMime(String mime) {
            this.mime = mime;
        }
    }

    public static class PostParams extends PutParams {
        private static final long serialVersionUID = -3939699621850878105L;

        private HashMap<String, Path> attachments = new HashMap<String, Path>();
        
        private String name;

		public PostParams() {
            super();
        }

        public PostParams(URI uri) {
            super(uri);
        }

        public PostParams(String uri) {
            super(uri);
        }

        public PostParams(String uri, String body, String mime) {
            super(uri, body, mime);
        }

        public PostParams(URI uri, String body, String mime) {
            super(uri, body, mime);
        }

        public HashMap<String, Path> getAttachments() {
            return attachments;
        }

        public void setAttachments(HashMap<String, Path> attachments) {
            this.attachments = attachments;
        }
        
        public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
    }

    protected String charSet = HTTPClientDefaults.CHARSET;

    protected transient CloseableHttpClient httpClient;

    protected String userAgent = HTTPClientDefaults.CHROME;

    protected String user;

    protected transient byte[] pass;

    protected transient Security security;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    protected CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create()//
                    .setUserAgent(getUserAgent())//
                    .setRedirectStrategy(getRedirectStrategy())//
                    .setDefaultRequestConfig(//
                            RequestConfig.custom()//
                                    .setContentCompressionEnabled(true)//
                                    .setMaxRedirects(5)//
                                    .setCircularRedirectsAllowed(false)//
                                    .setConnectionRequestTimeout(HTTPClientDefaults.TIMEOUT)//
                                    .setConnectTimeout(HTTPClientDefaults.TIMEOUT)//
                                    .setExpectContinueEnabled(HTTPClientDefaults.EXPECT_CONTINUE)//
                                    .setRedirectsEnabled(true)//
                                    .setCookieSpec(HTTPClientDefaults.BROWSER_COMPATIBILITY)//
                                    .build())//
                    .build();//
        }
        return httpClient;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return new LaxRedirectStrategy() {
            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                HttpUriRequest redirect = super.getRedirect(request, response, context);
                // HTTPClient.this.chain.add(redirect.getURI());
                // HTTPClient.this.domain = redirect.getURI().getHost();
                System.out.println("redirect: " + redirect.getURI());
                return redirect;
            }

            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                if (response == null) {
                    throw new IllegalArgumentException("HTTP response may not be null");
                }

                int statusCode = response.getStatusLine().getStatusCode();
                String method = request.getRequestLine().getMethod();
                Header locationHeader = response.getFirstHeader("location");

                switch (statusCode) {
                    case HttpStatus.SC_MOVED_TEMPORARILY:
                        return (method.equalsIgnoreCase(HttpGet.METHOD_NAME) || method.equalsIgnoreCase(HttpPost.METHOD_NAME)
                                || method.equalsIgnoreCase(HttpHead.METHOD_NAME)) && (locationHeader != null);
                    case HttpStatus.SC_MOVED_PERMANENTLY:
                    case HttpStatus.SC_TEMPORARY_REDIRECT:
                        return method.equalsIgnoreCase(HttpGet.METHOD_NAME) || method.equalsIgnoreCase(HttpPost.METHOD_NAME)
                                || method.equalsIgnoreCase(HttpHead.METHOD_NAME);
                    case HttpStatus.SC_SEE_OTHER:
                        return true;
                    default:
                        return false;
                } // end of switch
            }
        };
    }

    protected URI toFullUri(GetParams get) {
        URIBuilder uriBuilder = new URIBuilder(get.getUri());
        stream(get.formValues.entrySet().spliterator(), false).forEach(e -> e.getValue().forEach(i -> uriBuilder.addParameter(e.getKey(), i)));
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e1) {
            throw new RuntimeException(e1);
        }
    }

    @SuppressWarnings("deprecation")
    protected Response execute(HttpRequestBase req) {
        req.getParams().setParameter(HTTPClientDefaults.PARAM_SINGLE_COOKIE_HEADER, HTTPClientDefaults.SINGLE_COOKIE_HEADER);

        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(new SystemDefaultCredentialsProvider());
        Response response = null;
        try (CloseableHttpResponse httpResponse = getHttpClient().execute(req, context)) {
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED && user != null && pass != null) {
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(req.getURI().getHost(), AuthScope.ANY_PORT),
                        new UsernamePasswordCredentials(user, getPass()));
                context.setCredentialsProvider(credsProvider);
            } else {
                response = buildResponse(req, httpResponse);
            }
            consumeQuietly(httpResponse.getEntity());
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
        if (response == null) {
            try (CloseableHttpResponse httpResponse = getHttpClient().execute(req, context)) {
                response = buildResponse(req, httpResponse);
                consumeQuietly(httpResponse.getEntity());
            } catch (IOException ioex) {
                throw new UncheckedIOException(ioex);
            }
        }
        return response;
    }

    protected Response buildResponse(HttpRequestBase req, CloseableHttpResponse httpResponse) throws IOException {
        Response response = new Response();
        response.uri = req.getURI();
        for (Header header : httpResponse.getAllHeaders()) {
            response.addHeader(header.getName(), header.getValue());
        }
        response.statusCode = httpResponse.getStatusLine().getStatusCode();
        response.locale = httpResponse.getLocale();
        switch (httpResponse.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:// 200
                break;
            case HttpStatus.SC_NOT_FOUND: // 404
                break;
            case HttpStatus.SC_UNAUTHORIZED: // 401
                break;
        }
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            response.content = toByteArray(entity);
            response.contentLength = entity.getContentLength();
            response.contentEncoding = entity.getContentEncoding() == null ? null : entity.getContentEncoding().getValue();
            response.contentType = entity.getContentType() == null ? null : entity.getContentType().getValue();
        }
        return response;
    }

    public Response get(GetParams get) {
        HttpGet req = new HttpGet(toFullUri(get));
        return execute(req);
    }

    public Response delete(DeleteParams delete) {
        HttpDelete req = new HttpDelete(toFullUri(delete));
        return execute(req);
    }

    public Response put(PutParams put) {
        HttpPut req = new HttpPut(put.getUri());

        HttpEntity body = null;

        if (put.getBody() != null) {
            body = new StringEntity(put.getBody(), ContentType.create(put.getMime(), charSet));
        }

        if (!put.getFormValues().isEmpty()) {
            if (body != null) {
                throw new IllegalArgumentException("multiple bodies");
            }

            // TODO
        }

        if (body != null)
            req.setEntity(body);

        return execute(req);
    }

    public Response post(PostParams post) {
    	RequestBuilder builder = RequestBuilder.post().setUri(post.getUri());
//    	.map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
//    	.forEach(
//    		builder::addParameter);
		HttpUriRequest req = builder.build();
    	
    	
//        HttpPost req = new HttpPost(post.getUri());
//
//        HttpEntity body = null;
//
//        if (post.getBody() != null) {
//            body = new StringEntity(post.getBody(), ContentType.create(post.getMime(), charSet));
//        }
//
//        if (!post.getFormValues().isEmpty() || !post.getAttachments().isEmpty()) {
//            if (body != null) {
//                throw new IllegalArgumentException("multiple bodies");
//            }
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//
//            FormBodyPartBuilder fbpBuilder = FormBodyPartBuilder.create();
//            fbpBuilder.setName(post.getName());
//            stream(post.getFormValues().entrySet().spliterator(), false).forEach(e -> e.getValue().forEach(i -> fbpBuilder.addField(e.getKey(), i)));
//            builder.addPart(fbpBuilder.build());
//
//            stream(post.getAttachments().entrySet().spliterator(), false).forEach(e -> {
//                try {
//                    builder.addBinaryBody(e.getValue().getFileName().toString(), readAllBytes(e.getValue()));
//                } catch (IOException ioex) {
//                    throw new UncheckedIOException(ioex);
//                }
//            });
//
//            body = builder.build();
//        }
//
//        if (body != null) {
//            req.setEntity(body);
//        }

     //   return execute(req);
		return null;
    }

    public Response head(HeadParams head) {
        HttpHead req = new HttpHead(head.getUri());
        return execute(req);
    }

    public Response options(OptionsParams options) {
        HttpOptions req = new HttpOptions(options.getUri());
        return execute(req);
    }

    public Response trace(TraceParams trace) {
        HttpTrace req = new HttpTrace(trace.getUri());
        return execute(req);
    }

    protected String getPass() {
        try {
            return getSecurity().decrypt(this.pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HTTPClient resetAuthentication() {
        this.user = null;
        this.pass = null;
        this.security = null;
        return this;
    }

    public void setAuthentication(String user, String pass) {
        this.user = user;
        try {
            this.pass = getSecurity().encrypt(pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Security getSecurity() {
        if (security == null) {
            try {
                security = Security.class.cast(Class.forName("org.jhaws.common.io.security.SecureMeBC").newInstance());
            } catch (Exception ignore) {
                security = new SecureMe();
            }
        }
        return security;
    }

    public Response submit(Form form) {
        return HTTPClientDefaults.POST.equalsIgnoreCase(form.getMethod()) ? post(form) : get(form);
    }

    public Response post(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        PostParams post = new PostParams(uri);
        form.getInputElements().forEach(e -> post.addFormValue(e.getName(), e.getValue()));
        form.getInputElements()
                .stream()
                .filter(e -> isNotBlank(e.getName()))
                .filter(e -> isNotBlank(e.getValue()))
                .filter(e -> e instanceof FileInput)
                .map(e -> FileInput.class.cast(e))
                .forEach(e -> post.attachments.put(e.getName(), new FilePath(e.getFile())));
        post.setName(form.getId());
        return post(post);
    }

    public Response get(Form form) {
        URI uri = isBlank(form.getAction()) ? form.getUrl() : resolve(form.getUrl(), form.getAction());
        GetParams get = new GetParams(uri);
        form.getInputElements().forEach(e -> get.addFormValue(e.getName(), e.getValue()));
        return get(get);
    }
}
