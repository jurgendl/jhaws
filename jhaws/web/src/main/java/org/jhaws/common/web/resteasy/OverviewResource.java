package org.jhaws.common.web.resteasy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.springframework.stereotype.Component;

@Component
@Path("/overview")
public class OverviewResource {
	private static final class MethodDescription {
		private String method;

		private String fullPath;

		private String produces;

		private String consumes;

		public MethodDescription(String method, String fullPath, String produces, String consumes) {
			super();
			this.method = method;
			this.fullPath = fullPath;
			this.produces = produces;
			this.consumes = consumes;
		}
	}

	private static final class ResourceDescription {
		public static List<ResourceDescription> fromBoundResourceInvokers(Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
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
							subPath = ((Path) annotation).value();
							break;
						}
					}

					descriptions.get(basePath).addMethod(basePath + subPath, method);
				}
			}

			return new LinkedList<>(descriptions.values());
		}

		private static String mostPreferredOrNull(MediaType[] mediaTypes) {
			if ((mediaTypes == null) || (mediaTypes.length < 1)) {
				return null;
			}
			return mediaTypes[0].toString();
		}

		private String basePath;

		private List<MethodDescription> calls;

		public ResourceDescription(String basePath) {
			this.basePath = basePath;
			this.calls = new LinkedList<>();
		}

		public void addMethod(String path, ResourceMethodInvoker method) {
			String produces = ResourceDescription.mostPreferredOrNull(method.getProduces());
			String consumes = ResourceDescription.mostPreferredOrNull(method.getConsumes());

			for (String verb : method.getHttpMethods()) {
				this.calls.add(new MethodDescription(verb, path, produces, consumes));
			}
		}
	}

	public OverviewResource() {
		super();
	}

	@GET
	@Path("/ping/")
	@Produces("text/plain")
	public String ping() {
		return String.valueOf(System.currentTimeMillis());
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResourceDescription> getAvailableEndpoints(@Context Dispatcher dispatcher) {
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
		return ResourceDescription.fromBoundResourceInvokers(registry.getBounded().entrySet());
	}

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public Response getAvailableEndpointsHtml(@Context Dispatcher dispatcher) {

		StringBuilder sb = new StringBuilder();
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher.getRegistry();
		List<ResourceDescription> descriptions = ResourceDescription.fromBoundResourceInvokers(registry.getBounded().entrySet());

		sb.append("<h1>").append("REST interface overview").append("</h1>");

		for (ResourceDescription resource : descriptions) {
			sb.append("<h2>").append(resource.basePath).append("</h2>");
			sb.append("<ul>");

			for (MethodDescription method : resource.calls) {
				sb.append("<li> ").append(method.method).append(" ");
				sb.append("<strong>").append(method.fullPath).append("</strong>");

				sb.append("<ul>");

				if (method.consumes != null) {
					sb.append("<li>").append("Consumes: ").append(method.consumes).append("</li>");
				}

				if (method.produces != null) {
					sb.append("<li>").append("Produces: ").append(method.produces).append("</li>");
				}

				sb.append("</ul>");
			}

			sb.append("</ul>");
		}

		return Response.ok(sb.toString()).build();

	}
}
