<#import "commons.ftlh" as c>

<@c.page>
    <div class="article form-box">
        <div class="header">Create article</div>
        <div class="body">
            <form id="article-form" method="post" action="/article">
                <input type="hidden" name="action" value="create"/>

                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title" type="text"
                               maxlength="255"/>
                    </div>
                </div>

                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text" rows="10"
                                  maxlength="10000"></textarea>
                    </div>
                </div>

                <div id="article-error" class="error"></div>

                <div class="button-field">
                    <input type="submit" value="Create"/>
                </div>
            </form>
        </div>
    </div>
    <script>
        $("#article-form").on("submit", function (event) {
            event.preventDefault();

            const error = $("#article-error");
            error.text("");

            const data = $(this).serialize();

            ajax("/article", data).done(function (response) {
                if (response.error) {
                    error.text(response.error);
                }
            });
        });
    </script>
</@c.page>