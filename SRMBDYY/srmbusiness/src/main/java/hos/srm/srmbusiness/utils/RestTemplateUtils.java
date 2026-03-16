package hos.srm.srmbusiness.utils;


import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

public class RestTemplateUtils {

    public static String encodeSpecialChars(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }
        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static URI buildSafeURI(String baseUrl, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String encodedValue = encodeSpecialChars(entry.getValue());
            builder.queryParam(entry.getKey(), encodedValue);
        }

        return builder.build(true).toUri();
    }
}

