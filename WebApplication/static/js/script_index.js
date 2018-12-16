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

//Input Only Number Function for idCard Field
$("input[name='required_id_number']").keypress(function (e) {
    var charCode = (e.which) ? e.which : e.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
});