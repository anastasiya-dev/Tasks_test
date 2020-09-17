<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
<a href="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/delete-all" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Delete all unconfirmed</a>
<a href="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/sign-all" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Sign all unconfirmed</a>
<a href="/blockchain-web/${userId}/wallet/${walletId}" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Back to wallet</a>
    <h1>Transactions:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">transaction id</th>
          <th scope="col">sender</th>
          <th scope="col">recipient</th>
          <th scope="col">value</th>
          <th scope="col">date and time</th>
          <th scope="col"></th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${transactions}" var="transaction">
        <tr>
          <td>${transaction.transactionId}</td>
          <td>${transaction.senderId}</td>
          <td>${transaction.recipientId}</td>
          <td>${transaction.value}</td>
          <td>${transaction.transactionDateTime}</td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/sign-transaction" class="text-primary">Sign</a></p><td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/delete" class="text-primary">Delete</a></p><td>
        </tr>
</c:forEach>
      </tbody>
          <tfoot>
            <td>Total</td>
            <td></td>
            <td></td>
            <td>${sum}</td>
            <td></td>
            <td></td>
          </tfoot>

    </table>
<jsp:include page="footer.jsp"/>