var timer;
var gauge;			

$( document ).ready(function() {
	$.ionTabs("#tabs_1", {
		type: "none",
		onChange: function(obj){
			console.log(obj);
		}
	});
});

window.onload = function(){

	//save the default description text
	var desc_text = $("#description").find("p").html();

	
	//main gauge
	var gauge = new JustGage({
		id: "score_gauge",
		value: 78, 
		min: 0,
		max: 100,
		title: " ",
		label: "Score",
		shadowOpacity: 1,
		levelColorsGradient: true,
		gaugeColor: "#eee",
		levelColors: ["#ff0000", "#ffff00", "#00d200"],
		startAnimationTime: 1200,
		startAnimationType: "bounce"              
	});
	
	
	//Small circle 1
	$('.circle.first').circleProgress({
		value: 0.89,
		animation: true,
		size: 33,
		startAngle: -Math.PI/2,
		thickness: 4,
		fill: { gradient: ['#64d200', '#64e200'] }
	}).on('circle-animation-progress', function(event, progress, stepValue) {
		$(this).find('strong').text(String(stepValue.toFixed(2)).substr(2));
	});
	
	
	//Small circle 2
	$('.circle.second').circleProgress({
		value: 0.25,
		animation: true,
		size: 33,
		thickness: 4,
		startAngle: -Math.PI/2,
		fill: { gradient: ['#ee5500', '#ee5500'] }
	}).on('circle-animation-progress', function(event, progress, stepValue) {
		$(this).find('strong').text(String(stepValue.toFixed(2)).substr(2));
	});
	
	
	//Small circle 3
	$('.circle.third').circleProgress({
		value: 0.52,
		animation: true,
		size: 33,
		thickness: 4,
		startAngle: -Math.PI/2,
		fill: { gradient: ['#eeff00', '#eeff00'] }
	}).on('circle-animation-progress', function(event, progress, stepValue) {
		$(this).find('strong').text(String(stepValue.toFixed(2)).substr(2));
	});

	
	//range slider initialize
	$("#slider").noUiSlider({
		range: {
			min: 0,
			max: 100
		},
		step: 5,
		connect: "lower",
		start: 50
	});
	
	
	//range slider markers
	$("#slider").noUiSlider_pips({
		mode: 'positions',
		values: [0,25,50,75,100],
		density:5
	});				
	
	
	//range slider tooltip
	$("#slider").Link('lower').to('-inline-<div class="tooltip" style="display:none;"></div>', function (value) {
		$(this).html(
			'<span>' + Math.round(value) + '%</span>'
		);
	});
	
	
	//range slider tooltip animations
	$(".noUi-handle").on({
		"mouseover": function() {
			clearTimeout(timer);
			timer = setTimeout(function() {
				$(".tooltip").fadeIn();
			}, 200);
		}, "mouseout": function() {
			clearTimeout(timer);
			timer = setTimeout(function() {
				$(".tooltip").fadeOut();
			}, 800);
		}
	});
	
	
	//hide comment area and buttons
	$("#comment-div").hide();
	$("#save-comment-btn").hide();
	$("#cancel-comment-btn").hide();
	$("#category-2").hide();
	
	
	//hide checkboxes and show comment-area
	$("#comment-btn").click(function() {
		$(".checkbox-div").hide();
		$("#submit-rating-btn").hide();
		$("#save-comment-btn").show();
		$("#cancel-comment-btn").show();
		$(this).hide();
		$("#comment-div").show();
		$("#comment-textarea").focus();		
	});

	
	//hide comment-area and delete comment-text
	$("#cancel-comment-btn").click(function() {
		$("#comment-textarea").val("");
		$("#comment-btn").html("Leave a Comment");
		close_comments();
	});
	
	
	//hide comment-area and save comment-text
	$("#save-comment-btn").click(function() {
		if ($("#comment-textarea").val() != "") {
			$("#comment-btn").html("Edit Comment");
		}
		close_comments();
	});
	
	
	//Show description on mouseover
	$("#score_gauge").find("tspan").on({
		"mouseover": function() {
			$("#description").find("p").html("Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius.");
		}, "mouseout": function() {
			$("#description").find("p").html(desc_text);
		}
	});
	
	
	//Show description on mouseover
	$(".first.circle").on({
		"mouseover": function() {
			$("#description").find("p").html("Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.");
		}, "mouseout": function() {
			$("#description").find("p").html(desc_text);
		}
	});
	
	
	//Show description on mouseover
	$(".second.circle").on({
		"mouseover": function() {
			$("#description").find("p").html("Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem.");
		}, "mouseout": function() {
			$("#description").find("p").html(desc_text);
		}
	});
	
	
	//Show description on mouseover
	$(".third.circle").on({
		"mouseover": function() {
			$("#description").find("p").html("Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum.");
		}, "mouseout": function() {
			$("#description").find("p").html(desc_text);
		}
	});
	
	
	//Call the category-toggle on range-slider change
	$("#slider").on("change", function(){
		toggleCategory();
	});
	
	$("#user_comments").mCustomScrollbar({
		theme: "dark-2",
		setLeft: 0
	});
};


//The category-toggle function
function toggleCategory() {
	if (!$("#comment-textarea").is(":visible")) {
		if ($("#slider").val() <= 50) {
			$("#category-2").hide();
			$("#category-1").show();
		} else {
			$("#category-1").hide();
			$("#category-2").show();
		}
	}
}


//checkboxes <-> comments toggle function
function close_comments() {
	$("#save-comment-btn").show();
	$("#cancel-comment-btn").hide();
	$("#save-comment-btn").hide();
	$("#comment-div").hide();
	$(".checkbox-div").show();
	$("#submit-rating-btn").show();
	$("#comment-btn").show();
	toggleCategory();
}