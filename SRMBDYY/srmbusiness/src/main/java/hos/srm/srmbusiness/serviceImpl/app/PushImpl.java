package hos.srm.srmbusiness.serviceImpl.app;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.*;
        import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.res.QueryCidResDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.google.gson.JsonObject;
import hos.srm.srmbusiness.model.dto.app.PushNoticeDto;
import hos.srm.srmbusiness.model.vo.app.PushNoticeVo;
import hos.srm.srmbusiness.service.app.PushService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
//@Service
public class PushImpl implements PushService {

    @Resource(name = "myApiHelper")
    private ApiHelper myApiHelper;


    /**
     * 绑定别名
     * @param cid  用户在推送服务器的唯一识别标志
     * @param alias 在自己服务器上的唯一识别标志
     * @return 绑定结果
     */
    public PushNoticeVo bindAlias(String cid, String alias){
        PushNoticeVo rb=new PushNoticeVo();
        CidAliasListDTO cidAliasListDTO=new CidAliasListDTO();
        CidAliasListDTO.CidAlias cidAlias=new CidAliasListDTO.CidAlias();
        cidAlias.setCid(cid);
        cidAlias.setAlias(alias);
        cidAliasListDTO.add(cidAlias);
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        ApiResult<Void> voidApiResult = userApi.bindAlias(cidAliasListDTO);
        rb.setCode(voidApiResult.getCode());
        rb.setMsg(voidApiResult.getMsg());
        rb.setData(voidApiResult.getData());
        return rb;
    }

    /**
     * 批量解绑别名
     * @param aliasList 别名列表
     * @return 解绑结果
     *
     */
    public PushNoticeVo unbindAlias(List<String> aliasList){
        PushNoticeVo rb=new PushNoticeVo();
        List<CidAliasListDTO.CidAlias> list=new ArrayList<>();
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        for (String alias:aliasList){
            ApiResult<QueryCidResDTO> queryCidResDTOApiResult = userApi.queryCidByAlias(alias);
            if (queryCidResDTOApiResult.isSuccess()){
                List<String> cidList = queryCidResDTOApiResult.getData().getCid();
                for (String cid:cidList){
                    CidAliasListDTO.CidAlias cidAlias=new CidAliasListDTO.CidAlias();
                    cidAlias.setAlias(alias);
                    cidAlias.setCid(cid);
                    list.add(cidAlias);
                }
            }
        }
        CidAliasListDTO cidAliasListDTO=new CidAliasListDTO();
        cidAliasListDTO.setDataList(list);
        ApiResult<Void> voidApiResult = userApi.batchUnbindAlias(cidAliasListDTO);
        rb.setCode(voidApiResult.getCode());
        rb.setMsg(voidApiResult.getMsg());
        rb.setData(voidApiResult.getData());
        return rb;
    }

    /**
     * 一个用户根据cid进行绑定tag标签(次数限制)
     * @param cid 用户在推送服务器的唯一识别标志
     * @param tag 标签名
     * @return 绑定结果
     */
    public PushNoticeVo userBindTagsByCid(String cid,String tag){
        PushNoticeVo rb=new PushNoticeVo();
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        TagDTO dto=new TagDTO();
        dto.addTag(tag);
        ApiResult<Void> voidApiResult = userApi.userBindTags(cid, dto);
        rb.setCode(voidApiResult.getCode());
        rb.setMsg(voidApiResult.getMsg());
        rb.setData(voidApiResult.getData());
        return rb;
    }

    /**
     * 一个用户根据别名进行绑定tag标签(次数限制)
     * @param alias 在自己服务器上的唯一识别标志
     * @param tag 标签名
     * @return 绑定结果
     */
    public PushNoticeVo userBindTagsByAlias(String alias,String tag){
        PushNoticeVo rb=new PushNoticeVo();
        rb.setCode(1);
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        ApiResult<QueryCidResDTO> queryCidResDTOApiResult = userApi.queryCidByAlias(alias);
        if (queryCidResDTOApiResult.isSuccess()){
            List<String> cidList = queryCidResDTOApiResult.getData().getCid();
            if (cidList.size()==1){
                String cid = cidList.get(0);
                TagDTO dto=new TagDTO();
                dto.addTag(tag);
                ApiResult<Void> voidApiResult = userApi.userBindTags(cid, dto);
                rb.setCode(voidApiResult.getCode());
                rb.setMsg(voidApiResult.getMsg());
                rb.setData(voidApiResult.getData());
            }else {
                rb.setMsg("该别名对应多个cid,无法绑定标签");
            }
        }else {
            rb.setMsg(queryCidResDTOApiResult.getMsg());
        }
        return rb;
    }

    /**
     * 一批用户绑定tag标签(每分钟100次，每日10000次)
     * @param cidList 用户在推送服务器的唯一识别标志
     * @param tag 标签名
     * @return 绑定结果
     */
    public PushNoticeVo userBindTagsByAlias(List<String> cidList,String tag){
        PushNoticeVo rb=new PushNoticeVo();
        rb.setCode(1);
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        UserDTO userDTO=new UserDTO();
        Set<String> cidSet=new HashSet<>(cidList);
        userDTO.setCid(cidSet);
        ApiResult<Map<String, String>> mapApiResult = userApi.usersBindTag(tag, userDTO);
        rb.setCode(mapApiResult.getCode());
        rb.setMsg(mapApiResult.getMsg());
        rb.setData(mapApiResult.getData());
        return rb;
    }

    /**
     * 批量用户解绑一个标签
     * @param list 用户集合(可cid集合或alias集合)
     * @param type 0表示alias集合，1表示cid集合
     * @param tag 标签
     * @return 解绑结果
     */
    public PushNoticeVo usersUnbindTag(List<String> list,int type,String tag){
        PushNoticeVo rb=new PushNoticeVo();
        rb.setCode(1);
        UserApi userApi = myApiHelper.creatApi(UserApi.class);
        UserDTO userDTO=new UserDTO();
        List<String> cidList=new ArrayList<>();
        if (type==0){
            for (String alias:list){
                ApiResult<QueryCidResDTO> queryCidResDTOApiResult = userApi.queryCidByAlias(alias);
                if (queryCidResDTOApiResult.isSuccess()){
                    cidList.addAll(queryCidResDTOApiResult.getData().getCid());
                }
            }
        }else {
            cidList.addAll(list);
        }
        Set<String> cidSet=new HashSet<>(cidList);
        userDTO.setCid(cidSet);
        ApiResult<Map<String, String>> mapApiResult = userApi.deleteUsersTag(tag, userDTO);
        rb.setCode(mapApiResult.getCode());
        rb.setMsg(mapApiResult.getMsg());
        rb.setData(mapApiResult.getData());
        return rb;
    }



    /**
     * 群推透传消息
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushMessageToAll(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<String> pushDTO = new PushDTO<String>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        pushDTO.setAudience("all");
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> apiResult = pushApi.pushAll(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    /**
     * 根据cid推送消息(含批量)
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushMessageToCid(PushNoticeDto bean){
        boolean isMany=bean.getUser().contains(",");
        if (isMany){
            return pushMessageToManyCid(bean);
        }else {
            return pushMessageToSingleCid(bean);
        }
    }

    /**
     * 根据别名推送消息(含批量)
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushMessageToAlias(PushNoticeDto bean){
        boolean isMany=bean.getUser().contains(",");
        if (isMany){
            return pushMessageToManyAlias(bean);
        }else {
            return pushMessageToSingleAlias(bean);
        }
    }

    //根据cid单推消息
    public PushNoticeVo pushMessageToSingleCid(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        audience.addCid(bean.getUser());
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        System.out.println(apiResult);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    //批量根据cid推送消息
    private PushNoticeVo pushMessageToManyCid(PushNoticeDto bean){
        PushNoticeVo rb = new PushNoticeVo();
        PushDTO pushDTO=new PushDTO();
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        if (msg.isSuccess()){
            AudienceDTO audienceDTO=new AudienceDTO();
            audienceDTO.setTaskid(msg.getData().getTaskId());
            audienceDTO.setAsync(true);
            List<String> users = Arrays.asList(bean.getUser().split(","));
            Audience audience=new Audience();
            for (String user:users){
                audience.addCid(user);
            }
            audienceDTO.setAudience(audience);
            PushApi pushApi1 = myApiHelper.creatApi(PushApi.class);
            ApiResult<Map<String, Map<String, String>>> mapApiResult = pushApi1.pushListByCid(audienceDTO);
            rb.setCode(mapApiResult.getCode());
            rb.setMsg(mapApiResult.getMsg());
            rb.setData(mapApiResult.getData());
        }else {
            rb.setCode(msg.getCode());
            rb.setMsg(msg.getMsg());
            rb.setData(msg.getData());
        }
        return rb;
    }

    //根据别名单推消息
    private PushNoticeVo pushMessageToSingleAlias(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        audience.addAlias(bean.getUser());
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByAlias(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    //批量根据别名推送消息
    private PushNoticeVo pushMessageToManyAlias(PushNoticeDto bean){
        PushNoticeVo rb = new PushNoticeVo();
        PushDTO pushDTO=new PushDTO();
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        if (msg.isSuccess()){
            AudienceDTO audienceDTO=new AudienceDTO();
            audienceDTO.setTaskid(msg.getData().getTaskId());
            audienceDTO.setAsync(true);
            List<String> users = Arrays.asList(bean.getUser().split(","));
            Audience audience=new Audience();
            for (String user:users){
                audience.addAlias(user);
            }
            audienceDTO.setAudience(audience);
            PushApi pushApi1 = myApiHelper.creatApi(PushApi.class);
            ApiResult<Map<String, Map<String, String>>> mapApiResult = pushApi1.pushListByAlias(audienceDTO);
            rb.setCode(mapApiResult.getCode());
            rb.setMsg(mapApiResult.getMsg());
            rb.setData(mapApiResult.getData());
        }else {
            rb.setCode(msg.getCode());
            rb.setMsg(msg.getMsg());
            rb.setData(msg.getData());
        }
        return rb;
    }

    //根据标签推送消息
    public PushNoticeVo pushMessageToTag(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("m_title",bean.getTitle());
        jsonObject.addProperty("m_content",bean.getContent());
        pushMessage.setTransmission(jsonObject.toString());
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        Condition condition=new Condition();
        condition.setKey("custom_tag");
        List<String> tags=new ArrayList<>();
        if (bean.getUser().contains(",")){
            tags.addAll(Arrays.asList(bean.getUser().split(",")));
        }else {
            tags.add(bean.getUser());
        }
        Set<String> sets=new HashSet<>();
        for (String tag:tags){
            sets.add(tag);
        }
        condition.setValues(sets);
        condition.setOptType("and");
        audience.addCondition(condition);
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> apiResult = pushApi.pushByTag(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }


    /**
     * 群推通知
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushNoticeToAll(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<String> pushDTO = new PushDTO<String>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        pushDTO.setAudience("all");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> apiResult = pushApi.pushAll(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    /**
     * 根据cid推送通知(含批量)
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushNoticeToCid(PushNoticeDto bean){
        boolean isMany=bean.getUser().contains(",");
        if (isMany){
            return pushNoticeToManyCid(bean);
        }else {
            return pushNoticeToSingleCid(bean);
        }
    }

    /**
     * 根据别名推送通知(含批量)
     * @param bean 推送信息
     * @return 结果
     */
    public PushNoticeVo pushNoticeToAlias(PushNoticeDto bean){
        boolean isMany=bean.getUser().contains(",");
        if (isMany){
            return pushNoticeToManyAlias(bean);
        }else {
            return pushNoticeToSingleAlias(bean);
        }
    }

    //根据cid单推通知
    private PushNoticeVo pushNoticeToSingleCid(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        audience.addCid(bean.getUser());
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    //批量根据cid推送通知
    private PushNoticeVo pushNoticeToManyCid(PushNoticeDto bean) {
        PushNoticeVo rb = new PushNoticeVo();
        PushDTO pushDTO=new PushDTO();
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        if (msg.isSuccess()){
            AudienceDTO audienceDTO=new AudienceDTO();
            audienceDTO.setTaskid(msg.getData().getTaskId());
            audienceDTO.setAsync(true);
            List<String> users = Arrays.asList(bean.getUser().split(","));
            Audience audience=new Audience();
            for (String user:users){
                audience.addCid(user);
            }
            audienceDTO.setAudience(audience);
            PushApi pushApi1 = myApiHelper.creatApi(PushApi.class);
            ApiResult<Map<String, Map<String, String>>> mapApiResult = pushApi1.pushListByCid(audienceDTO);
            rb.setCode(mapApiResult.getCode());
            rb.setMsg(mapApiResult.getMsg());
            rb.setData(mapApiResult.getData());
        }else {
            rb.setCode(msg.getCode());
            rb.setMsg(msg.getMsg());
            rb.setData(msg.getData());
        }
        return rb;
    }

    //根据别名单推通知
    private PushNoticeVo pushNoticeToSingleAlias(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        audience.addAlias(bean.getUser());
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByAlias(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }

    //批量根据别名推送通知
    private PushNoticeVo pushNoticeToManyAlias(PushNoticeDto bean) {
        PushNoticeVo rb = new PushNoticeVo();
        PushDTO pushDTO=new PushDTO();
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        if (msg.isSuccess()){
            AudienceDTO audienceDTO=new AudienceDTO();
            audienceDTO.setTaskid(msg.getData().getTaskId());
            audienceDTO.setAsync(true);
            List<String> users = Arrays.asList(bean.getUser().split(","));
            Audience audience=new Audience();
            for (String user:users){
                audience.addAlias(user);
            }
            audienceDTO.setAudience(audience);
            PushApi pushApi1 = myApiHelper.creatApi(PushApi.class);
            ApiResult<Map<String, Map<String, String>>> mapApiResult = pushApi1.pushListByAlias(audienceDTO);
            rb.setCode(mapApiResult.getCode());
            rb.setMsg(mapApiResult.getMsg());
            rb.setData(mapApiResult.getData());
        }else {
            rb.setCode(msg.getCode());
            rb.setMsg(msg.getMsg());
            rb.setData(msg.getData());
        }
        return rb;
    }

    //根据标签推送通知
    public PushNoticeVo pushNoticeToTag(PushNoticeDto bean){
        PushNoticeVo rb=new PushNoticeVo();
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        notification.setTitle(bean.getTitle());
        notification.setBody(bean.getContent());
        notification.setClickType("none");
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);
        Audience audience = new Audience();
        Condition condition=new Condition();
        condition.setKey("custom_tag");
        List<String> tags=new ArrayList<>();
        if (bean.getUser().contains(",")){
            tags.addAll(Arrays.asList(bean.getUser().split(",")));
        }else {
            tags.add(bean.getUser());
        }
        Set<String> sets=new HashSet<>();
        for (String tag:tags){
            sets.add(tag);
        }
        condition.setValues(sets);
        condition.setOptType("and");
        audience.addCondition(condition);
        pushDTO.setAudience(audience);
        PushApi pushApi = myApiHelper.creatApi(PushApi.class);
        ApiResult<TaskIdDTO> apiResult = pushApi.pushByTag(pushDTO);
        rb.setCode(apiResult.getCode());
        rb.setMsg(apiResult.getMsg());
        rb.setData(apiResult.getData());
        return rb;
    }


}