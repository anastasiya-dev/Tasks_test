<jsp:include page="header.jsp"/>

<h2 style="margin-top: 2cm;">By pressing the button below you confirm that your profile in BlockChain will be deleted</h2>

<form action="/blockchain-web/${userId}/delete-user-confirm-process" method="get" >
   <button type="submit" class="btn send-button">Confirm</button>
</form>


<jsp:include page="footer.jsp"/>