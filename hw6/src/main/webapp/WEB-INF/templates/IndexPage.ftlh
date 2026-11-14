<#import "commons.ftlh" as c>

<@c.page>
    <div id="articles"></div>
    <template id="article-template">
        <article>
            <div class="title"></div>

            <div class="information">
                By <span class="author"></span>,
                <span class="creation-time"></span>
            </div>

            <div class="body"></div>
        </article>
    </template>
    <script>
        $(function () {
            ajax("/index", {action: "articles"}).done(function (response) {
                const container = $("#articles");
                const template = document.getElementById("article-template");
                container.empty();
                response.articles.forEach(function (data) {
                    const article = $(
                        template.content.firstElementChild.cloneNode(true)
                    );
                    article.find(".title").text(data.title);
                    article.find(".author").text(response.logins[data.userId]);
                    article.find(".creation-time").text(data.creationTime);
                    article.find(".body").text(data.text);
                    container.append(article);
                });
            });
        });
    </script>
</@c.page>
