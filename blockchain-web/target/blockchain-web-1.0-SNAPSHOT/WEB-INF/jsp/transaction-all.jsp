<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="header.jsp"/>
<br>
<form action="/blockchain-web/${userId}/wallet/${walletId}/download" method="get" >
   <button type="submit" class="btn send-button-small">Download</button>
</form>
   <h3>Filters:</h3>

   <form action="/blockchain-web/${userId}/wallet/${walletId}/transaction-all">
        <input class="input-small" type="text" id="sender" name="sender" placeholder="Sender Id"  value="${filterInput.sender}">
        <input class="input-small" type="text" id="recipient" name="recipient" placeholder="Recipient Id"  value="${filterInput.recipient}"><br>
        <input class="input-small" pattern="-?[0-9]+\.?[0-9]?" type="text" id="valueMin" name="valueMin" placeholder="Min" value="${filterInput.valueMin}">
        <input class="input-small" pattern="-?[0-9]+\.?[0-9]?" type="text" id="valueMax" name="valueMax" placeholder="Max" value="${filterInput.valueMax}"><br>
     <input class="input-small" pattern="[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]" type="text" id="dateStart" name="dateStart" value="${filterInput.dateStart}" placeholder="From: yyyy-MM-dd HH:mm">
     <input class="input-small" pattern="[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]" type="text" id="dateEnd" name="dateEnd" value="${filterInput.dateEnd}"placeholder="To: yyyy-MM-dd HH:mm"><br>
      <input class="input-small" type="text" id="operationType" name="operationType" placeholder="Status" value="${filterInput.operationType}">

      <button type="submit" class="btn send-button-small">Apply</button>
   </form>
<br>
      <form action="/blockchain-web/${userId}/wallet/${walletId}/transaction-all">
 <button type="submit" class="btn send-button-small">Clear all filters</button>
   </form>

    <h3>Transactions:</h3>
    <c:set value="${transactionList}" var="transactionPageList" />
    <table class="table">
      <thead>
        <tr>
          <th scope="col">Transaction Id</th>
          <th style="text-align:center" scope="col">Sender</th>
          <th style="text-align:center" scope="col">Recipient</th>
          <th style="text-align:center" scope="col">Value</th>
          <th style="text-align:center" scope="col">Status</th>
          <th style="text-align:center" scope="col">Date and Time</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${transactionPageList.pageList}" var="transaction">
        <tr>
          <td>${transaction.transactionId}</td>
          <td style="text-align:left">${transaction.senderId}</td>
          <td style="text-align:left">${transaction.recipientId}</td>
          <td>${transaction.value}</td>
          <td>${transaction.transactionStatus}</td>
          <td>${transaction.transactionDateTime}</td>
        </tr>
</c:forEach>
      </tbody>
          <tfoot >
            <td style="text-align:left"><b>Total</b></td>
            <td></td>
            <td></td>
            <td><b>${sum}</b></td>
            <td></td>
            <td></td>
          </tfoot>
    </table>
<br>
    <c:choose>
      <%-- Show Prev as link if not on first page --%>
      <c:when test="${transactionPageList.firstPage}">
        <span>Prev</span>
      </c:when>
      <c:otherwise>
        <c:url value="/${userId}/wallet/${walletId}/transaction-all/prev" var="url" />
        <a href='<c:out value="${url}" />'>Prev</a>
      </c:otherwise>
    </c:choose>
    <c:forEach begin="1" end="${transactionPageList.pageCount}" step="1"  varStatus="tagStatus">
      <c:choose>
        <%-- In PagedListHolder page count starts from 0 --%>
        <c:when test="${(transactionPageList.page + 1) == tagStatus.index}">
          <span>${tagStatus.index}</span>
        </c:when>
        <c:otherwise>
          <c:url value="/${userId}/wallet/${walletId}/transaction-all/${tagStatus.index}" var="url" />
          <a href='<c:out value="${url}" />'>${tagStatus.index}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>
    <c:choose>
      <%-- Show Next as link if not on last page --%>
      <c:when test="${transactionPageList.lastPage}">
        <span>Next</span>
      </c:when>
      <c:otherwise>
        <c:url value="/${userId}/wallet/${walletId}/transaction-all/next" var="url" />
        <a href='<c:out value="${url}" />'>Next</a>
      </c:otherwise>
    </c:choose>
<br>
<jsp:include page="footer.jsp"/>