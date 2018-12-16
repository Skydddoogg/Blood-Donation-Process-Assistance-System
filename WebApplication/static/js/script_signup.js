$('.menu div').each(function (e) {
    if (e == 1) {
        $(this).addClass("active");
    }
});

let statusLoading = true

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
        if (validateCardNumberLength()) {
        validateAvailableId().then(function (response) {
            if (response.data) {
                $('.step1').fadeOut();
                $('.step2').fadeIn();
            } else {
                alert("เลขบัตรประชาชนนี้ได้ลงทะเบียนไปแล้ว");
            }
            statusLoading = false

            if(!statusLoading){
                $('.loading').fadeOut();
            }
            
            return response.data
        })
        .catch(function (error) {
            console.log(error);
        });
        }else{
            statusLoading = true;
            alert("กรุณากรอกเลขบัตรประชาชนให้ครบ 13 หลัก");
        }
    } else {
        statusLoading = true;
        alert("กรุณากรอกข้อมูลที่สำคัญให้ครบถ้วน");
    }
});

$('.backStep').on('click', function () {
    $('.step1').fadeIn();
    $('.step2').fadeOut();
    statusLoading = true;
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


//Input Only Number Function for idCard Field
$(".idcard").keypress(function (e) {
    var charCode = (e.which) ? e.which : e.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
});

function validateCardNumberLength() {
    var cardNumberLenght = $('input[name="idcard"]').val().length
    if (cardNumberLenght == 13) {
        return true
    } else {
        return false
    }
}

function validateConfirmPassword() {
    var validate = $('.password_again').val() == $('.password').val();
    if (!validate) {
        $('.password').focus();
    }
    return validate;
}

function validateAvailableId() {
    if(statusLoading){
        $('.loading').css({'display':'flex'})
        $('.loading').fadeIn();
    }
    return axios.post('/validateAvailableId', {idcard: $('.idcard').val()})
    
}
