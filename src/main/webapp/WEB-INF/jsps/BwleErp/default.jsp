<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-Hans-CN">
<head>
<%@ include file="inc/meta.jsp" %>
</head>
<body ng-app="app">
<div class="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-top':app.settings.asideTop}" ui-view ng-controller="MainController" id="MainController"></div>
<!--<div ui-view="header_menu"></div>-->
<%@ include file="inc/header.jsp"%>
<%@ include file="inc/common.jsp"%>
</body>
</html>