<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
    <h1>Wallets:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">wallet id</th>
          <th scope="col">public key</th>
          <th scope="col">private key</th>
          <th scope="col">balance</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${wallets}" var="wallet">
        <tr>
          <td>${wallet.walletId}</td>
          <td>${wallet.publicKey}</td>
          <td>${wallet.privateKey}</td>
          <td>TO BE DONE</td>
          <td><p><a href="/blockchain-web/${wallet.walletId}/wallet" class="text-primary">Make transaction</a></p><td>
        </tr>
</c:forEach>
      </tbody>

    </table>
<jsp:include page="footer.jsp"/>