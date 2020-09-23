<jsp:include page="header.jsp"/>
<h2>Create transaction: </h2>

<div style="text-align:center">
<h2>Create transaction: </h2>
<form action="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" method="post" >
    <label for="recipient"></label><br>
     <input type="text" id="recipient" name="recipient" placeholder="Recipient"><br>

   <label for="value"></label><br>
<input type="text" id="value" name="value" placeholder="Value"><br>

   <button type="submit" class="btn send-button">Submit</button>
</form>
</div>
<jsp:include page="footer.jsp"/>