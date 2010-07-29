package com.ccb.datatrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-3-20
 * Time: 16:07:03
 * To change this template use File | Settings | File Templates.
 */
public class MortResultBean {

    int lineno;
    String name;
    double amt;
    String bankid;
    String loanid;
    String mortdate;
    String receiptdate;
    String mortexpiredate;
    String mortoverrtndate;

    int term;
    String type;


    List<String> loanidsA = new ArrayList<String>(); //ͬ��
    List<String> loanidsB = new ArrayList<String>(); //ͬ��ͬ����
    List<String> loanidsC = new ArrayList<String>(); //ͬ��ͬ����ͬ���

    List<String> loanids = new ArrayList<String>(); 
    List<String> loandates = new ArrayList<String>(); //������������
    List<Integer> loanterms = new ArrayList<Integer>(); //��������
    List<String> loantypes = new ArrayList<String>(); //��������


    int repeatname;  //����
    int repeatname_bank;  //ͬ��������

    int repeatname_bank_amt;  //ͬ����ͬ�������
    int repeatname_bank_type;  //ͬ�������� ��ͬ ����������ͬ
    int repeatname_bank_term;  //ͬ�������� ��ͬ ����������ͬ

    int repeatname_bank_amt_type;  //ͬ����ͬ������� ͬ��������
    int repeatname_bank_amt_term;  //ͬ����ͬ������� ͬ��������
    int repeatname_bank_amt_date;  //ͬ����ͬ������� ͬ��������
    int repeatname_bank_amt_state;  //ͬ����ͬ������� ͬ��������
    int repeatname_bank_amt_type_term;  //ͬ����ͬ������� ͬ�������� ͬ��������

    int foundGuaramtNum; //�ҵ���ͬ�������ֵ����

    private double guarAmt;

    public   MortResultBean(int lineno,String name,double amt,String bankid, String loanid,
                            int i1,int i2,int i3){
        this.lineno = lineno;
        this.name = name;
        this.amt = amt;
        this.bankid = bankid;
        this.loanid = loanid;

        this.repeatname = i1;
        this.repeatname_bank = i2;
        this.repeatname_bank_amt = i3;

    }
    public   MortResultBean(int lineno,String name,double amt,String bankid, String loanid,
                            double guarAmt,
                            String mortdate,
                            int termMort,
                            String typeMort,
                            String receiptdate,
                            int i1,int i2,int i3){
        this.lineno = lineno;
        this.name = name;
        this.amt = amt;
        this.bankid = bankid;
        this.loanid = loanid;
        this.guarAmt=guarAmt;
        this.mortdate=mortdate;
        this.term =termMort;
        this.type =typeMort;
        this.receiptdate =receiptdate;

        this.repeatname = i1;
        this.repeatname_bank = i2;
        this.repeatname_bank_amt = i3;

    }


    public void addLoanidA(String loanid){
        this.loanidsA.add(loanid);
    }
    public void addLoanidB(String loanid){
        this.loanidsB.add(loanid);
    }
    public void addLoanidC(String loanid){
        this.loanidsC.add(loanid);
    }

    public void addLoanids(String loanid){
        this.loanids.add(loanid);
    }
    public void addLoandates(String loandate){
        this.loandates.add(loandate);
    }
    public void addLoanterms(int loanterm){
        this.loanterms.add(loanterm);
    }
    public void addLoantypes(String loantype){
        this.loantypes.add(loantype);
    }



    public List<String> getLoanidsA() {
        return loanidsA;
    }
    public List<String> getLoanidsB() {
        return loanidsB;
    }
    public List<String> getLoanidsC() {
        return loanidsC;
    }
    public List<String> getLoanids() {
        return loanids;
    }

    public List<String> getLoandates() {
        return loandates;
    }
    public List<String> getLoantypes() {
        return loantypes;
    }
    public List<Integer> getLoanterms() {
        return loanterms;
    }

    public String getMortdate() {
        return mortdate;
    }

    public void setMortdate(String mortdate) {
        this.mortdate = mortdate;
    }

    public int getLineno() {
        return lineno;
    }

    public void setLineno(int lineno) {
        this.lineno = lineno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public int getRepeatname() {
        return repeatname;
    }

    public void setRepeatname(int repeatname) {
        this.repeatname = repeatname;
    }

    public int getRepeatname_bank() {
        return repeatname_bank;
    }

    public void setRepeatname_bank(int repeatname_bank) {
        this.repeatname_bank = repeatname_bank;
    }

    public int getRepeatname_bank_amt() {
        return repeatname_bank_amt;
    }

    public void setRepeatname_bank_amt(int repeatname_bank_amt) {
        this.repeatname_bank_amt = repeatname_bank_amt;
    }

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public double getGuarAmt() {
        return guarAmt;
    }

    public void setGuarAmt(double guarAmt) {
        this.guarAmt = guarAmt;
    }

    public int getFoundGuaramtNum() {
        return foundGuaramtNum;
    }

    public void setFoundGuaramtNum(int foundGuaramtNum) {
        this.foundGuaramtNum = foundGuaramtNum;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRepeatname_bank_type() {
        return repeatname_bank_type;
    }

    public void setRepeatname_bank_type(int repeatname_bank_type) {
        this.repeatname_bank_type = repeatname_bank_type;
    }

    public int getRepeatname_bank_term() {
        return repeatname_bank_term;
    }

    public void setRepeatname_bank_term(int repeatname_bank_term) {
        this.repeatname_bank_term = repeatname_bank_term;
    }

    public int getRepeatname_bank_amt_type() {
        return repeatname_bank_amt_type;
    }

    public void setRepeatname_bank_amt_type(int repeatname_bank_amt_type) {
        this.repeatname_bank_amt_type = repeatname_bank_amt_type;
    }

    public int getRepeatname_bank_amt_term() {
        return repeatname_bank_amt_term;
    }

    public void setRepeatname_bank_amt_term(int repeatname_bank_amt_term) {
        this.repeatname_bank_amt_term = repeatname_bank_amt_term;
    }

    public int getRepeatname_bank_amt_type_term() {
        return repeatname_bank_amt_type_term;
    }

    public void setRepeatname_bank_amt_type_term(int repeatname_bank_amt_type_term) {
        this.repeatname_bank_amt_type_term = repeatname_bank_amt_type_term;
    }

    public int getRepeatname_bank_amt_date() {
        return repeatname_bank_amt_date;
    }

    public void setRepeatname_bank_amt_date(int repeatname_bank_amt_date) {
        this.repeatname_bank_amt_date = repeatname_bank_amt_date;
    }

    public int getRepeatname_bank_amt_state() {
        return repeatname_bank_amt_state;
    }

    public void setRepeatname_bank_amt_state(int repeatname_bank_amt_state) {
        this.repeatname_bank_amt_state = repeatname_bank_amt_state;
    }

    public String getReceiptdate() {
        return receiptdate;
    }

    public void setReceiptdate(String receiptdate) {
        this.receiptdate = receiptdate;
    }

    public String getMortexpiredate() {
        return mortexpiredate;
    }

    public void setMortexpiredate(String mortexpiredate) {
        this.mortexpiredate = mortexpiredate;
    }

    public String getMortoverrtndate() {
        return mortoverrtndate;
    }

    public void setMortoverrtndate(String mortoverrtndate) {
        this.mortoverrtndate = mortoverrtndate;
    }
}
