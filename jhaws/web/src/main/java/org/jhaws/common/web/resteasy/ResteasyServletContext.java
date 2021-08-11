package org.jhaws.common.web.resteasy;

//import java.io.IOException;
//
//import javax.inject.Inject;
//import javax.servlet.ServletContext;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.ext.Provider;
//
//import org.jboss.resteasy.annotations.interception.ServerInterceptor;
//import org.jboss.resteasy.spi.ResteasyProviderFactory;
//import org.springframework.stereotype.Component;
//
//// @ConditionalOnWebApplication
//@Component
//@Provider
//@ServerInterceptor
public class ResteasyServletContext {
}
//implements ContainerRequestFilter {
//    @Inject
//    ServletContext sc;
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//        ResteasyProviderFactory.getContextDataMap().put(ServletContext.class, sc);
//    }
//}