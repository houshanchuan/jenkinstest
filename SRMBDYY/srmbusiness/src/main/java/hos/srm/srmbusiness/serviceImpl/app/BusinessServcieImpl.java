package hos.srm.srmbusiness.serviceImpl.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hos.srm.srmbusiness.model.dto.SrmBussinessDto;
import hos.srm.srmbusiness.service.app.BusinessServcie;
import hos.srm.srmbusiness.utils.NoHttpsClientUtils;
import hos.srm.srmbusiness.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
@Service
public class BusinessServcieImpl implements BusinessServcie {
    @Value("${cacheinfo.userName}")
    private String userName;

    @Value("${cacheinfo.passWord}")
    private String passWord;

    @Value("${cacheinfo.url}")
    private String url;
    @Override
    public String sendBusinessData(SrmBussinessDto srmBussinessDto) {
        JSONObject resultJson = new JSONObject();
        String result = null;
        Map<String, String> params = new HashMap<>();
        params.put("CacheUserName", userName);
        params.put("CachePassword", passWord);
        params.put("RequestCode", srmBussinessDto.getRequestCode());
        params.put("RequestInput", srmBussinessDto.getRequestInput());
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            // 指定请求头格式为JSON
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            result = responseEntity.getBody();
            //System.out.println(result);
            Map<String, String> resMap = JSON.parseObject(result, HashMap.class);

        } catch (Exception e) {
            System.out.println(e.getMessage());

            // 异常处理
        }
        return result;
    }
    /*
    public String sendBusinessData2(SrmBussinessDto srmBussinessDto) {

        String result = null;
        // 调用接口url
        String url ="https://58.56.200.231:1443/imedical/rest/open/requestMethod";

        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            // 出参构造
            JSONObject resultJson = new JSONObject();
            HttpEntity<MultiValueMap<String, String>> requestEntity = getMultiValueMapHttpEntity(srmBussinessDto);
            // post方式调用
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            result = responseEntity.getBody();
            // String转JSON
            //resultJson = JSONObject.parseObject(result);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            // 异常处理
        }
        return result;
    }
    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(SrmBussinessDto srmBussinessDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        // 指定请求头格式为JSON
        httpHeaders.add("Transfer-Encoding", "chunked");
        httpHeaders.add("Content-type", "application/json;charset=UTF-8");


        // 创建HttpEntity对象，包含请求体和headers
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("CacheUserName", "_SYSTEM");
        map.add("CachePassword", "15I7dSa%0P");
        map.add("RequestCode", srmBussinessDto.getRequestCode());
        map.add("RequestInput", srmBussinessDto.getRequestInput());
        System.out.println(srmBussinessDto.getRequestInput());
        return new HttpEntity<>(map, httpHeaders);
    }

     */

}
