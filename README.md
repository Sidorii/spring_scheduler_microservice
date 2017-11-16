# spring_scheduler_microservice
<b>Spring Boot</b> microservice for <b>scheduling, storing</b> and <b>delegating</b> tasks. Tasks comes from web via <b>JSON
</b>objects that are
parses and stores in DB. Then, using <b>Quartz</b>, they executes by different adapters (like http, shell and etc.) 
Used technology stack:

<ul>
<li>Connection to DB: <i>Spring JDBC</i><br>
<li>DataBase: <i>MySQL</i><br>
<li>Web-flow: <i>Spring MVC, REST</i><br>
<li>Sheduling: <i>Quartz</i><br>
<li>JSON parsing: <i>Jackson</i><br>
<li>Design patterns: <i>Controller, Adapter, Service, Wrapper, Listener</i><br>
<li>Testing: <i>JUnit, EasyMock, TDD</i><br>
<li>Build tool: <i>Maven</i>
</ul>
