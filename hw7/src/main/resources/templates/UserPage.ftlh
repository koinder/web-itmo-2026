<#import "commons.ftlh" as c>

<@c.page>
    <#if profileUser??>
        <h2>User profile</h2>

        <p>Id: ${profileUser.id}</p>
        <p>Login: ${profileUser.login}</p>
        <p>Created: ${profileUser.creationTime?datetime}</p>
    <#else>
        <p>No such user</p>
    </#if>
</@c.page>