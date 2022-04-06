package org.jhaws.common.net.reastclient;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

import com.fasterxml.jackson.core.type.TypeReference;

public interface RestConverter<T> {
    org.springframework.core.ParameterizedTypeReference<T> springweb();

    com.fasterxml.jackson.core.type.TypeReference<T> jackson();

    static final RestConverter<String> STRING = new RestConverter<String>() {
        @Override
        public ParameterizedTypeReference<String> springweb() {
            return new ParameterizedTypeReference<String>() {
                /**/ };
        }

        @Override
        public TypeReference<String> jackson() {
            return new TypeReference<String>() {/**/
            };
        }
    };

    static final RestConverter<List<String>> STRING_LIST = new RestConverter<List<String>>() {
        @Override
        public ParameterizedTypeReference<List<String>> springweb() {
            return new ParameterizedTypeReference<List<String>>() {
                /**/ };
        }

        @Override
        public TypeReference<List<String>> jackson() {
            return new TypeReference<List<String>>() {/**/
            };
        }
    };

    static final RestConverter<Long> LONG = new RestConverter<Long>() {
        @Override
        public ParameterizedTypeReference<Long> springweb() {
            return new ParameterizedTypeReference<Long>() {
                /**/ };
        }

        @Override
        public TypeReference<Long> jackson() {
            return new TypeReference<Long>() {/**/
            };
        }
    };

    static final RestConverter<Map<String, Object>> MAP = new RestConverter<Map<String, Object>>() {
        @Override
        public ParameterizedTypeReference<Map<String, Object>> springweb() {
            return new ParameterizedTypeReference<Map<String, Object>>() {
                /**/ };
        }

        @Override
        public TypeReference<Map<String, Object>> jackson() {
            return new TypeReference<Map<String, Object>>() {/**/
            };
        }
    };
}
