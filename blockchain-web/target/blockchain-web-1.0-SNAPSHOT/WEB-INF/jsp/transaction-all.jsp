<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
<a href="/blockchain-web/${userId}/wallet/${walletId}/download" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Download</a>
    <h1>Filters:</h1>
       <form class="form-inline my-2 my-lg-0" action="/blockchain-web/${userId}/wallet/${walletId}/transaction-all">
    <input class="form-control mr-sm-2" type="search" name="sender" placeholder="sender" aria-label="sender" value="${filterInput.sender}">
    <input class="form-control mr-sm-2" type="search" name="recipient" placeholder="recipient" aria-label="recipient" value="${filterInput.recipient}">
    <input class="form-control mr-sm-2" type="search" name="valueMin" placeholder="valueMin" aria-label="valueMin" value="${filterInput.valueMin}">
    <input class="form-control mr-sm-2" type="search" name="valueMax" placeholder="valueMax" aria-label="valueMax" value="${filterInput.valueMax}">
    <input class="form-control mr-sm-2" type="search" name="operationType" placeholder="status" aria-label="operationType" value="${filterInput.operationType}">
    <input class="form-control mr-sm-2" type="search" name="dateStart" placeholder="from: yyyy-MM-dd HH:mm" aria-label="dateStart" value="${filterInput.dateStart}">
    <input class="form-control mr-sm-2" type="search" name="dateEnd" placeholder="to: yyyy-MM-dd HH:mm" aria-label="dateEnd" value="${filterInput.dateEnd}">
    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Apply filter</button>
      </form>
    <h1>Transactions:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">transaction id</th>
          <th scope="col">sender</th>
          <th scope="col">recipient</th>
          <th scope="col">value</th>
          <th scope="col">status</th>
          <th scope="col">date and time</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${transactionsFiltered}" var="transaction">
        <tr>
          <td>${transaction.transactionId}</td>
          <td>${transaction.senderId}</td>
          <td>${transaction.recipientId}</td>
          <td>${transaction.value}</td>
          <td>${transaction.transactionStatus}</td>
          <td>${transaction.transactionDateTime}</td>
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