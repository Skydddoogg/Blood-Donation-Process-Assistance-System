$( document ).ready(function(){
    $('.pages').each(function (e) {
        var parentAttr = $(this).attr("page");
        $('.menu > div').each(function(){
            var childAttr = $(this).attr("page");
            if(childAttr == parentAttr){
                $(this).addClass("active");
            }
        })
    });
});