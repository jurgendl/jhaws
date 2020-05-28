package org.jhaws.common.web.wicket.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.GZIP;
import org.jhaws.common.web.resteasy.Pretty;
import org.jhaws.common.web.resteasy.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Path(VueTestRest.PATH)
@Pretty
@GZIP
@Component
@Controller
public class VueTestRest implements RestResource {
	public static final String OID = "oid";
	public static final String PATH = "/vuetest";
	public static final String PATH_LIST = "/list";
	public static final String PARAM_ID = "id";
	public static final String PATH_REMOVE_SIMPLE = "/remove/";
	public static final String PATH_REMOVE = PATH_REMOVE_SIMPLE + "{" + PARAM_ID + "}";
	private Random r = new Random();
	private Map<String, Map<String, List<Map<String, Object>>>> map = new HashMap<>();
	private int i;

	public VueTestRest() {
		Map<String, List<Map<String, Object>>> data = new HashMap<>();
		map.put("data", data);
		List<Map<String, Object>> elements = new ArrayList<>();
		for (i = 0; i < 10; i++) {
			Map<String, Object> element = new HashMap<>();
			element.put("id", UUID.randomUUID().toString());
			element.put("order", i);
			element.put("show", true);
			elements.add(element);
		}
		data.put("elements", elements);
	}

	@POST
	@Path(PATH_REMOVE)
	public void remove(@PathParam(PARAM_ID) String id) {
		List<Map<String, Object>> elements = map.get("data").get("elements");
		elements.stream().filter(element -> id.equals(element.get("id"))).forEach(element -> element.put("show", false));
	}

	@GET
	@Path(PATH_LIST)
	@Produces(JSON)
	@org.jboss.resteasy.annotations.providers.jackson.Formatted
	public Map<String, Map<String, List<Map<String, Object>>>> list() {
		List<Map<String, Object>> elements = map.get("data").get("elements");
		if (r.nextInt(100) == 0) {
			elements.clear();
		}
		Map<String, Object> rndEl = elements.stream().filter(m -> Boolean.TRUE.equals(m.get("show"))).findAny().orElse(null);
		if (r.nextInt(5) < 2 && rndEl != null) {
			rndEl.put("show", false);
		} else {
			Map<String, Object> element = new HashMap<>();
			element.put("id", UUID.randomUUID().toString());
			element.put("order", i++);
			element.put("show", true);
			elements.add(element);
		}
		return map;
	}
}
