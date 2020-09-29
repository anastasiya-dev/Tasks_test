<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="header.jsp"/>
<br>
<h3>Mining sessions:</h3>
<table class="table">
   <thead>
      <tr>
         <th scope="col">Mining Session Id</th>
         <th style="text-align:center" scope="col">Block Id</th>
         <th style="text-align:center" scope="col">Miner Reward</th>
         <th style="text-align:center" scope="col">Status</th>
         <th style="text-align:center" scope="col">Request</th>
         <th style="text-align:center" scope="col">Start</th>
         <th style="text-align:center" scope="col">End</th>
      </tr>
   </thead>
   <tbody>
      <c:forEach items="${miningSessionsForUser}" var="ms">
         <tr>
            <td style="text-align:left">${ms.miningSessionId}</td>
            <td style="text-align:left">${ms.blockIdActual}</td>
            <td>${ms.minerReward}</td>
            <td>${ms.miningSessionStatus}</td>
            <td>${ms.sessionRequest}</td>
            <td>${ms.sessionStart}</td>
            <td>${ms.sessionEnd}</td>
         </tr>
      </c:forEach>
   </tbody>
   <tfoot >
      <td style="text-align:left"><b>Total</b></td>
      <td></td>
      <td><b>${sum}</b></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
   </tfoot>
</table>
<jsp:include page="footer.jsp"/>