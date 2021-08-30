package org.jhaws.common.web.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

// @Configuration
// @ConfigurationProperties(prefix = "tests")
// @PropertySource(value = { //
// "classpath:/tests.yml", //
// "classpath:/tests.properties", //
// "classpath:/tests.xml", //
// "classpath:/tests.json",//
// }//
// , factory = AdaptingPropertySourceFactory.class//
// )
//
// https://www.baeldung.com/spring-yaml-propertysource
// https://www.baeldung.com/spring-boot-json-properties
public class AdaptingPropertySourceFactory implements PropertySourceFactory {
    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) throws IOException {
        String filename = encodedResource.getResource().getFilename();
        if (filename.toLowerCase().endsWith(".properties")) {
            return (name != null ? new ResourcePropertySource(name, encodedResource) : new ResourcePropertySource(encodedResource));
        }
        if (filename.toLowerCase().endsWith(".xml")) {
            return (name != null ? new ResourcePropertySource(name, encodedResource) : new ResourcePropertySource(encodedResource));
        }
        if (filename.toLowerCase().endsWith(".yml")) {
            try {
                YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
                factory.setResources(encodedResource.getResource());
                Properties properties = factory.getObject();
                return new PropertiesPropertySource(filename, properties);
            } catch (java.lang.IllegalStateException ex) {
                if (ex.getCause() instanceof IOException) {
                    throw (IOException) ex.getCause();
                }
                throw ex;
            }
        }
        if (filename.toLowerCase().endsWith(".json")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> readValue = objectMapper.readValue(encodedResource.getInputStream(), Map.class);
            Set<Map.Entry<String, Object>> set = readValue.entrySet();
            List<MapPropertySource> propertySources = convertEntrySet(set, Optional.empty());
            Map<String, Object> merged = new LinkedHashMap<>();
            propertySources.forEach(t -> {
                merged.putAll(t.getSource());
            });
            return new MapPropertySource(filename, merged);
        }
        throw new UnsupportedOperationException();
    }

    protected List<MapPropertySource> convertEntrySet(Set<Map.Entry<String, Object>> entrySet, Optional<String> parentKey) {
        return entrySet.stream()
                .map((Map.Entry<String, Object> e) -> convertToPropertySourceList(e, parentKey))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    protected List<MapPropertySource> convertToPropertySourceList(Map.Entry<String, Object> e, Optional<String> parentKey) {
        String key = parentKey.map(s -> s + ".").orElse("") + e.getKey();
        Object value = e.getValue();
        return covertToPropertySourceList(key, value);
    }

    protected List<MapPropertySource> covertToPropertySourceList(String key, Object value) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            Set<Map.Entry<String, Object>> entrySet = map.entrySet();
            return convertEntrySet(entrySet, Optional.ofNullable(key));
        }
        String finalKey = key;
        return Collections.singletonList(new MapPropertySource(finalKey, Collections.singletonMap(finalKey, value)));
    }
}