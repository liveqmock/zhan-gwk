<%--
  Created by IntelliJ IDEA.
  User: gwk
  Date: 2010-8-4
  Time: 16:58:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=GBK" %>
<%@include file="/global.jsp"%>
<%@ page import="pub.platform.db.*"%>
<html>
  <head>
      <title>
          ������Ϣ
      </title>
      <script language="javascript" src="cardinfoquery.js"></script>
      <%
           String strSql =  " select cbinfo.account,cbinfo.cardname,bgcy.name as bgcyname," +
                   " cbinfo.gatheringbankacctname,cbinfo.gatheringbankacctcode," +
                   " cbinfo.idnumber,cbinfo.digest,cbinfo.createdate,cbinfo.startdate," +
                   " cbinfo.enddate,cbinfo.sentflag,cbinfo.status" +
                   " from ls_cardbaseinfo cbinfo left join ls_bdgagency bgcy on cbinfo.bdgagency=bgcy.code" +
                   " where 1=1 ";
       DBGrid dbGrid = new DBGrid();
       dbGrid.setGridID("cardInfoTab");
       dbGrid.setGridType("edit");
       dbGrid.setfieldSQL(strSql);
       dbGrid.setWhereStr(" order by cbinfo.account");
       dbGrid.setField("����","text","12","account","true","0");
       dbGrid.setField("�ֿ���","text","6","cardname","true","0");
       dbGrid.setField("Ԥ�㵥λ","text","14","bgcyname","true","0");
       dbGrid.setField("�����ʻ���","text","13","gatheringbankacctname","true","0");
       dbGrid.setField("���ʻ���","text","14","gatheringbankacctcode","true","0");
       dbGrid.setField("���֤��","text","12","idnumber","true","0");
       dbGrid.setField("��;","text","10","digest","true","0");
       dbGrid.setField("��������","text","6","createdate","true","0");
       dbGrid.setField("��Ч��ʼ����","text","9","startdate","true","0");
       dbGrid.setField("��Ч��ֹ����","text","9","enddate","true","0");
       dbGrid.setField("�Ƿ���","dropdown","8","sentflag","true","CARDSENDFLAG");
       dbGrid.setField("��״̬","dropdown","8","status","true","CARDSTATUS");
//       dbGrid.setfieldcn("����,�ֿ���,Ԥ�㵥λ,�����ʻ���,���ʻ���,���֤��,��;,��������,��Ч��ʼ����,��Ч��ֹ����," +
//               "");
       dbGrid.setpagesize(50);
       dbGrid.setCheck(false);
        //////���ݼ���ť
       dbGrid.setdataPilotID("datapilot");
       dbGrid.setbuttons("����Excel=excel,moveFirst,prevPage,nextPage,moveLast");
      %>
  </head>
  <body onLoad="body_resize() " onResize="body_resize()" class="Bodydefault">
  <fieldset>
      <legend>
        ��ѯ����
      </legend>
      <table border="0" cellspacing="0" cellpadding="0" width="100%" >
        <form id="queryForm" name="queryForm">
        <tr height="20">
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              ����
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input" >
                <input style="width:90%" type="text" id="cardNo" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              �ֿ�������
            </td>
            <td width="15%" align="right" nowrap="nowrap" class="data_input" >
                 <input style="width:90%" type="text" id="cust_name" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
              Ԥ�㵥λ
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
                <input type="text" style="width:90%;" id="departmentName" name="departmentName"
                       value="" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td align="center">
                <input name="cbRetrieve" type="button" class="buttonGrooveDisable" id="button" onClick="cbRetrieve_Click()" value="����">
            </td>
        </tr>
        <tr height="20">
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                ���֤ID
            </td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
               <input type="text" style="width:90%;" id="personalID" name="personalID" size="40" class="ajax-suggestion url-getLoanPull.jsp">
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">
                ��״̬
            </td>
            <td width="15%" align="right" nowrap="nowrap" class="data_input">
                <%
                    ZtSelect zs = new ZtSelect("status", "CARDSTATUS", "");
                    zs.addAttr("style", "width: 91%");
                    zs.addAttr("fieldType", "text");
                    zs.addOption("", "");
                    out.print(zs);
                %>
            </td>
            <td width="10%" align="right" nowrap="nowrap" class="lbl_right_padding">�Ƿ���</td>
            <td width="20%" align="right" nowrap="nowrap" class="data_input">
                <%
                    ZtSelect zs1 = new ZtSelect("sentflag", "CARDSENDFLAG", "");
                    zs1.addAttr("style", "width: 91%");
                    zs1.addAttr("fieldType", "text");
                    zs1.addOption("", "");
                    out.print(zs1);
                %>
            </td>
            <td  align="center" nowrap="nowrap">
                <input name="Input" class="buttonGrooveDisable" type="reset" value="����" >
            </td>
        </tr>
       </form>
      </table>
    </fieldset>
    <fieldset>
      <legend>
        ������Ϣ
      </legend>
      <table width="100%">
        <tr>
          <td>
            <%=dbGrid.getDBGrid()%>
          </td>
        </tr>
      </table>
    </fieldset>
    <FIELDSET>
      <LEGEND>
        ����
      </LEGEND>
      <table width="100%" class="title1">
        <tr>
          <td id="cellButtons" align="right">
            <%=dbGrid.getDataPilot()%>
          </td>
        </tr>
      </table>
    </FIELDSET>

    <div id="search-result-suggestions">
      <div id="search-results">
      </div>
     </div>
  </body>
</html>