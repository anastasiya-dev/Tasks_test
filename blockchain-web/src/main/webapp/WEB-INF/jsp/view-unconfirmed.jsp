<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
<a href="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/delete-all" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Delete all unconfirmed</a>
<a href="/blockchain-web/${userId}/wallet/${walletId}" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Back to wallet</a>

    <h1>Sign all:</h1>
       <form action="/blockchain-web/${userId}/wallet/${walletId}/unconfirmed/sign-all" method="post">
           <div class="form-group">
              <label for="formGroupExampleInput2">Private key to confirm: </label>
              <input type="text" class="form-control" name="privateKeyString" id="formGroupExampleInput1" placeholder="Private Key">
           </div>
           <button type="submit" class="btn btn-primary">Submit</button>
           </form>

    <h1>Unconfirmed transactions:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">transaction id</th>
          <th scope="col">sender</th>
          <th scope="col">recipient</th>
          <th scope="col">value</th>
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
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/sign-transaction" class="text-primary">Sign</a></p><td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${walletId}/transaction/${transaction.transactionId}/delete-transaction" class="text-primary">Delete</a></p><td>
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