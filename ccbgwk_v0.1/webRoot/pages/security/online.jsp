
<%pub.platform.security.OperatorManager _om = (pub.platform.security.OperatorManager)session.getAttribute(pub.platform.form.config.SystemAttributeNames.USER_INFO_NAME);
  if ( _om == null ) {
      out.println("<script language=\"javascript\">alert ('����Ա��ʱ��������ǩ����'); if(top){ top.location.href='/pages/security/loginPage.jsp'; } else { location.href = '/pages/security/loginPage.jsp';} </script>");
//      out.println("<script language=\"javascript\">alert ('����Ա��ʱ��������ǩ����'); if(top){ top.location.href='/login.jsp'; } else { location.href = '/login.jsp';} </script>");
      return;
  }%>
