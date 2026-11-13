<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="enter form-box">
        <div class="header">Enter</div>
        <div class="body">
            <form id="enter-form" method="post" action="">
                <input type="hidden" name="action" value="enter"/>
                <div class="field">
                    <div class="name">
                        <label for="loginOrEmail">Login or email</label>
                    </div>
                    <div class="value">
                        <input id="loginOrEmail" name="loginOrEmail" value="${loginOrEmail!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" type="password"/>
                    </div>
                </div>
                <div id="enter-error" class="error"></div>
                <div class="button-field">
                    <input type="submit" value="Enter">
                </div>
            </form>
        </div>
    </div>
    <script>
        $("#enter-form").on("submit", function (event) {
            event.preventDefault();
            const error = $("#enter-error");
            error.text("");
            const data = $(this).serialize();
            ajax("/enter", data).done(function (response) {
                if (response.error) {
                    error.text(response.error);
                }
            });
        });
    </script>
</@c.page>
