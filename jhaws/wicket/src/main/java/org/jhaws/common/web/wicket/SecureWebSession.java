package org.jhaws.common.web.wicket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@SuppressWarnings("serial")
public class SecureWebSession extends AuthenticatedWebSession {
    private HttpSession httpSession;

    @SpringBean(name = "authenticationManger")
    private AuthenticationManager authenticationManager;

    public SecureWebSession(Request request) {
        super(request);
        this.httpSession = ((HttpServletRequest) request.getContainerRequest()).getSession();
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (auth.isAuthenticated()) {
            // the authentication object has to be stored in the
            // SecurityContextHolder and in the HttpSession manually, so that
            // the
            // security context will be accessible in the next request
            SecurityContextHolder.getContext().setAuthentication(auth);
            httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Roles getRoles() {
        throw new UnsupportedOperationException("Not supported yet.");
        // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

}