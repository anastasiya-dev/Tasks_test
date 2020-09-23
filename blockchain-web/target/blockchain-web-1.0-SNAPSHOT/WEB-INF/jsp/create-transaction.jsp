<jsp:include page="header.jsp"/>

<div style="text-align:center">
<h2>Create transaction: </h2>
<form action="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" method="post" >
    <label for="recipient">Recipient</label><br>
     <input type="text" id="recipient" pattern="[a-z0-9._%+-]+" name="recipient" placeholder="Wallet Id"><br>

   <label for="value">Value</label><br>
<input type="text" id="value" pattern="[0-9]+\.?[0-9]?" name="value" placeholder=">0 and <balance"><br>

   <button type="submit" class="btn send-button">Submit</button>
</form>
</div>
<jsp:include page="footer.jsp"/>