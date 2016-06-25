package org.jhaws.common.web.resteasy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Pretty
@Component
@Path("/api")
@Controller
@GZIP
public class RestEasyApiResource implements RestResource {
	@XmlRootElement
	public static final class MethodDescription {
		@XmlAttribute
		private String method;

		@XmlAttribute
		private String fullPath;

		@XmlAttribute
		private String produces;

		@XmlAttribute
		private String consumes;

		public MethodDescription() {
			super();
		}

		public MethodDescription(String method, String fullPath, String produces, String consumes) {
			this.method = method;
			this.fullPath = fullPath;
			this.produces = produces;
			this.consumes = consumes;
		}

		public String getMethod() {
			return this.method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getFullPath() {
			return this.fullPath;
		}

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}

		public String getProduces() {
			return this.produces;
		}

		public void setProduces(String produces) {
			this.produces = produces;
		}

		public String getConsumes() {
			return this.consumes;
		}

		public void setConsumes(String consumes) {
			this.consumes = consumes;
		}
	}

	@XmlRootElement
	public static final class ResourceDescription {
		public static List<ResourceDescription> fromBoundResourceInvokers(ServletContext servletContext,
				Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
			Map<String, ResourceDescription> descriptions = new LinkedHashMap<>();

			for (Map.Entry<String, List<ResourceInvoker>> entry : bound) {
				Method aMethod = ((ResourceMethodInvoker) entry.getValue().get(0)).getMethod();
				String basePath = aMethod.getDeclaringClass().getAnnotation(Path.class).value();

				if (!descriptions.containsKey(basePath)) {
					descriptions.put(basePath, new ResourceDescription(basePath));
				}

				for (ResourceInvoker invoker : entry.getValue()) {
					ResourceMethodInvoker method = (ResourceMethodInvoker) invoker;
					String subPath = null;
					for (Annotation annotation : method.getMethodAnnotations()) {
						if (annotation.annotationType().equals(Path.class)) {
							subPath = Path.class.cast(annotation).value();
							break;
						}
					}
					descriptions.get(basePath).addMethod(basePath + (subPath == null ? "" : subPath), method);
				}
			}

			LinkedList<ResourceDescription> ll = new LinkedList<>(descriptions.values());
			return ll;
		}

		private static String mostPreferredOrNull(MediaType[] mediaTypes) {
			if ((mediaTypes == null) || (mediaTypes.length < 1)) {
				return null;
			}
			return mediaTypes[0].toString();
		}

		@XmlAttribute
		private String basePath;

		private List<MethodDescription> calls;

		public ResourceDescription() {
			super();
		}

		public ResourceDescription(String basePath) {
			this.basePath = basePath;
			this.calls = new LinkedList<>();
		}

		public void addMethod(String path, ResourceMethodInvoker method) {
			String produces = mostPreferredOrNull(method.getProduces());
			String consumes = mostPreferredOrNull(method.getConsumes());
			for (String verb : method.getHttpMethods()) {
				this.calls.add(new MethodDescription(verb, path, produces, consumes));
			}
		}

		public String getBasePath() {
			return this.basePath;
		}

		public void setBasePath(String basePath) {
			this.basePath = basePath;
		}

		public List<MethodDescription> getCalls() {
			return this.calls;
		}

		public void setCalls(List<MethodDescription> calls) {
			this.calls = calls;
		}
	}

	public RestEasyApiResource() {
		super();
	}

	@GET
	@Path("/ping" + "." + TEXT_EXTENSION)
	@Produces(TEXT)
	public String ping() {
		return String.valueOf(System.currentTimeMillis());
	}

	@GET
	@Path("/" + JSON_EXTENSION)
	@Produces({ JSON })
	public List<ResourceDescription> getAvailableEndpointsJson(@Context ServletContext servletContext,
			@Context Dispatcher dispatcher) {
		return getAvailableEndpoints(servletContext, dispatcher);
	}

	protected List<ResourceDescription> getAvailableEndpoints(ServletContext servletContext, Dispatcher dispatcher) {
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
		return ResourceDescription.fromBoundResourceInvokers(servletContext, registry.getBounded().entrySet());
	}

	@GET
	@Path("/" + XML_EXTENSION)
	@Produces({ XML })
	public List<ResourceDescription> getAvailableEndpointsXml(@Context ServletContext servletContext,
			@Context Dispatcher dispatcher) {
		return getAvailableEndpoints(servletContext, dispatcher);
	}

	// With @Context you can inject HttpHeaders, UriInfo, Request,
	// HttpServletRequest, HttpServletResponse, ServletConvig, ServletContext,
	// SecurityContext
	@GET
	@Path("/")
	@Produces(HTML)
	public Response getAvailableEndpointsHtml(@Context ServletContext servletContext, @Context Dispatcher dispatcher) {
		StringBuilder sb = new StringBuilder();
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
		List<ResourceDescription> descriptions = ResourceDescription.fromBoundResourceInvokers(servletContext,
				registry.getBounded().entrySet());

		sb.append("<h1>").append("REST interface overview").append("</h1>");

		for (ResourceDescription resource : descriptions) {
			sb.append("<h2>").append(resource.basePath).append("</h2>");
			sb.append("<ul>");

			for (MethodDescription method : resource.calls) {
				sb.append("<li> ").append(method.method).append(" ");
				sb.append("<a href='");
				String url = servletContext.getContextPath() + "/rest"
						+ (method.fullPath.startsWith("/") ? method.fullPath : "/" + method.fullPath);
				sb.append(url).append("'>").append(method.fullPath).append("</a>");
				sb.append(" : ");
				if (method.consumes != null) {
					sb.append(method.consumes).append(" << ");
				}
				if (method.produces != null) {
					sb.append(" >> ").append(method.produces);
				}
			}

			sb.append("</ul>");
		}

		return Response.ok(sb.toString()).build();
	}
}
