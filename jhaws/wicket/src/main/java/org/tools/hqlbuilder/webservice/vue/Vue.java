package org.tools.hqlbuilder.webservice.vue;

import org.tools.hqlbuilder.webservice.wicket.CssResourceReference;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// https://vuejs.org/v2/guide/syntax.html
// https://jsfiddle.net/fxszLc2j/6/
// https://vuejs.org/v2/guide/list.html#Mutation-Methods
// https://github.com/vuejs/vue-devtools
// https://bootstrap-vue.js.org/
public class Vue {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Vue.class, "vue.js");

    /**
     * Wicket renderHead:<br>
     * response.render(JavaScriptHeaderItem.forReference(Vue.JS_APP));<br>
     * response.render(JavaScriptHeaderItem.forScript(";var testVueApplication = AppVue('" + RestCte.getRestPath() + VueTestRest.PATH +
     * "','appVueTest');", "vuetest_rest_init"));<br>
     * <br>
     * RestEasy:<br>
     * 
     * <pre>
     * &#64;Path(VueTestRest.PATH)
     * &#64;Pretty
     * &#64;GZIP
     * &#64;Component
     * &#64;Controller
     * public class VueTestRest implements RestConstants, RestResource {
     *     public static final String PATH = "/vuetest";
     *     public static final String PATH_LIST = "/list";
     *     public static final String PARAM_ID = "id";
     *     public static final String PATH_REMOVE_SIMPLE = "/remove/";
     *     public static final String PATH_REMOVE = PATH_REMOVE_SIMPLE + "{" + PARAM_ID + "}";
     *     private Map<String, Map<String, List<Map<String, Object>>>> map = new HashMap<>();
     *     public VueTestRest() {
     *         Map<String, List<Map<String, Object>>> data = new HashMap<>();
     *         map.put("data", data);
     *         List<Map<String, Object>> elements = new ArrayList<>();
     *         ...
     *         data.put("elements", elements);
     *     }
     *     &#64;POST
     *     &#64;Path(PATH_REMOVE)
     *     public void remove(@PathParam(PARAM_ID) String id) {
     *         List<Map<String, Object>> elements = map.get("data").get("elements");
     *         elements.stream().filter(element -> id.equals(element.get("id"))).forEach(element -> element.put("show", false));
     *     }
     *     &#64;GET
     *     &#64;Path(PATH_LIST)
     *     &#64;Produces(JSON)
     *     &#64;org.jboss.resteasy.annotations.providers.jackson.Formatted
     *     public Map<String, Map<String, List<Map<String, Object>>>> list() {
     *         List<Map<String, Object>> elements = map.get("data").get("elements");
     *         ...
     *         return map;
     *     }
     * }
     * </pre>
     * 
     * <br>
     * Html:<br>
     * 
     * <pre>
        &lt;div style="display:none" id="appVueTest"&gt;
            &lt;span v-for="(element, index) in elements"&gt;
                &lt;transition appear name="fade"&gt;
                    &lt;div v-show="element.show" class="card border-dark bg-light m-2 p-0" :id="element.id" :key="element.id"&gt;
                        &lt;div class="card-body p-2"&gt;
                            &lt;h5 class="card-title text-muted"&gt;{{ element.id }}&lt;/h5&gt;
                            &lt;h6 class="card-subtitle mb-2 text-muted"&gt;{{ element.order }}&lt;/h6&gt;
                            &lt;p class="card-text"&gt;&lt;small class="text-muted"&gt;{{ index }}&lt;/small&gt;&lt;/p&gt;
                        &lt;/div&gt;
                        &lt;button onclick="appVueTest.remove(this)" style="position:absolute;top:0.25rem;right:0.25rem;" class="btn btn-sm btn-danger"&gt;&lt;i class="fa-fw fas fa-stop-circle"&gt;&lt;/i&gt;&lt;/button&gt;
                    &lt;/div&gt;
                &lt;/transition&gt;
            &lt;/span&gt;
        &lt;/div&gt;
     * </pre>
     */
    public static JavaScriptResourceReference JS_APP = new JavaScriptResourceReference(Vue.class, "vue.app.js");

    public static CssResourceReference JS_APP_CSS = new CssResourceReference(Vue.class, "vue.app.css");

    static {
        JS_APP.addJavaScriptResourceReferenceDependency(JS);
        JS_APP.addCssResourceReferenceDependency(JS_APP_CSS);
    }
}
