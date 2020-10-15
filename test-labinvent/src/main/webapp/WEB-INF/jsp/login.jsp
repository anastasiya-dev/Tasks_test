<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html xmlns:th="http://www.thymeleaf.org">
   <head th:include="layout :: head(title=~{::title},links=~{})">
      <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
      <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
      <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
      <link href="
      <c:url value="/resources/css/signup-styles.css" />
      " rel="stylesheet">
      <title>Sensors</title>
   </head>
   <body th:include="layout :: body" th:with="content=~{::content}">
      <div th:fragment="content">
      <div class="container">
      <div class="d-flex justify-content-center h-100">
      <div class="card">
         <div class="card-header">
            <h3>Login Form</h3>
            <h4>enter login and password for access</h4>
         </div>
         <div class="card-body">
            <form name="f" th:action="@{/login}" method="post">
               <fieldset>
                  <div th:if="${param.error}" class="alert alert-error">
                  </div>
                  <div th:if="${param.logout}" class="alert alert-success">
                     You have been logged out.
                  </div>
                  <div class="input-group form-group">
                     <input type="text" id="username" class="form-control my-input" name="username" placeholder="Your Login">
                  </div>
                  <div class="input-group form-group">
                     <input type="password" id="password" class="form-control my-input" name="password" placeholder="Your Password">
                  </div>
                  <div class="form-actions">
                     <button type="submit" class="btn float-right send-button">Log in</button>
                  </div>
               </fieldset>
            </form>
         </div>
      </div>
   </body>
</html>