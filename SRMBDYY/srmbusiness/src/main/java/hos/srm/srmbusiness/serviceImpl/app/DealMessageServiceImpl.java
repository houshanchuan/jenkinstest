package hos.srm.srmbusiness.serviceImpl.app;

import com.alibaba.fastjson.JSON;
import hos.srm.srmbusiness.exception.ErrorException;
import hos.srm.srmbusiness.model.dto.SrmMessageDto;
import hos.srm.srmbusiness.service.app.DealMessageService;
import hos.srm.srmbusiness.utils.NoHttpsClientUtils;
import hos.srm.srmbusiness.utils.ReponseResult;
import hos.srm.srmbusiness.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class DealMessageServiceImpl implements DealMessageService {
    //@Value("${qyWx.corPid}")
    private String corPid="ww5e8f9a58d8145b4d";
    //@Value("${qyWx.corpSecret}")
    private String corpSecret="xoqFdoaJo2WZIYjgeAoiAUgjMOc3R6_88TVaLP-zhQA";
    private int agenId=1000105;
    public static Map<String, LocalDateTime> tokenMap=new HashMap<>();

    private String weChatIp="https://qyapi.weixin.qq.com";
    private String weChatDlIp="http://172.31.61.81:9999/qyapi";
    public String getMapToken() {
        String token=null;
        int size=tokenMap.keySet().size();
        if(size!=1)return null;
        token=tokenMap.keySet().iterator().next();
        LocalDateTime time=tokenMap.get(token);
        if(LocalDateTime.now().compareTo(time)<7000){
            token=null;
        }
        return token;
    }
    @Override
    public String getAccessToken() {
        String accessToken = getMapToken();
        if (accessToken != null) {
            return accessToken;
        }
        //String url ="https://qyapi.weixin.qq.com/cgi-bin/gettoken";
        String url=weChatDlIp+"/gettoken";
        Map<String, String> params = new HashMap<>();
        params.put("corpid", corPid);
        params.put("corpsecret", corpSecret);
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            String result = responseEntity.getBody();
            Map<String, Object> resMap = JSON.parseObject(result, HashMap.class);
            //System.out.println("res:"+resMap);
            String errocde=String.valueOf(resMap.get("errcode"));
            if (!errocde.equals("0")){
                throw new ErrorException("-200","获取token失败");
            }
            accessToken=String.valueOf(resMap.get("access_token"));



        } catch (Exception e) {
            throw new ErrorException("-200","获取token失败");
            // 异常处理
        }
        return accessToken;
    }

    // 获取成员ID列表
    @Override
    public ReponseResult listDept() {
        String result=null;
        String accessToken =getAccessToken();
        //String url ="https://qyapi.weixin.qq.com/cgi-bin/department/simplelist";
        String url=weChatDlIp+"/department/simplelist";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("id", null);
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            result = responseEntity.getBody();
            //System.out.println(result);

        } catch (Exception e) {
            throw new ErrorException("-200","获取用户列表失败");
            // 异常处理
        }
        return new ReponseResult<>(200,"",result);
    }
    // 获取用户信息
    @Override
    public String sendMessage(SrmMessageDto srmMessageDto) {
        //System.out.println(srmMessageDto);
        String accessToken=this.getAccessToken();
        // 推送消息
        String content=srmMessageDto.getMessage();
        String userId=srmMessageDto.getUserIdStr();
        String res="";
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            //
            // String url2 = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
            // req
            String url2 = weChatDlIp+"/message/send?access_token=" + accessToken;

            Map<String, Object> req = new HashMap<>();
            Map<String, Object> contentMap = new HashMap<>();
            contentMap.put("content", content);
            req.put("text", contentMap);
            req.put("touser", userId);
            req.put("msgtype", "text");
            req.put("agentid", agenId);
            //System.out.println(req);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url2, req, String.class);
            res = responseEntity.getBody();
            //System.out .println(res);
        } catch (Exception e) {
            throw new ErrorException("-200","推送失败");
            // 异常处理
        }
        return res;
    }
    @Override
    public ReponseResult listUser(SrmMessageDto srmMessageDto) {
        String result=null;
        String accessToken =getAccessToken();
        String deptId=srmMessageDto.getDeptId();
        //String url ="https://qyapi.weixin.qq.com/cgi-bin/user/simplelist";
        String url=weChatDlIp+"/user/simplelist";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("department_id", deptId);
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            result = responseEntity.getBody();
            //System.out.println(result);

        } catch (Exception e) {
            throw new ErrorException("-200","获取用户列表失败");
            // 异常处理
        }
        return new ReponseResult<>(200,"",result);
    }

    @Override
    public ReponseResult listUserDetail(SrmMessageDto srmMessageDto) {
        String result=null;
        String accessToken =getAccessToken();
        String deptId=srmMessageDto.getDeptId();
        String url =weChatDlIp+"/user/list";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("department_id", deptId);
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            result = responseEntity.getBody();
            //System.out.println(result);

        } catch (Exception e) {
            throw new ErrorException("-200","获取用户列表失败");
            // 异常处理
        }
        return new ReponseResult<>(200,"",result);
    }

    @Override
    public ReponseResult getUserDetailByCode(SrmMessageDto srmMessageDto) {
        String result=null;
        String accessToken =getAccessToken();
        String code=srmMessageDto.getCode();
        String url =weChatDlIp+"/auth/getuserinfo";
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("code", code);
        URI safeUri = RestTemplateUtils.buildSafeURI(url, params);
        try {
            RestTemplate restTemplate = NoHttpsClientUtils.getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Transfer-Encoding", "chunked");
            httpHeaders.add("Content-type", "application/json;charset=UTF-8");
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(safeUri, requestEntity, String.class);
            result = responseEntity.getBody();
            //System.out.println(result);

        } catch (Exception e) {
            throw new ErrorException("-200","获取用户列表失败");
            // 异常处理
        }
        return new ReponseResult<>(200,"",result);
    }
}
