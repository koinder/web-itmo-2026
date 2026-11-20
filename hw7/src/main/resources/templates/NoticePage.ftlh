<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice form-box">
        <div class="header">Add notice</div>
        <div class="body">
            <form method="post" action="">
                <div class="field">
                    <div class="name">
                        <label for="content">Content</label>
                    </div>
                    <div class="value">
                        <textarea id="content" name="content"
                                  rows="5" maxlength="10000">${noticeForm.content!}</textarea>
                    </div>

                    <@c.error "noticeForm.content"/>
                </div>

                <div class="button-field">
                    <input type="submit" value="Add"/>
                </div>
            </form>
        </div>
    </div>
</@c.page>