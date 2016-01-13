<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TenderParser</title>
        <link href="css/style_index.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div class="catalog">
            <ul>
                <li>Services <i class="fa fa-angle-down"></i>
                    <ul>
                        <li><a href="<%=request.getContextPath()%>/start">Start</a></li>
                        <li><a href="<%=request.getContextPath()%>/save">Compare and Send</a></li>
                        <li><a href="<%=request.getContextPath()%>/view">View in DataBase</a></li>
                    </ul>
                </li>
<!--                    <li>Regex <i class="fa fa-angle-down"></i>
                <ul>
                        <li>Choice pattern date <span>...</span></li>
                    </ul>
                </li>-->
            </ul>                
            <div class="slider">
                <%int count=1;%>                                                 
                <c:forEach items="${tend}" var="tdr">                       
                    <p>            
                       <br>=============== <%=count%> ==================
                       <br>1) ${tdr.getEmail().getKeyword()} 
                       <br>2) ${tdr.getIdTender()} 
                       <br>3) ${tdr.getCompanyTender()}                       
                       <br>4) ${tdr.getNameTender()}                       
                       <br>5) ${tdr.getDeadlineTender()}                      
                       <br>6) ${tdr.getCostTender()}                       
                       <br>7) <a href="${tdr.getUrlTender()}" target="_blank">${tdr.getUrlTender()}</a> 
                       <br>=============== <%=count++%> ==================
                    </p>    
                </c:forEach>                                                                                                  
                </div>
            <div class="information">
                <h1>Парсер тендеров</h1>
                <h3>Проект находится в разработке, поэтому дизайн и функционал будет дорабатываться.</h3>
                <p>© 2016 ICL. Test project</p>
            </div>
        </div>
    </body>
</html>
