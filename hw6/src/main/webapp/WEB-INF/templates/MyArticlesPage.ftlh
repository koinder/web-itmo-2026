<#import "commons.ftlh" as c>

<@c.page>
    <div class="my-articles datatable">
        <div class="caption">My articles</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Visibility</th>
            </tr>
            </thead>
            <tbody>
            <#list articles as a>
                <tr>
                    <td>${a.id}</td>
                    <td>${a.title}</td>
                    <td>
                        <button type="button" class="change-hidden"
                                data-id="${a.id?c}"
                                data-hidden="${(!a.hidden)?c}">
                            <#if a.hidden>Show<#else>Hide</#if>
                        </button>
                    </td>
                </tr>
            <#else>
                <tr>
                    <td colspan="3">No articles</td>
                </tr>
            </#list>
            </tbody>
        </table>
        <div id="visibility-error" class="error"></div>
    </div>
    <script>
        $(".change-hidden").on("click", function () {
            const button = $(this);
            const error = $("#visibility-error");

            error.text("");
            button.prop("disabled", true);

            ajax("/myArticles", {
                action: "changeHidden",
                id: button.attr("data-id"),
                hidden: button.attr("data-hidden")
            }).done(function (response) {
                if (response.error) {
                    error.text(response.error);
                    return;
                }

                if (typeof response.hidden === "boolean") {
                    button.text(response.hidden ? "Show" : "Hide");
                    button.attr("data-hidden", String(!response.hidden));
                }
            }).fail(function () {
                error.text("Could not change article visibility.");
            }).always(function () {
                button.prop("disabled", false);
            });
        });
    </script>
</@c.page>