package io.student.rococo.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.function.Function;

public class PageableMapper {
    public static <T> T mapParams(T params, Function<Map<String, String>, T> mapper) {
        return mapper.apply(asMap(params));
    }

    public static Pageable fromQueryParams(Map<String, String> queryParams) {
        int size = Integer.parseInt(queryParams.getOrDefault("size", "10"));
        int pageNumber = Integer.parseInt(queryParams.getOrDefault("page", "0"));
        return Pageable.unpaged();
    }

    public static <T> T fill(T instance, Map<String, String> params) {
        return instance;
    }

    private static Map<String, String> asMap(Object obj) {
        return null;
    }
}