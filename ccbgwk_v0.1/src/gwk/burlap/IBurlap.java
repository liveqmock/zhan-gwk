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
        <!—业务系统标示 -->
        <string>applicationid</string>
        <!—银行编码 -->
        <string>BANK</string>
        <!—年度-->
        <string>year </string>
        <!—财政机构 -->
        <string>FinOrgCode</string>
        <!—银行系统所接收的公务卡凭证中最大的GUID（顺序码）-->
        <string> guid </string>
        </burlap:call>
        ======
        <burlap:reply>
            <list>
        <map>
        <!- 公务卡卡号-->
        <string> ACCOUNT </string><string>value</string>
        <!- 公务卡制卡人-->
        <string> CARDNAME </string><string>value</string>
        <!—公务卡状态-->
        <string> STATUS </string><string>value</string>
        <!—预算单位-->
        <string> BDGAGENCY</string><string>value</string>
        <!—guid-->
        <string> GUID </string><string>value</string>
        </map>
        <!-- … -->
        </list
        </burlap:reply>
     */
    public List  getOfficeCardStatus(String applicationid,String BANK,String year,String guid);
}
