package org.tools.hqlbuilder.webservice.resteasy.providers;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jhaws.common.lang.DeEnCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This interceptor verify the access permissions for a user based on username and passowrd provided in request
 * */
@Provider
public class SecurityInterceptor implements javax.ws.rs.container.ContainerRequestFilter {
    protected static final Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    protected static final String AUTHORIZATION_PROPERTY = "Authorization";

    protected static final String AUTHENTICATION_SCHEME = "Basic";

    protected static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());;

    protected static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());;

    protected static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());;

    /**
     * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) requestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();
        logger.debug("checking security: " + method);
        // Access allowed for all
        if (!method.isAnnotationPresent(PermitAll.class)) {
            // Access denied for all
            if (method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }

            // Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();

            // Fetch authorization header
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);

            // If no authorization information present; block access
            // if (authorization == null || authorization.isEmpty()) {
            // requestContext.abortWith(ACCESS_DENIED);
            // return;
            // }

            String username = null;
            String password = null;
            if (authorization != null) {
                // Get encoded username and password
                final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

                // Decode username and password
                String usernameAndPassword = null;
                try {
                    usernameAndPassword = DeEnCoding.base64DecodeToString(encodedUserPassword);
                } catch (Exception e) {
                    requestContext.abortWith(SERVER_ERROR);
                    return;
                }

                // Split username and password tokens
                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                username = tokenizer.nextToken();
                password = tokenizer.nextToken();
            }

            // Verifying Username and password
            // ...

            // Verify user access
            if (method.isAnnotationPresent(RolesAllowed.class)) {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

                // Is user valid?
                if (!isUserAllowed(username, password, rolesSet)) {
                    requestContext.abortWith(ACCESS_DENIED);
                    return;
                }
            }
        }
    }

    
    protected boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;

        // Step 1. Fetch password from database and match with password in argument
        // If both match then get the defined role for user from database and continue; else return isAllowed [false]
        // Access the database and do this part yourself
        // String userRole = userMgr.getUserRole(username);
        String userRole = "ADMIN";

        // Step 2. Verify user role
        if (rolesSet.contains(userRole)) {
            isAllowed = true;
        }
        return isAllowed;
    }
}