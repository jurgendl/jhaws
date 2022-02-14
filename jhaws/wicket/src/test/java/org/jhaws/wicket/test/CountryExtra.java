package org.jhaws.wicket.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.apache.commons.io.IOUtils;
import org.jhaws.common.net.client.HTTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CountryExtra {
    public static JsonStructure _json(String json) {
        try {
            return Json.createReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes()))).readObject();
        } catch (Exception ex) {
            return Json.createReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes()))).readArray();
        }
    }

    public static void main(String[] args) {
        new CountryExtra().test();
    }

    public static JsonStructure json(String json) {
        return _json(json);
    }

    public static JsonStructure jsonLenient(String json) {
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement el = parser.parse(json);
        json = gson.toJson(el);
        return json(json);
    }

    private void test() {
        try {
            InputStream input = CountryExtra.class.getClassLoader().getResourceAsStream("org/tools/hqlbuilder/webservice/wicket/forms/bootstrap/country/countries.js");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(input, output);
            String r = new String(output.toByteArray()).replace("var countrydata = ", "").replace("\r", " ").replace("\n", " ");
            JsonArray el = (JsonArray) json(r.substring(0, r.length() - 1));
            List<Map<String, String>> refurb = new ArrayList<>();
            for (int i = 0; i < el.size(); i++) {
                JsonValue jv = el.get(i);
                @SuppressWarnings("unchecked")
                Map<String, JsonString> tmp = Map.class.cast(jv.asJsonObject());
                Map<String, String> tmp2 = new LinkedHashMap<>();
                tmp.entrySet().forEach(e -> {
                    tmp2.put(e.getKey(), e.getValue().getString());
                });
                refurb.add(tmp2);
            }
            try (HTTPClient hc = new HTTPClient()) {
                String html = hc.get("https://en.wikipedia.org/wiki/List_of_adjectival_and_demonymic_forms_for_countries_and_nations").getContentString();
                Document parsed = Jsoup.parse(html);
                Elements elements = parsed.select(".wikitable tr");
                Iterator<Element> it = elements.iterator();
                while (it.hasNext()) {
                    Element tag = it.next();
                    Elements elements2 = tag.select("td");
                    Iterator<Element> it2 = elements2.iterator();
                    if (it2.hasNext()) {
                        Element tag2a = it2.next();
                        if (it2.hasNext()) {
                            Element tag2b = it2.next();
                            tag2a = tag2a.select("a").first();
                            String country = tag2a.html();
                            Map<String, String> found = refurb.stream().filter(a -> a.get("name").equals(country)).findAny().orElse(null);
                            if (found != null) {
                                Elements tag2ba = tag2b.select("a");
                                Iterator<Element> tag2bai = tag2ba.iterator();
                                List<String> all = new ArrayList<>();
                                while (tag2bai.hasNext()) {
                                    String adjectivals = tag2bai.next().html();
                                    if (!adjectivals.startsWith("[")) {
                                        all.add(adjectivals);
                                    }
                                }
                                if (!all.isEmpty()) {
                                    found.put("adjectivals", all.stream().collect(Collectors.joining(";")));
                                }
                            }
                        }
                    }
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(refurb);
            System.out.println("var countrydata = " + json + ";");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
