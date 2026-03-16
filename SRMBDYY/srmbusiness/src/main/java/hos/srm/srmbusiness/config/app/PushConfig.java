package hos.srm.srmbusiness.config.app;

import org.springframework.context.annotation.Configuration;

import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

//@Configuration
public class PushConfig {

//    @Value("https://restapi.getui.com/v2")
//    private String baseUrl;

    @Value("${uniPush.appId}")
    private String appId;

    @Value("${uniPush.appKey}")
    private String appKey;

    @Value("${uniPush.masterSecret}")
    private String masterSecret;

    @Bean(name = "myApiHelper")
    public ApiHelper apiHelper() {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(appId);
        apiConfiguration.setAppKey(appKey);
        apiConfiguration.setMasterSecret(masterSecret);
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        //默认为https://restapi.getui.com/v2
        //apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        return apiHelper;
    }
}
