<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle??>
        <#assign u = c.findBy(users, "handle", handle)!/>
    <#elseif user_id?? && user_id?is_number>
        <#assign u = c.findBy(users, "id", user_id)!/>
    </#if>

    <#if u?has_content>
        <div class="user-profile">
            <img class="user-avatar" src="/img/no-title.svg" alt="${u.name}" title="${u.name}">
            <h1><@c.userlink u/></h1>
            <ul>
                <li>id: ${u.id}</li>
                <li>handle: ${u.handle}</li>
                <li>name: ${u.name}</li>
            </ul>
        </div>

        <#assign index = c.findIndBy(users, "id", u.id)/>
        <div class="user-arrows">
            <#if index gt 0>
                <a href="/user?handle=${users[index - 1].handle}">&larr;</a>
            <#else>
                <span class="disabled">&larr;</span>
            </#if>
            <#if index lt users?size - 1>
                <a href="/user?handle=${users[index + 1].handle}">&rarr;</a>
            <#else>
                <span class="disabled">&rarr;</span>
            </#if>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>
