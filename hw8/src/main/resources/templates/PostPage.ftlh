<#import "commons.ftlh" as c>

<@c.page>
    <@c.post post=post/>
    <h2>Comments</h2>

    <#list post.comments as comment>
        <article>
            <div class="information">
                ${comment.user.login}, ${comment.creationTime?datetime}
            </div>

            <div class="body">
                <p>${comment.text}</p>
            </div>
        </article>
    <#else>
        <p>No comments yet.</p>
    </#list>
    <#if user??>
        <form method="post" action="">
            <label for="comment-text">Your comment</label>

            <p>
                <textarea id="comment-text" name="text"
                        rows="4" cols="60">${commentForm.text!}</textarea>
            </p>

            <@c.error "commentForm.text"/>

            <button type="submit">Send</button>
        </form>
    </#if>
</@c.page>