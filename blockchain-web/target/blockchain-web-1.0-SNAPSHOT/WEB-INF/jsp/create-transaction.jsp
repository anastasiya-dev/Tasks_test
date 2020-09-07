<jsp:include page="header.jsp"/>
<h1>Create transaction: </h1>

<form action="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" method="post">
   <div class="form-group">
      <label for="formGroupExampleInput2">Recipient public key</label>
      <input type="text" class="form-control" name="recipient" id="formGroupExampleInput1" placeholder="Recipient public key">
   </div>
   <div class="form-group">
      <label for="formGroupExampleInput2">Value</label>
      <input type="text" class="form-control" name="value" id="formGroupExampleInput2" placeholder="Transaction value">
   </div>
   <button type="submit" class="btn btn-primary">Submit</button>
</form>
<jsp:include page="footer.jsp"/>