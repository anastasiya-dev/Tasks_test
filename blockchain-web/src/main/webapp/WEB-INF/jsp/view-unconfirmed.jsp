<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
<br>
<br>
<form action="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/delete-all" method="get" >
   <button type="submit" class="btn send-button">Delete all unconfirmed</button>
</form>

    <h3>Sign all:</h3>
<form action="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/sign-all" method="post" >
    <label for="privateKeyString">Private key</label><br>
     <input type="text" id="privateKeyString" name="privateKeyString"><br>

   <button type="submit" class="btn send-button-small">Submit</button>
</form>

    <h3>Unconfirmed transactions:</h3>
    <table class="table">
      <thead>
        <tr>
          <th style="text-align:center" scope="col">Transaction Id</th>
          <th style="text-align:center" scope="col">Sender</th>
          <th style="text-align:center" scope="col">Recipient</th>
          <th style="text-align:center" scope="col">Value</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${transactions}" var="transaction">
        <tr>
          <td style="text-align:left" >${transaction.transactionId}</td>
          <td style="text-align:left">${transaction.senderId}</td>
          <td style="text-align:left">${transaction.recipientId}</td>
          <td>${transaction.value}</td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/sign-transaction" class="text-primary">Sign</a></p><td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/delete-transaction" class="text-primary">Delete</a></p><td>
        </tr>
</c:forEach>
      </tbody>
          <tfoot>
            <td><b>Total</b></td>
            <td></td>
            <td></td>
            <td><b>${sum}</b></td>
            <td></td>
            <td></td>
          </tfoot>

    </table>
<jsp:include page="footer.jsp"/>