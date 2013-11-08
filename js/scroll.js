var scrolling = function(){
	$('a[href^="#"]').click(function(){  
		var the_id = $(this).attr("href");  
		$('html, body').animate({  
			scrollTop:$(the_id).offset().top - 80 
		}, 'slow');  
		return false;  
	});  
};
jQuery(function(){
	scrolling();
});