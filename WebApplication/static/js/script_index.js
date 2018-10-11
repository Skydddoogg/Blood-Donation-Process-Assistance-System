$('.menu div').each(function (e) {
    if (e == 0) {
        $(this).addClass("active");
    }
});
var checkListNull = $(".userList > table > tbody > tr").html() == null;
if (checkListNull) {
    $(".noneResult").css({ "display": "block" });
} else {
    $(".noneResult").css({ "display": "none" });
}