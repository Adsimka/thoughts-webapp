<#import "parts/common_macro.ftl" as c>
<#import "parts/login_macro.ftl" as l>

<@c.page "Registration page">

Add new user

<@l.login "/registration" />

<#if message??>
    <ul>
        <li>${message}</li>
    </ul>
</#if>

</@c.page>