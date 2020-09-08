<jsp:include page="header.jsp"/>
<h1>Transaction: </h1>
    <table class="table">
      <thead>
        <tr>
          <th scope="col">wallet sender</th>
          <th scope="col">wallet recipient</th>
          <th scope="col">value</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>${sender}</td>
          <td>${recipient}</td>
          <td>${value}</td>
        </tr>
      </tbody>
    </table>

    <form action="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transactionId}/sign-transaction" method="post">
       <div class="form-group">
          <label for="formGroupExampleInput2">Private key to confirm: </label>
          <input type="text" class="form-control" name="privateKeyString" id="formGroupExampleInput1" placeholder="Private Key">
       </div>
       <button type="submit" class="btn btn-primary">Submit</button>
       </form>

<br>
<a href="/blockchain-web/${userId}/wallet/${walletId}/create-transaction" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Back to transaction</a>
<br>
<a href="/blockchain-web/${userId}/user-cabinet" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Back to user cabinet</a>

<jsp:include page="footer.jsp"/>