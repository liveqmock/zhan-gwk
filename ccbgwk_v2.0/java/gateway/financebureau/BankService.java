package gateway.financebureau;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-1-5
 * Time: ����9:38
 * To change this template use File | Settings | File Templates.
 */
// /remoting/service/bankservice
public interface BankService {
    List writeOfficeCard(String applicationid, String bankcode, String year, String fanCode, List cardList);
    //�µĽӿڷ��Ϳ���Ϣ�����������������루admdiv�� 2012-10-29
    List writeOfficeCard(String applicationid, String bankcode, String year,String admdiv, String fanCode, List cardList);
    List writeConsumeInfo(String applicationid, String bankcode, String year, String fanCode, List cardList);
    //�µĽӿڷ���������Ϣ�����������������루admdiv�� 2012-10-29
    List writeConsumeInfo(String applicationid, String bankcode, String year, String admdiv, String fanCode, List cardList);
    List getOfficeCardStatus(String applicationid, String bankcode, String year, String fanCode, String guid);
    //�µĽӿڣ�����֧�����ѯ������Ϣ�� 2012-10-29
    List queryVoucherByBillCode(String applicationid, String bankcode, String year, String admdiv, String fanCode,
                                  String billtype,String billcode );
}
