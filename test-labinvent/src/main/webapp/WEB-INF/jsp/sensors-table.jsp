<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="header.jsp"/>
<br>
<h3>Sensor table</h3>
<form action="/test-labinvent/sensors-table">
   <input class="input-small" type="text" id="viewerInput" name="viewerInput" placeholder="enter text to search"  value="${filterInput.viewerInput}">
   <button type="submit" class="btn send-button-small">Search</button>
</form>
<br>
<form action="/test-labinvent/sensors-table">
   <button type="submit" class="btn send-button-small">Clear all filters</button>
</form>
<br>
<c:set value="${sensorList}" var="sensorPageList"/>
<table class="table">
   <thead>
      <tr>
         <th scope="col"></th>
         <th scope="col">Name</th>
         <th scope="col">Model</th>
         <th scope="col">Type</th>
         <th scope="col">Range</th>
         <th scope="col">Unit</th>
         <th scope="col">Location</th>
      </tr>
   </thead>
   <tbody>
      <c:forEach items="${sensorPageList.pageList}" var="sensor">
         <tr>
            <td><a href="/test-labinvent/edit-sensor/${sensor.sensorId}">Edit</a>
            <td title=${sensor.description} >${sensor.name}</td>
            <td>${sensor.model}</td>
            <td>${sensor.type}</td>
            <td>${sensor.rangeFrom} - ${sensor.rangeTo}</td>
            <td>${sensor.unitValue}</td>
            <td>${sensor.location}</td>
            <td><a href="/test-labinvent/delete-sensor/${sensor.sensorId}">Delete</a>
         </tr>
      </c:forEach>
   </tbody>
</table>
<br>
<c:choose>
   <%-- Show Prev as link if not on first page --%>
   <c:when test="${sensorPageList.firstPage}">
      <span>Prev</span>
   </c:when>
   <c:otherwise>
      <c:url value="/sensors-table/prev" var="url" />
      <a href='<c:out value="${url}" />'>Prev</a>
   </c:otherwise>
</c:choose>
<c:forEach begin="1" end="${sensorPageList.pageCount}" step="1"  varStatus="tagStatus">
   <c:choose>
      <%-- In PagedListHolder page count starts from 0 --%>
      <c:when test="${(sensorPageList.page + 1) == tagStatus.index}">
         <span>${tagStatus.index}</span>
      </c:when>
      <c:otherwise>
         <c:url value="/sensors-table/${tagStatus.index}" var="url" />
         <a href='<c:out value="${url}" />'>${tagStatus.index}</a>
      </c:otherwise>
   </c:choose>
</c:forEach>
<c:choose>
   <%-- Show Next as link if not on last page --%>
   <c:when test="${sensorPageList.lastPage}">
      <span>Next</span>
   </c:when>
   <c:otherwise>
      <c:url value="/sensors-table/next" var="url" />
      <a href='<c:out value="${url}" />'>Next</a>
   </c:otherwise>
</c:choose>
<br>
<form action="/test-labinvent/add-sensor" method="get" >
   <button type="submit" class="btn send-button-small">Add Sensor</button>
</form>