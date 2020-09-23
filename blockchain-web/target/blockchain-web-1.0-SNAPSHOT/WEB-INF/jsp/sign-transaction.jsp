<jsp:include page="header.jsp"/>
<br>
<br>
<h2 >Your are going to confirm the following transaction:</h2>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Sender</th>
          <th scope="col">Recipient</th>
          <th scope="col">Value</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>${transaction.senderId}</td>
          <td>${transaction.recipientId}</td>
          <td>${transaction.value}</td>
        </tr>
      </tbody>
    </table>
<br>
<form action="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transactionId}/sign-transaction" method="post" >
    <label for="privateKeyString">Private key</label><br>
     <input type="text" id="privateKeyString" name="privateKeyString"><br>

   <button type="submit" class="btn send-button">Submit</button>
</form>


<jsp:include page="footer.jsp"/>