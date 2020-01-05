package ro.unibuc.master.groupexpensetracker.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityUtils {

    private static final String FILTER_PATTERN = "([a-zA-Z.]+)(:|<|>|!)([a-zA-Z0-9-*_!\\*'\\(\\);:@&=+\\$/\\?%#\\[\\] ]+)|(.*!\\\\(.*EN.*\\\\).*),";

    private static final String[] RESERVED_CHARACTERS = {"_", "!",  "\\*", "'", "\\(", "\\)", ";", ":", "@" ,"&", "=", "\\$", "," , "/", "\\?", "%", "#", "\\[",  "]"};

    public static PageRequest getPageRequest(Sort.Direction direction, String orderBy, Integer page, Integer size) {
        return direction != null && orderBy != null ?
                PageRequest.of(page, size, direction, orderBy) : PageRequest.of(page, size);
    }

    public static List<SearchCriteria> generateSearchCriteria(final String search) {
        final List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        final Pattern pattern = Pattern.compile(FILTER_PATTERN);
        final Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            String matcherGroup = matcher.group(3);
            for(String character : RESERVED_CHARACTERS) {
                matcherGroup = matcherGroup.replaceAll(character, "\\" + character);
            }
            searchCriteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcherGroup));
        }
        return searchCriteriaList;
    }
}
