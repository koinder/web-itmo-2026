<#import "commons.ftlh" as c>

<@c.page>
    <div class="talks form-box">
        <div class="header">Send a message</div>
        <div class="body">
            <form method="post" action="/talks">
                <input type="hidden" name="action" value="send"/>

                <div class="field">
                    <div class="name">
                        <label for="targetUserId">Recipient</label>
                    </div>
                    <div class="value">
                        <select id="targetUserId" name="targetUserId">
                            <#list users as u>
                                <option value="${u.id?c}">${u.login}</option>
                            </#list>
                        </select>
                    </div>
                    <div class="field">
                        <div class="name">
                            <label for="text">Message</label>
                        </div>
                        <div class="value">
                            <textarea id="text" name="text" rows="5"
                                    maxlength="2500">${text!}</textarea>
                        </div>
                    </div>
                    <#if error??>
                        <div class="error">${error}</div>
                    </#if>

                    <div class="button-field">
                        <input type="submit" value="Send"/>
                    </div>
                </div>
            </form>
            <div class="talks datatable">
                <div class="caption">Messages</div>
                <table>
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Message</th>
                        <th>Created</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list talks as t>
                        <tr>
                            <td>${t.id}</td>
                            <td>${logins[t.sourceUserId?c]}</td>
                            <td>${logins[t.targetUserId?c]}</td>
                            <td>${t.text}</td>
                            <td>${t.creationTime?datetime}</td>
                        </tr>
                    <#else>
                        <tr>
                            <td colspan="5">No messages</td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</@c.page>