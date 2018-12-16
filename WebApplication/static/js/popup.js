$( document ).ready(function() {
    $(".popupButton").on('click',function(){
        $('.overlay').css({'display':'block'});
        $('.addProfilePopup').fadeIn();
    });

    $(".overlay").on('click',function(){
        $('.overlay').css({'display':'none'});
        $('.addProfilePopup').css({'display':'none'});
    });

    $(".closeOverlay").on('click',function(){
        $('.overlay').css({'display':'none'});
        $('.addProfilePopup').css({'display':'none'});
    });
});