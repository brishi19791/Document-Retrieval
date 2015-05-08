<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css" />

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">

<div class="page-header">
    
<!-- Contact Form - START -->
<div class="container">

    <div class="row">
        <div class="col-md-12">
            <div class="well well-sm">
           
                <form class="form-horizontal" action="search" method="post">
                    <fieldset>
                    
                        <legend class="text-center header">Document Retrieval</legend>
                    
                       <div class="form-group">
                            <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                            <div class="col-md-8">
                              <input id="search" name="search" type="text" placeholder="Search" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-12 text-center">
                                <button type="submit" class="btn btn-primary btn-lg">Search</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
                
                <table border="1" width="700">
         <tr>
         <th align="center">Rank</th>
         <th align="center">FileName</th>
         </tr>
         <c:forEach items="${results}" var="result" varStatus="count">
         <tr><td>${result.rank}</td>
         <td><a href="viewResults?fileName=${result.fileName}">${result.fileName}</a></td>
        
         </tr>	
        </c:forEach> 
         
         <tr><td colspan = "8" align="center"></td></tr>    
        
</table>
                
               
                
            </div>
        </div>
    </div>
</div>

<style>
    .header {
        color: #36A0FF;
        font-size: 27px;
        padding: 10px;
    }

    .bigicon {
        font-size: 35px;
        color: #36A0FF;
    }
</style>

<!-- Contact Form - END -->

</div>

</body>
</html>