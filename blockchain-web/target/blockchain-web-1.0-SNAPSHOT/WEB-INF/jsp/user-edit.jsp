<jsp:include page="header.jsp"/>
<table class="table">

<div style="text-align:center">
<h2>Edit info: </h2>
<form action="/blockchain-web/${user.userId}/update" method="post" >
    <label for="userName">Name</label><br>
     <input type="text" id="userName" pattern=".{1,}" name="userName" value="${user.userName}"><br>
<small class="text-muted">If you change your name you'll be logged out</small><br>
<br>
  <label for="email">Email</label><br>
     <input type="text" id="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" name="email" value="${user.email}"><br>

  <label for="mobile">Phone</label><br>
     <input type="text" id="mobile" name="mobile" value="${user.mobile}"><br>

   <button type="submit" class="btn send-button">Submit</button>
</form>
</div>

<jsp:include page="footer.jsp"/>