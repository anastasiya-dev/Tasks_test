<jsp:include page="header.jsp"/>
<h3>The balance of your wallet is currently ${balance} </h3>
<br>
<h3>To prevent the loss of the funds before wallet deletion please make transfer transaction:</h3>
<br>
<a href="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Create transaction</a>
<br>
<h3>You can also review the transactions you saved but hasn't confirmed yet:</h3>
<a href="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">View unconfirmed</a>
<br>
<h3>To delete the wallet anyway please enter the private key for confirmation:</h3>
<br>
    <form action="/blockchain-web/${userId}/wallet/${walletId}/delete" method="post">
       <div class="form-group">
          <label for="formGroupExampleInput2">Private key to confirm: </label>
          <input type="text" class="form-control" name="privateKeyString" id="formGroupExampleInput1" placeholder="Private Key">
       </div>
       <button type="submit" class="btn btn-primary">Submit</button>
       </form>
<jsp:include page="footer.jsp"/>