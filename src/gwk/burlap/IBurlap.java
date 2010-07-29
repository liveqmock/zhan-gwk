package gwk.burlap;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-5-6                                               
 * Time: 11:00:40
 * To change this template use File | Settings | File Templates.
 */
public interface IBurlap {
    public List queryAllElementCode(String applicationid,String ElementCode,int year);

    /*
        <burlap:call>
        <method> getOfficeCardStatus </method>
        <!��ҵ��ϵͳ��ʾ -->
        <string>applicationid</string>
        <!�����б��� -->
        <string>BANK</string>
        <!�����-->
        <string>year </string>
        <!���������� -->
        <string>FinOrgCode</string>
        <!������ϵͳ�����յĹ���ƾ֤������GUID��˳���룩-->
        <string> guid </string>
        </burlap:call>
        ======
        <burlap:reply>
            <list>
        <map>
        <!- ���񿨿���-->
        <string> ACCOUNT </string><string>value</string>
        <!- �����ƿ���-->
        <string> CARDNAME </string><string>value</string>
        <!������״̬-->
        <string> STATUS </string><string>value</string>
        <!��Ԥ�㵥λ-->
        <string> BDGAGENCY</string><string>value</string>
        <!��guid-->
        <string> GUID </string><string>value</string>
        </map>
        <!-- �� -->
        </list
        </burlap:reply>
     */
    public List  getOfficeCardStatus(String applicationid,String BANK,String year,String guid);
}
