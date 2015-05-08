<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="resources/css/jquery.ui.chatbox.css"
	rel="stylesheet" />
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.ui.chatbox.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
	$(document).ready(function() {
		myOffset = 0;
		onlineCheck();
		initialize('${me}');
		// to create
		//$(handler).chatbox("option", "boxManager").addMsg("Mr. Foo", "Barrr!");

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
	function sendMessage(from, message, to) {
		$.post("sendMessage", {
			"${_csrf.parameterName}" : "${_csrf.token}",
			"from" : from,
			"to" : to,
			"message" : message
		}, function(data, status) {
		});
	}
	function initialize(me) {
		setInterval(function() {
			$.post("getMessages", {
				"${_csrf.parameterName}" : "${_csrf.token}"
			}, function(data, status) {
				if (data.length != 0) {
					data.forEach(function(entry) {
						if ($('#' + entry.from.trim()).length != 0) {
							$("#" + name).chatbox("option", "hidden", false);
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
		}, 2000)
	}
	function onlineCheck() {
		setInterval(function() {
			$("label").css('color', 'black');
			$.post("getOnlineUsers", {
				"${_csrf.parameterName}" : "${_csrf.token}"
			}, function(dat, status) {
				dat.forEach(function(entry) {
					$('#label-' + entry).css('color', 'green');
				})

			});
		}, 4000)
	}
</script>
<title>Chat</title>
</head>
<body>
	<c:url var="logoutUrl" value="/logout" />
	<form:form class="form-inline" action="${logoutUrl}" method="post">
		<input type="submit" value="Log out" />
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form:form>
	<h1>Chat</h1>
	<c:forEach items="${userList}" var="name">
		<label id="label-${fn:toLowerCase(name) }" onclick="javascript:addChatBox('${fn:toLowerCase(name)}')">${name}</label>
		<br />
	</c:forEach>
	<div id="root"></div>
</body>
</html>
