package gwk.burlap;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-5-6
 * Time: 11:00:40
 * To change this template use File | Settings | File Templates.
 */
public interface IElement {

    /*
        <burlap:call>
        <method>queryAllElementCode</method>
        <!—业务系统标示 -->
        <string>applicationid</string>
        <!— 要查询的基础数据编码 -->
        <string>ElementCode</string>
        <!—年度 -->                                                 
        <int>year</int>
        </burlap:call>
         ===========
        <burlap:reply>
        <list>
        <map>
        <type></type>
        <string>id</string><string>value</string>
        <string>code</string><string>value</string>
        <string>guid</string><string>value</string>
        <string>supercode</string><string>value</string>
        <string>name</string><string>value</string>
        </map>
        <!-- … -->
        <map>
        <!— 数据最新版本号 -->
        <string>version</string><int>value</int>
        </map>
        </list>
        </burlap:reply>
     */
    public List queryAllElementCode(String applicationid,String ElementCode,int year) throws UnsupportedEncodingException;
        /*
            <burlap:call>
            <method>queryElementVersion</method>
            <!—业务系统标示 -->
            <string>applicationid</string>
            <!—年度 -->
            <int>year</int>
            <list>
              <!--  需要查询的要素编码列表  -->
            <string>elementcode</string>
            <string>elementcode</string>
            <!-- … -->
            </list>
            </burlap:call>

            <burlap:reply>
            <map>
            <type></type>
            <!—要素编码、最新版本号  -->
            <string>elementcode</string><int>value</int>
            <string>elementcode</string><int>value</int>
            <!-- … -->
            </map>
            </burlap:reply>
     */
    public Map queryElementVersion(String applicationid,int year,List elements);


    /*
        <burlap:call>
        <method>syncElementCode</method>
        <!—业务系统标示 -->
        <string>applicationid</string>
        <!—年度 -->
        <int>year</int>
        <map>
        <type></type>
        <!—同步要素编码--key --><!—同步要素版本--value -->
        <string>key</string><int>value</int>
        <!-- … -->
        </map>
        </burlap:call>

        <burlap:reply>
        <list>
        <type></type>
        <length>1</length>
          <!--  表示一个需要同步的基础信息要素 -->
        <map>
        <type></type>
        <!—同步要素编码 -->
        <string>elementcode</string><string>value</string>
        <!—要素最终版本 -->
        <string>version</string><int>value</int>
        <string>datas</string>
        <list>
          <type></type>
          <length>1</length>
        <map>
        <type></type>
        <!--  元素变动类型：0-新增 1-修改 2-删除  -->
        <string>action</string><string>value</string>
        <string>code</string><string>value</string>
        <string>name</string><string>value</string>
        <string>guid</string><string> value </string>
        <!-- … -->
        </map>
        <!-- … -->
        </list>
        </map>                                                            
        </list>
        </burlap:reply>

     */
    public List syncElementCode(String applicationid,int year, Map element);



}