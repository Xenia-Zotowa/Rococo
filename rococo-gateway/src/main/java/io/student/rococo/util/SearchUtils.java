package io.student.rococo.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class SearchUtils {
    public static Pageable createPageable(Map<String, String> params) {
        int size = Integer.parseInt(params.getOrDefault("size", "18"));
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        return Pageable.unpaged();
    }

    public static boolean containsIgnoreCase(String text, String query) {
        if (text == null || query == null) return false;
        return text.toLowerCase().contains(query.toLowerCase());
    }
}