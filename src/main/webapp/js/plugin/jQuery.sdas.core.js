;(function($){
    $.fn.myform = function(options){
        options = $.extend({
            target:"myform"
        },options);
        $("#"+options.target).ajaxSubmit(function(message){

        });
    };
})(jQuery)