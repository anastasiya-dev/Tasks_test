<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="header.jsp"/>
    <h1>Wallets:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">wallet id</th>
          <th scope="col">balance</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${wallets}" var="wallet">
        <tr>
          <td>${wallet.walletId}</td>
          <td>TO BE DONE</td>
          <td><p><a href="/blockchain-web/${userId}/wallet/${wallet.walletId}" class="text-primary">View wallet</a></p><td>
        </tr>
</c:forEach>
      </tbody>
                <tfoot>
                  <td>Total</td>
                  <td>${sum}</td>
                </tfoot>

    </table>
<jsp:include page="footer.jsp"/>