<#import "commons.ftlh" as c>

<#assign canManageUsers = user?? && user.admin>
<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <#if canManageUsers>
                    <th>Actions</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#if users?size==0>
                <tr class="noData">
                    <td colspan="${canManageUsers?then(4, 3)}">
                        No data
                    </td>
                </tr>
            <#else>
                <#list users as user>
                <tr>
                    <td class="id">${user.id}</td>
                    <td class="login">${user.login}</td>
                    <td class="creationTime">${user.creationTime}</td>
                    <#if canManageUsers>
                        <td>
                            <form method="post" action="">
                                <input type="hidden" name="id" value="${user.id}">

                                <button type="submit"
                                        name="disabled"
                                        value="${(!user.disabled)?c}">
                                    ${user.disabled?then("Enable", "Disable")}
                                </button>
                            </form>
                        </td>
                    </#if>
                </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>
