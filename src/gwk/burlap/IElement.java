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
        <!��ҵ��ϵͳ��ʾ -->
        <string>applicationid</string>
        <!�� Ҫ��ѯ�Ļ������ݱ��� -->
        <string>ElementCode</string>
        <!����� -->                                                 
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
        <!-- �� -->
        <map>
        <!�� �������°汾�� -->
        <string>version</string><int>value</int>
        </map>
        </list>
        </burlap:reply>
     */
    public List queryAllElementCode(String applicationid,String ElementCode,int year) throws UnsupportedEncodingException;
        /*
            <burlap:call>
            <method>queryElementVersion</method>
            <!��ҵ��ϵͳ��ʾ -->
            <string>applicationid</string>
            <!����� -->
            <int>year</int>
            <list>
              <!--  ��Ҫ��ѯ��Ҫ�ر����б�  -->
            <string>elementcode</string>
            <string>elementcode</string>
            <!-- �� -->
            </list>
            </burlap:call>

            <burlap:reply>
            <map>
            <type></type>
            <!��Ҫ�ر��롢���°汾��  -->
            <string>elementcode</string><int>value</int>
            <string>elementcode</string><int>value</int>
            <!-- �� -->
            </map>
            </burlap:reply>
     */
    public Map queryElementVersion(String applicationid,int year,List elements);


    /*
        <burlap:call>
        <method>syncElementCode</method>
        <!��ҵ��ϵͳ��ʾ -->
        <string>applicationid</string>
        <!����� -->
        <int>year</int>
        <map>
        <type></type>
        <!��ͬ��Ҫ�ر���--key --><!��ͬ��Ҫ�ذ汾--value -->
        <string>key</string><int>value</int>
        <!-- �� -->
        </map>
        </burlap:call>

        <burlap:reply>
        <list>
        <type></type>
        <length>1</length>
          <!--  ��ʾһ����Ҫͬ���Ļ�����ϢҪ�� -->
        <map>
        <type></type>
        <!��ͬ��Ҫ�ر��� -->
        <string>elementcode</string><string>value</string>
        <!��Ҫ�����հ汾 -->
        <string>version</string><int>value</int>
        <string>datas</string>
        <list>
          <type></type>
          <length>1</length>
        <map>
        <type></type>
        <!--  Ԫ�ر䶯���ͣ�0-���� 1-�޸� 2-ɾ��  -->
        <string>action</string><string>value</string>
        <string>code</string><string>value</string>
        <string>name</string><string>value</string>
        <string>guid</string><string> value </string>
        <!-- �� -->
        </map>
        <!-- �� -->
        </list>
        </map>                                                            
        </list>
        </burlap:reply>

     */
    public List syncElementCode(String applicationid,int year, Map element);



}