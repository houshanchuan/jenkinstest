package hos.srm.srmbusiness.other;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

public class AddSoapHeader extends AbstractSoapInterceptor {
    private String userName;
    private String passWord;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public AddSoapHeader() {
        super(Phase.WRITE);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        List<Header> headers = message.getHeaders();
        // 创建Document对象 创建根节点
        Document doc = DOMUtils.createDocument();
        Element Security = doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security");
        Security.setPrefix("wsse");
        // 配置服务器端Head信息的用户密码
        Element eleId = doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Username");
        eleId.setPrefix("wsse");
        eleId.setTextContent(this.userName);
        Element elePass = doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Password");
        elePass.setPrefix("wsse");
        elePass.setTextContent(this.passWord);
        Element UsernameToken = doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","UsernameToken");
        UsernameToken.setPrefix("wsse");
        UsernameToken.appendChild(eleId);
        UsernameToken.appendChild(elePass);
        Security.appendChild(UsernameToken);
        headers.add(new Header(new QName(""), Security));
    }
}
