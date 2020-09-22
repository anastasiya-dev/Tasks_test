<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <link href='https://fonts.googleapis.com/css?family=Roboto:400,300,500,100' rel='stylesheet' type='text/css'>
<link href="<c:url value="/resources/css/home-styles.css" />" rel="stylesheet">
  </head>
 <body>
    <div class="header">
      <div class="container">
        <h1>BlockChain</h1>
        <p>Shape the future of finance and technology with us</p>
        <a class="btn" href="#">Learn More</a>
      </div>
    </div>

    <div class="nav">
      <div class="container">
        <ul>
   <sec:authorize access="isAuthenticated()">
   <li><a class="btn" href="/blockchain-web/logout">Logout</a></li>
   </sec:authorize>

   <sec:authorize access="!isAuthenticated()">
   <li><a class="btn" href="/blockchain-web/signup">Signup</a></li>
    <li><a class="btn" href="/blockchain-web/login">Login</a></li>
    </sec:authorize>

    <li><a class="btn" href="#">About</a></li>
    <li><a class="btn" href="#">Contacts</a></li>


      </div>
    </div>

    <div class="main">
      <div class="container">
        <h2>How blockchain works - in  simple words</h2>
        <p>Blockchain is the technology the underpins digital currency. The tech allows digital information to be distributed, but not copied. That means each individual piece of data can only have one owner.</p>
        <p>You may hear it described as a "digital ledger" stored in a distributed network.</p>
        <p>There is a good analogy to help understand how Blockchain works:</p>
        <p>"Picture a spreadsheet that is duplicated thousands of times across a network of computers.
         <br>Then imagine that this network is designed to regularly update this spreadsheet
         <br>and you have a basic understanding of the blockchain."</p>
        </div>
    </div>

    <div class="jumbotron">
      <div class="container">
        <h2>Stay Connected</h2>
        <p>Receive weekly insights from industry insiders.</p>
        <a class="btn" href="#">Join</a>
      </div>
    </div>

    <div class="footer">
      <div class="container">
        <p>&copy; BlockChain. Anastasiya Kalach</p>
      </div>
    </div>
  </body>
</html>

