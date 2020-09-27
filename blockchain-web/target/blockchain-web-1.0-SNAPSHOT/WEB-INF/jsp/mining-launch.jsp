<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<jsp:include page="header.jsp"/>

<div style="align:center">
   <h3>Please select wallet to start mining: </h3>
      <form:form method = "POST" action = "/blockchain-web/${userId}/mining-launch">
         <table>
            <tr>
               <td><form:label path = "miningWalletId"></form:label></td>
               <td>
                  <form:select class="select-css" path = "miningWalletId">
                     <form:option value = "NONE" label = "Select"/>
                     <form:options items = "${walletList}" />
                  </form:select>

              <button type="submit" class="btn send-button-small">Go!</button>

         </table>
      </form:form>
<p style="color:red">Important: you can mine only for one wallet at a time!</p>
</div>