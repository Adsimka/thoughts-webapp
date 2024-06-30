<#import "parts/common_macro.ftl" as c>
<#import "parts/login_macro.ftl" as l>

<@c.page "Login page">

Login page

<#if param.error>
Invalid username or password.
</#if>
<#if param.logout>
You have been logged out.
</#if>

<@l.login "/login" />

<a href="/registration">Add new user</a>

</@c.page>