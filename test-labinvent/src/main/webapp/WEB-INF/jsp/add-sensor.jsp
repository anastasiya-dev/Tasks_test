<jsp:include page="header.jsp"/>
<div style="text-align:center">
   <h2>Add/edit</h2>
   <form action="/test-labinvent/add-sensor" method="post" >
      <label class="required" for="name">Name</label>
      <input type="text" id="name" name="name" placeholder="Name" value="${sensor.name}"><br>
      <label class="required" for="model">Model</label>
      <input type="text" id="model" name="model" placeholder="Model" value="${sensor.model}"><br>
      <a>Range</a>
      <label for="rangeFrom">from</label>
      <input type="number" step="1" id="rangeFrom" name="rangeFrom" placeholder="From" value="${sensor.rangeFrom}">
      <label for="rangeTo">to</label>
      <input type="number" step="1" id="rangeTo" name="rangeTo" placeholder="To" value="${sensor.rangeTo}"><br>
      <label class="required" for="type">Type</label>
      <input list="types" id="type" name="type" placeholder="Select" value="${sensor.type}">
      <datalist id="types">
         <option value="Pressure">
         <option value="Voltage">
         <option value="Temperature">
         <option value="Humidity">
      </datalist>
      <br>
      <label class="required" for="unit">Unit</label>
      <input list="units" id="unit" name="unit" placeholder="Select" value="${sensor.unit}">
      <datalist id="units">
      <option value="BAR">
      <option value="VOLTAGE">
      <option value="DEGREE">
      <option value="PERCENT">
      </datalist><br>
      <label for="location">Location</label>
      <input type="text" id="location" name="location" placeholder="location" value="${sensor.location}"><br>
      <p class="formfield">
      <label for="description">Description</label>
      <textarea id="description" name="description" rows=5 columns=100 placeholder="Here is some text input &#10; &#10; Here is another paragraph of input" value="${sensor.description}"></textarea><br>
      </p>
      <input type="hidden" name="sensorId" value="${sensor.sensorId}"/>
      <button type="submit" class="btn send-button-small">Save</button>
   </form>
   <br>
   <form action="/test-labinvent/add-sensor/cancel" method="get" >
   <button type="submit" class="btn send-button-small">Cancel</button>
   </form>
</div>