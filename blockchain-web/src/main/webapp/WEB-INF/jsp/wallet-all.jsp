<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
    <h2 style="margin-top: 4cm;">Please see all your wallets below:</h2>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">Wallet Id</th>
          <th scope="col">Wallet balance</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${wallets}" var="wallet">
        <tr>
          <td style="text-align:left">${wallet.walletId}</td>
          <td>${wallet.balance}</td>
          <td style="border-collapse: collapse"><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}">View</a><td>
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