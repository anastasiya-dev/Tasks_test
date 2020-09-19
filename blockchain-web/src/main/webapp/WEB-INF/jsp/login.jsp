<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="signup-header.jsp"/>
<body>
   <div class="container">
      <div class="d-flex justify-content-center h-100">
         <div class="card">
            <div class="card-header">
               <h3>Sign In</h3>
            </div>
            <div class="card-body">
               <form action="/blockchain-web/login" method="post">
                  <div class="input-group form-group">
                     <div class="input-group-prepend">
                        <span class="input-group-text"><i class="fas fa-user"></i></span>
                     </div>
                     <input type="text" class="form-control my-input" name="inputName" placeholder="username">
                  </div>
                  <div class="input-group form-group">
                     <div class="input-group-prepend">
                        <span class="input-group-text"><i class="fas fa-key"></i></span>
                     </div>
                     <input type="password" class="form-control my-input" name="inputPassword" placeholder="password">
                  </div>
                  <div class="row align-items-center remember">
                     <input type="checkbox">Remember Me
                  </div>
                  <div class="form-group">
                     <input type="submit" value="Login" class="btn float-right send-button">
                  </div>
               </form>
            </div>
            <div class="card-footer">
               <div class="d-flex justify-content-center links">
                  Don't have an account?<a href="/blockchain-web/signup">Sign Up</a>
               </div>
               <div class="d-flex justify-content-center">
                  <a href="#">Forgot your password?</a>
               </div>
            </div>
         </div>
      </div>
   </div>
</body>
<jsp:include page="footer.jsp"/>