<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html lang="en">
   <head>
      <!-- Required meta tags -->
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <!-- Bootstrap CSS -->
      <link href="
      <c:url value="/resources/css/main-styles.css" />
      " rel="stylesheet">
      <title>Sensors</title>
   </head>
   <body>
      <div class="navbar">
         <sec:authorize access="isAuthenticated()">
            <a href="/test-labinvent/logout" style="float:right">Logout</a>
         </sec:authorize>
      </div>