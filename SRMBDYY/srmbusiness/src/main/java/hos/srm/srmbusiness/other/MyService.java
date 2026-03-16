package hos.srm.srmbusiness.other;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service("myService")
public class MyService {
    public String test(){
        System.out.println("hosc");
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        //Client client = clientFactory.createClient("http://127.0.0.1:8080/soap/user?wsdl");
        AddSoapHeader addSoapHeader = new AddSoapHeader();
        addSoapHeader.setUserName("dhwebservice");
        addSoapHeader.setPassWord("1wMu21i$9E");
        //addSoapHeader.setUserName("dhwebservice");
        //addSoapHeader.setPassWord("1wMu21i$9E");
        String res="";
        try {
            Client client = clientFactory.createClient("https://58.56.200.231:2443/imedical/webservice/HRP.SRMAPP.Service.CLS?WSDL=1&CacheUserName=dhwebservice&CachePassword=1wMu21i$9E&CacheNoRedirect=1");
            //http://127.0.0.1/imedical/web/web.DHCSTMService.HRP.In2OutService.CLS?WSDL=1
            //Client client = clientFactory.createClient("http://localhost:57772/imedical/web/HRP.SRMAPP.Service.cls?WSDL=1&CacheUserName=_system&CachePassword=SYS&CacheNoRedirect=1");

            //http://localhost:57772/imedical/web/HRP.SRMAPP.Service.cls?WSDL=1
            client.getOutInterceptors().add(addSoapHeader);
            Object[] objects = client.invoke("Test","111");
            res=objects[0].toString();
            System.out.println(res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public String test22(String path){
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        //Client client = clientFactory.createClient("http://127.0.0.1:8080/soap/user?wsdl");
        AddSoapHeader addSoapHeader = new AddSoapHeader();
        addSoapHeader.setUserName("_system");
        addSoapHeader.setPassWord("SYS");
        InputStream resinputStream;
        try {
            Client client = clientFactory.createClient("http://127.0.0.1/imedical/web/web.DHCSTMService.HRP.In2OutService.CLS?WSDL=1&CacheUserName=_system&CachePassword=SYS&CacheNoRedirect=1");
            //http://127.0.0.1/imedical/web/web.DHCSTMService.HRP.In2OutService.CLS?WSDL=1
            client.getOutInterceptors().add(addSoapHeader);
            Object[] objects = client.invoke("TesAA",path);
            System.out.println(objects[0].toString());
            //System.out.println(objects[0].toString());
            /*
            resinputStream = (InputStream) objects[0];
            ///然后保存呢
            File convFile = null;
            convFile = new File("测试.pdf");
            FileOutputStream fos = new FileOutputStream(convFile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = resinputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();

             */

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "0";
    }

}
