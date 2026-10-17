<#import "./common.ftlh" as c>

<@c.page>
    <#if user_id?? && user_id?is_number>
        <#assign u = c.findBy(users, "id", user_id)!/>
    </#if>

    <#if u?has_content>
        <#list posts?filter(p -> p.userId == u.id)?reverse as p>
            <@c.post p/>
        <#else>
            No posts
        </#list>
    <#else>
        No such user
    </#if>
</@c.page>
