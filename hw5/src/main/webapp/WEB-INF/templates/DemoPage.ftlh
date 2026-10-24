<#import "./common.ftlh" as c>

<@c.page>
    <h1>Demo: поиск action-методов</h1>

    <p>${message}</p>

    <ul>
        <li><a href="/demo">/demo</a>: action(request, view) в предке DemoBase (private)</li>
        <li><a href="/demo?action=fromAncestor">/demo?action=fromAncestor</a>:
            fromAncestor(view, request) в предке, параметры в обратном порядке
        </li>
        <li><a href="/demo?action=greet">/demo?action=greet</a>:
            default-метод greet(view) интерфейса Greeter, который подключил предок
        </li>
        <li><a href="/demo?action=ownAction">/demo?action=ownAction</a>: ownAction(view) в самой DemoPage</li>
        <li><a href="/demo?action=requestOnly">/demo?action=requestOnly</a>:
            requestOnly(request), один параметр HttpServletRequest, ответом будет перенаправление на ownAction
        </li>
        <li><a href="/demo?action=toString">/demo?action=toString</a>: методы Object не вызываются, будет 404</li>
        <li><a href="/demo?action=unknown">/demo?action=unknown</a>: такого метода нет, будет 404</li>
    </ul>
</@c.page>
