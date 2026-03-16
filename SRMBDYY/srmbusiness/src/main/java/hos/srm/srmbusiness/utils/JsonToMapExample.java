package hos.srm.srmbusiness.utils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonToMapExample {
    public static void main(String[] args) {
        // 示例JSON字符串
        String jsonString = "{\"1\":\"{\\\"fileName\\\":\\\"1.jpg\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000001.jpg\\\"}\",\"2\":\"{\\\"fileName\\\":\\\"2.jpg\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000002.jpg\\\"}\",\"3\":\"{\\\"fileName\\\":\\\"3.jpg\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000003.jpg\\\"}\",\"4\":\"{\\\"fileName\\\":\\\"0710.xlsx\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000004.xlsx\\\"}\",\"5\":\"{\\\"fileName\\\":\\\"科技成果登记表.pdf\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000005.pdf\\\"}\",\"6\":\"{\\\"fileName\\\":\\\"科技成果登记服务指南.pdf\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000006.pdf\\\"}\",\"7\":\"{\\\"fileName\\\":\\\"科技成果转化服务手册.pdf\\\",\\\"fileUrl\\\":\\\"/srm/2025/07/11/673970000000007.pdf\\\"}\"}";

        try {
            Map<String, String> resMap = JSON.parseObject(jsonString, HashMap.class);
            //遍历存放所有key的Set集合
            Set<String> keySet = resMap.keySet();
            Iterator<String> it =keySet.iterator();

            while(it.hasNext()) {
                String key = it.next();
                String item=resMap.get(key);
                Map<String, String> itemMap = JSON.parseObject(item, HashMap.class);
                String fileName=itemMap.get("fileName");
                String fileUrl=itemMap.get("fileUrl");
                System.out.println("Gson方法结果: " + fileName+","+fileUrl);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
