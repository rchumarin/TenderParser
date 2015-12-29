<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TenderParser</title>
        <link href="css/style_index.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div class="container">
            <div class="catalog">
                <ul>
                    <li>Services <i class="fa fa-angle-down"></i>
                        <ul>
                            <li>Start parser</li>
                            <li>Send email</li>
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
                           <br>1) ${tdr.getIdTender()} 
                           <br>2) ${tdr.getCompanyTender()}                       
                           <br>3) ${tdr.getNameTender()}                       
                           <br>4) ${tdr.getDeadlineTender()}                      
                           <br>5) ${tdr.getCostTender()}                       
                           <br>6) <a href="${tdr.getUrlTender()}">${tdr.getUrlTender()}</a> 
                           <br>=============== <%=count++%> ==================
                        </p>    
                    </c:forEach>                                  
                </div>
                <div class="information">
                    <!--<h1>Example h1</h1>-->
                    <h3>Проект находится в разработке, поэтому дизайн и функционал будет дорабатываться.</h3>
                    <p>© 2015 ICL. Test project</p>
                </div>
<!--                <div class="footer">
                Разработчик: Рафаэль Чумарин, 2015 г
                </div>   -->
            </div>
        </div>
    </body>
</html>
