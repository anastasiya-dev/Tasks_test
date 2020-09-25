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
      <c:url value="/resources/css/background-styles.css" />
      " rel="stylesheet">
      <title>BlockChain</title>
   </head>
   <body>
         <div class="navbar">
           <a href="/blockchain-web/home">Home</a>

            <a href="/blockchain-web/${userId}/wallet-all">MyBlockChain</a>
            <a href="/blockchain-web/${userId}/statistics">Statistics</a>

<sec:authorize access="isAuthenticated()">

                     <a href="/blockchain-web/logout" style="float:right">Logout</a>
                     <a href="/blockchain-web/${userId}/change-password" style="float:right">Change password</a>

                  </sec:authorize>
               <sec:authorize access="hasRole('USER')">
                  <a style="color:#0066ff; float:right" href="#">
                     Welcome,
                     <b>
                        <sec:authentication property="principal.username"/></b>!
                  </a>
               </sec:authorize>
               <sec:authorize access="hasRole('ADMIN')">
                  <a style="color:#0066ff; float:right" href="#">
                     Welcome Admin,
                     <b>
                        <sec:authentication property="principal.username"/></b>!
                  </a>
               </sec:authorize>



           <div class="dropdown">
             <button class="dropbtn">Personal info
               <i class="fa fa-caret-down"></i>
             </button>
             <div class="dropdown-content" style="overflow:visible;">

               <a href="/blockchain-web/${userId}/edit">Edit</a>
               <a href="/blockchain-web/${userId}/delete">Delete</a>
             </div>
           </div>





         </div>
