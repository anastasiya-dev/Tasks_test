<jsp:include page="header.jsp"/>
<h4 style="margin-top: 2cm;">The balance of your wallet is currently ${balance} </h4>
<h4>To prevent the loss of the funds before wallet deletion please make transfer transaction:</h4>
<form action="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" method="get" >
   <button type="submit" class="btn send-button-small">Make transfer</button>
</form>
<h4>You can also review the transactions you saved but haven't confirmed yet:</h4>
<form action="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed" method="get" >
   <button type="submit" class="btn send-button-small">View unconfirmed</button>
</form>
<h4>To delete the wallet anyway please enter the private key for confirmation:</h4>
    <form action="/blockchain-web/${userId}/wallet/${walletId}/delete" method="post">
 <label for="privateKeyString">Private key to confirm:</label><br>
 <input type="text" id="privateKeyString" name="privateKeyString"><br>
 <button type="submit" class="btn send-button-small">Submit</button>
       </form>
       <br>
<jsp:include page="footer.jsp"/>