<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>首页</title>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
</head>
<body>
<%--设置--%>
<%@ include file="/WEB-INF/views/include/settings.jsp"%>

<div class="page-container">
    <%--菜单--%>
    <%@ include file="/WEB-INF/views/include/sidebar_menu.jsp"%>
    <%--主体单页内容 --%>
    <div class="main-content">
        <%@ include file="/WEB-INF/views/include/navi.jsp"%>
        <%@ include file="/WEB-INF/views/include/footer.jsp"%>
    </div>
</div>
</body>
</html>
