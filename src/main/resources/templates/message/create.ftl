<#import "parts/common_macro.ftl" as c>

<@c.page "Messages">

<h1>Create message</h1>

<form method="post" action="/messages">
    <label for="text">Text:
        <input type="text" id="text" name="text" required><br><br>
    </label><br>

    <label for="tag">Tag:
        <input type="text" id="tag" name="tag" required><br><br>
    </label><br>

    <button type="submit">Create Message</button>
</form>

</@c.page>