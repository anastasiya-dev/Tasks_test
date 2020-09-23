<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
<h2 style="margin-top: 2cm;"></h2>
<form action="/blockchain-web/${userId}/create-wallet" method="get" >
   <button type="submit" class="btn send-button-small">Create wallet</button>
</form>

    <br>
    <h2>Please see all your wallets below:</h2>

    <table class="table">
      <thead>
        <tr>
          <th style="text-align:center"scope="col">Wallet Id</th>
          <th style="text-align:center" scope="col">Wallet balance</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${wallets}" var="wallet">
        <tr>
          <td style="text-align:left">${wallet.walletId}</td>
          <td>${wallet.balance}</td>
          <td style="border-collapse: collapse"><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}/create-transaction">New transaction</a><td>
          <td style="border-collapse: collapse"><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}/transaction-all">View processed</a><td>
          <td style="border-collapse: collapse"><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}/unconfirmed">View unconfirmed </a><td>
          <td style="border-collapse: collapse"><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}/delete">Delete</a><td>
        </tr>
</c:forEach>
      </tbody>
                <tfoot>
                  <td><b>Total</b></td>
                  <td><b>${sum}</b></td>
                </tfoot>

    </table>
<jsp:include page="footer.jsp"/>