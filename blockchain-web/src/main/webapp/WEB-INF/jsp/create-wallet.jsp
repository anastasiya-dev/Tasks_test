<jsp:include page="header.jsp"/>

<h2 style="margin-top: 2cm;">Your new wallet is created:</h2>
<div style="overflow-x:auto;">
<table class="table">
      <thead>

        <tr>
          <th scope="col">Wallet id</th>
          <th scope="col">Private key</th>
        </tr>
      </thead>
      <tbody>

        <tr>
          <td style="text-align:left">${wallet.walletId}</td>
          <td style="text-align:right">${wallet.privateKeyString}</td>
        </tr>
      </tbody>
    </table>
    </div>
    <br>
    <br>
    <p style="color:red">Save your private key and don't share it with anyone!</p>
    <p>It will be used for transactions confirmation.</p>
    <br>
    <br>

<jsp:include page="footer.jsp"/>