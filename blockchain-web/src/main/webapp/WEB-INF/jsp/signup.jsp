<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="signup-header.jsp"/>

<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
<body>
   <div class="container">
      <div class="col-md-6 mx-auto text-center">
         <div class="header-title">
            <h1 class="wv-heading--title">
               Welcome!
            </h1>
            <h2 class="wv-heading--subtitle">
               And here the future of finance begins...
            </h2>
         </div>
      </div>
      <div class="row">
         <div class="col-md-4 mx-auto">
            <div class="myform form ">
               <form action="/blockchain-web/signup" method="post" name="login">
                  <div class="form-group">
                     <input type="text" name="userName"  class="form-control my-input" id="name" placeholder="Name">
                  </div>
                  <div class="form-group">
                     <input type="email" name="email"  class="form-control my-input" id="email" placeholder="Email">
                     <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
                  </div>
                  <div class="form-group">
                     <input type="number" min="0" name="mobile" id="phone"  class="form-control my-input" placeholder="Phone">
                  </div>
                  <div class="form-group">
                      <input type="password" min="0" name="userPassword" id="userPassword"  class="form-control my-input" placeholder="Password">
                  </div>
                   <div class="form-group">
                       <input type="password" min="0" name="confirmPassword" id="confirmPassword"  class="form-control my-input" placeholder="Confirm password">
                   </div>
                  <div class="text-center ">
                     <button type="submit" class=" btn btn-block send-button tx-tfm">Create Your Account</button>
                  </div>
                  <p class="small mt-3">By signing up, you are indicating that you have read and agree to the <a href="#" class="ps-hero__content__link">Terms of use</a> and <a href="#">Privacy policy</a>.
                  </p>
               </form>
            </div>
         </div>
      </div>
   </div>
</body>