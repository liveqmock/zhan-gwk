<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*" %>
<%@ page import="pub.platform.form.config.SystemAttributeNames" %>
<%@ page import="pub.platform.security.OperatorManager" %>
<%@ page import="pub.platform.security.OnLineOpersManager" %>

<%
	OperatorManager om = (OperatorManager)session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

	if ( om == null ) {

		om = new OperatorManager();
		session.setAttribute(SystemAttributeNames.USER_INFO_NAME,om);
	}

	String username = request.getParameter("username");
	String password = request.getParameter("password");
	//String imgsign = request.getParameter("imgsign");
	boolean isLogin = false;


	try {
		//if(!om.ImgSign(imgsign))
		//	out.println("<script language=\"javascript\">alert ('输入校验码有误！'); if(top){ top.location.href='/index.jsp'; } else { location.href = '/index.jsp';} </script>");
		om.setRemoteAddr(request.getRemoteAddr());
	    om.setRemoteHost(request.getRemoteHost());
		isLogin = om.login(username, password);
		if ( ! isLogin ) {
		    out.println("<script language=\"javascript\">alert ('输入用户名或密码有误！'); if(top){ top.location.href='/pages/security/loginPage.jsp'; } else { location.href = '/pages/security/loginPage.jsp';} </script>");
	    }  else {
            if (!OnLineOpersManager.isHasUserList(application)) {
                OnLineOpersManager.setUserListToServer(application);
                OnLineOpersManager.addOperToServer(session.getId() + username, om, application);
            } else {
                OnLineOpersManager.addOperToServer(session.getId() + username, om, application);
            }
        }
	} catch ( Exception e ) {
	     e.printStackTrace();
	}
%>

