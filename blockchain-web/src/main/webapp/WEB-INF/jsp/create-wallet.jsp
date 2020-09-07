<jsp:include page="header.jsp"/>
<h1>wallet created: </h1>
<table class="table">
      <thead>
        <tr>
          <th scope="col">wallet id</th>
          <th scope="col">public key</th>
          <th scope="col">private key</th>
        </tr>
      </thead>
      <tbody>

        <tr>
          <td>${wallet.walletId}</td>
          <td>${wallet.publicKey}</td>
          <td>${wallet.privateKey}</td>
        </tr>
      </tbody>
    </table>
<a href="/blockchain-web/${userId}/user-cabinet" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Get back to the cabinet</a>
<jsp:include page="footer.jsp"/>