<jsp:include page="header.jsp"/>

<form action="/blockchain-web/${userId}/change-password" method="post">
<div class="form-group">
        <input type="password" min="0" name="oldPassword" id="oldPassword"  class="form-control my-input" placeholder="Old password">
</div>

<div class="form-group">
        <input type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" min="0" name="newPassword" id="newPassword"  class="form-control my-input" placeholder="New password">
              <small id="passwordRequirement" class="form-text text-muted">Must contain at least one  number and one uppercase and lowercase letter, and at least 8 or more characters</small>
</div>

<div class="form-group">
        <input type="password" min="0" name="confirmPassword" id="confirmPassword"  class="form-control my-input" placeholder="Confirm new password">
</div>
    <button type="submit" class="btn btn-primary">Submit</button>
    </form>

<jsp:include page="footer.jsp"/>