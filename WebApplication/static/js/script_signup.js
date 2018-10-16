$('.menu div').each(function (e) {
    if (e == 1) {
        $(this).addClass("active");
    }
});

$('.nextStep').on('click', function () {
    var checkInputStep1 = true;
    $('input[required]').each(function (e) {
        if ($(this).attr('step') == '1') {
            if ($(this).val() == '') {
                checkInputStep1 = false
                $(this).focus();
            }
        }
    });
    if (checkInputStep1) {
        validateAvailableId().then(function (response) {
            if (response.data) {
                $('.step1').fadeOut();
                $('.step2').fadeIn();
            } else {
                alert("เลขบัตรประชาชนนี้ได้ลงทะเบียนไปแล้ว");
            }
            return response.data
        })
        .catch(function (error) {
            console.log(error);
        });
    } else {
        alert("กรุณากรอกข้อมูลที่สำคัญให้ครบถ้วน");
    }
});

$('.backStep').on('click', function () {
    $('.step1').fadeIn();
    $('.step2').fadeOut();
});


$('#register').submit(function () {
    var checkInputStep2 = false;
    if (validatePasswordLenght()) {
        if (validateConfirmPassword()) {
            checkInputStep2 = true;
            alert('ระบบกำลังดำเนินการลงทะเบียน และจะกลับไปที่หน้าแรกโดยอัตโนมัติเมื่อดำเนินการเสร็จสิ้น');
        } else {
            alert("Password ไม่ตรงกัน");
        }
    } else {
        alert("Password ต้องมี 6 ตัวอักษร เป็นอย่างต่ำ");
        checkInputStep2 = false;
    }
    return checkInputStep2;
})


function validatePasswordLenght() {
    var validate = true;
    $('input[required]').each(function (e) {
        if ($(this).attr('step') == '2') {
            if ($(this).val().length < 6) {
                $(this).focus();
                validate = false;
            }
        }
    });
    return validate;
}

function validateConfirmPassword() {
    var validate = $('.password_again').val() == $('.password').val();
    if (!validate) {
        $('.password').focus();
    }
    return validate;
}

function validateAvailableId() {
    return axios.post('/validateAvailableId', {idcard: $('.idcard').val()})
}
