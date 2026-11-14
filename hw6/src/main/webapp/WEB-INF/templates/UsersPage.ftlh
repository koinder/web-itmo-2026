<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <#if canManageAdmins>
                    <th>Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td>${u.id}</td>
                    <td>${u.login}</td>
                    <td>${u.creationTime?datetime}</td>
                    <#if canManageAdmins>
                        <td>
                            <span class="admin-value">${u.admin?c}</span>

                            <button type="button" class="change-admin"
                                    data-id="${u.id?c}"
                                    data-admin="${(!u.admin)?c}">
                                <#if u.admin>Disable<#else>Enable</#if>
                            </button>
                        </td>
                    </#if>
                </tr>
            <#else>
                <tr class="noData">
                    <td colspan="${canManageAdmins?then(4, 3)}">No data</td>
                </tr>
            </#list>
            </tbody>
        </table>
        <div id="admin-error" class="error"></div>
    </div>
    <script>
        $(".change-admin").on("click", function () {
            const button = $(this);
            const error = $("#admin-error");
            error.text("");
            ajax("/users", {
                action: "changeAdmin",
                id: button.attr("data-id"),
                admin: button.attr("data-admin")
            }).done(function (response) {
                if (response.error) {
                    error.text(response.error);
                    return;
                }
                if (typeof response.admin === "boolean") {
                    button.closest("td")
                        .find(".admin-value")
                        .text(String(response.admin));
                    button.text(response.admin ? "Disable" : "Enable");
                    button.attr("data-admin", String(!response.admin));
                }
            }).fail(function () {
                error.text("Could not change administrator rights.");
            });
        });
    </script>
</@c.page>
