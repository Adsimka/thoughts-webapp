<#import "parts/common_macro.ftl" as c>

<@c.page "Messages">

<h1>
    Hello <#if user??>${user.username}<#else>user</#if>!
</h1>
<form action="/logout" method="post">
    <input type="submit" value="Sign Out"/>
</form>

<h1>All Messages</h1>

<form action="/messages/tag" method="post">
    <input type="text" name="tag" placeholder="Enter tag" value="${tag}">
    <button type="submit">Найти</button>
</form>

<div>
    <ul>
        <#list messages as message>
        <li>
            <span>${message.id}</span>
            <span>${message.text}</span>
            <span>${message.tag}</span>
            <span>${message.author.username}</span>
        </li>
    </#list>
    </ul>
</div>

<a href="/messages/create">Add new message</a>

</@c.page>
