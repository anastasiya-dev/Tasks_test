<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
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
<c:forEach items="${transactions}" var="transaction">
        <tr>
          <td>${transaction.transactionId}</td>
          <td>${transaction.senderString}</td>
          <td>${transaction.recipientString}</td>
          <td>${transaction.value}</td>
          <td>TO BE DONE</td>
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