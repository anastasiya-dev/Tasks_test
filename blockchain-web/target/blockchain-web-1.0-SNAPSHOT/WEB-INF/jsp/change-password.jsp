<jsp:include page="header.jsp"/>

<div style="text-align:center">
<h2>Change password: </h2>
<form action="/blockchain-web/${userId}/change-password" method="post" >
     <input type="password" id="oldPassword" name="oldPassword" placeholder="Old password"><br>
<input type="password" id="newPassword" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" name="newPassword" placeholder="New password"><br>
<small id="passwordRequirement" class="text-muted">Must contain at least one  number and one uppercase <br> and lowercase letter, and at least 8 or more characters</small><br>
<input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password"><br>

   <button type="submit" class="btn send-button">Submit</button>
</form>
</div>
<jsp:include page="footer.jsp"/>