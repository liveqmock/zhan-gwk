package gateway.sbs;

/**
 * �������۲�ѯ�����Ӧ��ʧ�ܼ�¼��
 * User: zhanrui
 * Date: 2009-7-6
 * Time: 15:44:45
 * To change this template use File | Settings | File Templates.
 */
public class BalanceRecord {

/*
    T839-CUSIDT	�˺�	  X(18)
    T839-APCODE	����	  X(60)
    T839-LASBAL	���	  X(18)
    T839-CURCDE	�˻����	X(1)
*/

    String CUSIDT;
    String APCODE;
    String LASBAL;
    String CURCDE;

    public String getCUSIDT() {
        return CUSIDT;
    }

    public void setCUSIDT(String CUSIDT) {
        this.CUSIDT = CUSIDT;
    }

    public String getAPCODE() {
        return APCODE;
    }

    public void setAPCODE(String APCODE) {
        this.APCODE = APCODE;
    }

    public String getLASBAL() {
        return LASBAL;
    }

    public void setLASBAL(String LASBAL) {
        this.LASBAL = LASBAL;
    }

    public String getCURCDE() {
        return CURCDE;
    }

    public void setCURCDE(String CURCDE) {
        this.CURCDE = CURCDE;
    }


}