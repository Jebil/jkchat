<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="resources/css/jquery.ui.chatbox.css"
	rel="stylesheet" />
<script src="//cdn.jsdelivr.net/sockjs/1.0.0/sockjs.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.ui.chatbox.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	$(document).ready(function() {
		var lastMan;
		myOffset = 0;
		connect();
		initialize('${me}');
		onlineCheck();
	});
</script>
<script type="text/javascript">
	function addChatBox(name) {
		if (myOffset > 1000) {
			return false;
		}
		if ($("#" + name).length == 0) {
			$("#root").append('<div id='+name+'> </div>')
			$("#" + name).chatbox({
				id : name,
				title : name,
				user : ' ${me}',
				offset : myOffset,
				messageSent : function(id, user, msg) {
					this.boxManager.addMsg("You", msg);
					sendMessage(user, msg, name);
				},
				boxClosed : function(id) {
					myOffset -= 350;
				}
			});
			myOffset += 350;
			loadPrevMessages(name);
		} else {
			$("#" + name).chatbox("option", "hidden", false);
			myOffset += 350;
		}
	}
	function loadPrevMessages(name) {
		$.post("loadPreviousMessages", {
			"${_csrf.parameterName}" : "${_csrf.token}",
			"from" : name,
		}, function(data, status) {
			if (data.length != 0) {
				data.forEach(function(entry) {
					if (entry[1].trim() == '${me}') {
						$('#' + name).chatbox("option", "boxManager").addMsg(
								"You", entry[2]);
					} else {

						$('#' + entry[1].trim())
								.chatbox("option", "boxManager").addMsg(
										entry[1], entry[2]);
					}
				});
			}

		});
	}
	function initialize(me) {
		$.post("getMessages", {
			"${_csrf.parameterName}" : "${_csrf.token}"
		}, function(data, status) {
			if (data.length != 0) {
				data
						.forEach(function(entry) {
							if ($('#' + entry.from.trim()).length != 0) {
								$("#" + name)
										.chatbox("option", "hidden", false);
								$('#' + entry.from.trim()).chatbox("option",
										"boxManager").addMsg(entry.from,
										entry.message);
							} else {
								addChatBox(entry.from.trim());
								$('#' + entry.from.trim()).chatbox("option",
										"boxManager").addMsg(entry.from,
										entry.message);
							}
						});
			}

		});
	}

	function onlineCheck() {
		$("label").css('color', 'black');
		$.post("getOnlineUsers", {
			"${_csrf.parameterName}" : "${_csrf.token}"
		}, function(dat, status) {
			dat.forEach(function(entry) {
				$('#label-' + entry).css('color', 'green');
			})

		});
	}
</script>
<script type="text/javascript">
	var stompClient = null;
	function connect() {
		var socket = new SockJS('/jkchat/recieveMessage');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			stompClient.subscribe('/queue/${me}', function(greeting) {
				showGreeting(JSON.parse(greeting.body));
			});
			stompClient.subscribe('/queue/onlineUsers', function(userName) {
				$('#label-' + userName.body).css('color', 'green');
			});
			stompClient.subscribe('/queue/loggedOutUser', function(userName) {
				$('#label-' + userName.body).css('color', 'black');
			});
			stompClient.subscribe('/queue/users', function(users) {
				$('#usersList').append(
						"<label id='label-'" + users.body+">" + users.body
								+ "</label>");
				$('#label-' + users.body).click(function() {
					addChatBox(users.body);
				});
				$('#label-' + users.body).css('color', 'black');
			});

		});
	}

	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		console.log("Disconnected");
	}

	function sendMessage(from, message, to) {
		stompClient.send("/recieveMessage", {}, JSON.stringify({
			"from" : from,
			"to" : to,
			"message" : message
		}));
	}

	function showGreeting(message) {

		if ($('#' + message.from.trim()).length != 0) {
			$("#" + message.from.trim()).chatbox("option", "hidden", false);
		} else {
			addChatBox(message.from.trim());
		}
		$('#' + message.from.trim()).chatbox("option", "boxManager").addMsg(
				message.from, message.message);
	}
</script>
<title>Chat</title>
</head>
<body>
	<c:url var="logoutUrl" value="/logout" />
	<form:form class="form-inline" action="${logoutUrl}" method="post">
		<input type="submit" value="Log out" onclick="javascript:disconnect()" />
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form:form>
	<h1>Chat</h1>
	<h3>Welcome ${me}</h3>
	<sec:authorize access="isRememberMe()">
		<h2># This user is login by "Remember Me Cookies".</h2>
	</sec:authorize>

	<sec:authorize access="isFullyAuthenticated()">
		<h2># This user is login by username / password.</h2>
	</sec:authorize>
	<div id="usersList">
		<c:forEach items="${userList}" var="name">
			<label id="label-${fn:toLowerCase(name) }"
				onclick="javascript:addChatBox('${fn:toLowerCase(name)}')">${name}</label>
			<br />
		</c:forEach>
	</div>
	<div id="root"></div>
</body>
</html>
