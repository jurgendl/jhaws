package org.tools.hqlbuilder.webservice.resteasy.resources;

import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.tools.hqlbuilder.webservice.js.GoogleLogin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Controller
public class GoogleLoginResourceImpl implements GoogleLoginResource {
    public String ping() {
        return String.valueOf(System.currentTimeMillis());
    }

    public Response tokensignin(String id_token) {
        if (id_token == null) {
            return Response.serverError().entity("no id token").build();
        }
        String jsonResponse;
        try {
            CloseableHttpClient httpclient = getHttpClient();
            HttpGet httpGet = new HttpGet(GoogleLogin.VALIDATION_URL + id_token);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                HttpEntity entity = response.getEntity();
                jsonResponse = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } catch (Exception ex) {
            return Response.serverError().entity(String.valueOf(ex)).build();
        }
        // http://www.journaldev.com/2324/jackson-json-processing-api-in-java-example-tutorial
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // objectMapper.configure(SerializationConfig.Feature.SORT_PROPERTIES_ALPHABETICALLY, true);
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(jsonResponse, GoogleLoginResponse.class);
            return Response.ok(googleLoginResponse).build();
        } catch (Exception ex) {
            return Response.serverError().entity(String.valueOf(ex)).build();
        }
    }

    protected CloseableHttpClient httpClient;

    protected CloseableHttpClient getHttpClient() {
        return Optional.ofNullable(httpClient).orElseGet(() -> httpClient = HttpClients.createDefault());
    }

    @XmlRootElement
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleLoginResponse {
        private String given_name;

        private String family_name;

        private String locale;

        private String picture;

        private String name;

        private String email;

        private Boolean email_verified;

        public String getGiven_name() {
            return given_name;
        }

        public void setGiven_name(String given_name) {
            this.given_name = given_name;
        }

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(Boolean email_verified) {
            this.email_verified = email_verified;
        }
    }
}
