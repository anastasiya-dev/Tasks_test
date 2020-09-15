<jsp:include page="header.jsp"/>
<h1>wallet created: </h1>
<table class="table">
      <thead>
        <tr>
          <th scope="col">wallet id</th>
          <th scope="col">private key</th>
        </tr>
      </thead>
      <tbody>

        <tr>
          <td>${wallet.walletId}</td>
          <td>${wallet.privateKeyString}</td>
        </tr>
      </tbody>
    </table>
    <h2>Save your private key and don't share it with anyone!</h2>
    <br>
    <h2>It will be used for transactions confirmation</h2>
<a href="/blockchain-web/${userId}/user-cabinet" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Get back to the cabinet</a>
<jsp:include page="footer.jsp"/>