<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="registration form-box">
        <div class="header">Register</div>
        <div class="body">
            <form id="register-form" method="post" action="">
                <input type="hidden" name="action" value="register"/>
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login" value="${login!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="email">Email</label>
                    </div>
                    <div class="value">
                        <input id="email" name="email" type="text"/>
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
                <div class="field">
                    <div class="name">
                        <label for="passwordConfirmation">Password Confirmation</label>
                    </div>
                    <div class="value">
                        <input id="passwordConfirmation" name="passwordConfirmation" type="password"/>
                    </div>
                </div>
                <div id="register-error" class="error"></div>
                <div class="button-field">
                    <input type="submit" value="Register">
                </div>
            </form>
        </div>
    </div>
    <script>
        $("#register-form").on("submit", function (event) {
            event.preventDefault();
            const error = $("#register-error");
            error.text("");
            const data = $(this).serialize();
            ajax("/register", data).done(function (response) {
                if (response.error) {
                    error.text(response.error);
                }
            });
        });
    </script>
</@c.page>
