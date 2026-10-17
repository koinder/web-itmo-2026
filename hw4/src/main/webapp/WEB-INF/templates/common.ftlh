<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/users", "name": "Users"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <a href="${item.href}"<#if uri == item.href> class="current"</#if>>${item.name}</a>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <section>
                <div class="header">Post #${p.id}</div>
                <div class="body">${shorten(p.text)}</div>
                <div class="footer">
                    <a href="/post?post_id=${p.id}">View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#-- nameOnly=true: старый вид (имя обычной ссылкой), иначе — цветной хэндл как на Codeforces -->
<#macro userlink user nameOnly=false>
<#-- @ftlvariable name="user" type="ru.itmo.web.hw4.model.User" -->
    <#if nameOnly>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    <#else>
        <a href="/user?handle=${user.handle}" class="user-link user-${user.color?lower_case}">${user.handle}</a>
    </#if>
</#macro>

<#-- full=false: сокращённый вид (текст до 350 символов), full=true: полный пост -->
<#macro post p full=false>
    <#local author = findBy(users, "id", p.userId)!/>
    <article>
        <div class="title"><a href="/post?post_id=${p.id}">${p.title}</a></div>
        <div class="information">
            By <#if author?has_content><@userlink author/><#else>unknown</#if>
        </div>
        <div class="body">
            <p><#if full>${p.text}<#else>${shorten(p.text)}</#if></p>
        </div>
    </article>
</#macro>

<#-- Текст длиннее 350 символов обрезается с "..." -->
<#function shorten text>
    <#if text?length gt 350>
        <#return text?substring(0, 350) + "...">
    </#if>
    <#return text>
</#function>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function findIndBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item?index/>
        </#if>
    </#list>
    <#return -1/>
</#function>
