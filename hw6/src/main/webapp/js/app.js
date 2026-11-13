// Всплывающее уведомление в правом нижнем углу.
window.notify = function (message, isError) {
    $.notify(message, {
        position: "right bottom",
        className: isError ? "error" : "success"
    });
};
function ajax(url, data) {
    return $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: "json"
    }).done(function (response) {
        if (response.redirect) {
            window.location.href = response.redirect;
        }
    });
}