var Slider = function(containerSelector, opt) {
    this.context = $(containerSelector);
    this.options = {interval: 2000, numbers: true};
    this.numPieces = 0;
    this.play = null;
    
    if(opt !== undefined) {
        if (opt['interval'] !== undefined) {
            this.options['interval'] = opt['interval'];
        }
        
        if (opt['numbers'] !== undefined) {
            this.options['numbers'] = opt['numbers'];
        }
    }
}

// Prototype alias
Slider.fn = Slider.prototype;

/**
 * Gets the number of pieces in the slider
 */
Slider.fn.getNumPieces = function(){
    var numPieces = $('.reel > .piece', this.context).size();
    return numPieces;
};


/**
 * Changes page in the slider
 */
Slider.fn.changePage = function(index){
    var curPage = $('.active', this.context).attr('data');
    var numPieces = this.numPieces;

    // Removes active from current page
    $('.page:eq('+(curPage-1)+')', this.context).removeClass('active');
    curPage = (index-1) % numPieces + 1;

    $('.page:eq('+(curPage-1)+')', this.context).addClass('active');
};

/**
 * Changes image in the slider, by animating the reel
 */
Slider.fn.slide = function(index, context){
    console.log('Rotates '+this.context.attr('id'));
    var left = $('.piece:eq('+(index-1)+')', this.context).position().left;

    $('.reel', this.context).stop(true,true).animate({
            left: -left+'px'
        }, 500);
};

/**
 * Rotates the piece in the slider
 */
Slider.fn.rotate = function(context){
    
    var curPage = $('.active', this.context).attr('data');
    var numPieces = context.numPieces;
    
    curPage = (curPage) % numPieces + 1;
    
    context.changePage(curPage);
    context.slide(curPage);
};


/**
 * Starts the slider
 */
Slider.fn.start = function(){
    console.log('Start Slider');
    
    // store reference to the slider object
    var self = this;

    var reelWidth = 0;

    // Change width of reel
    $('.reel > .piece', this.context).each(function() {
        reelWidth += $(this).width();
    });

    $('.reel', this.context).width(reelWidth);

    // Number of Pieces
    this.numPieces = this.getNumPieces(this.context);
    console.log('size '+this.numPieces);

    // Gets pages div
    var pagesContainer = $('.pages', this.context);

    // Creates dynamic pages
    for (i=0; i<this.numPieces; i++) {
        var divText = "&nbsp;";
        if (this.options['numbers'] == true) {
            divText = ' '+(i+1)+' ';
        }
        
        var div = $('<div/>', {
                id: 'page'+(i+1),
                html: divText
            });
        div.addClass('page');
        div.attr('data', (i+1).toString());

        pagesContainer.append(div);
    }

    // Selects first piece
    if (this.numPieces > 0) {
        pagesContainer.find('.page:eq(0)').addClass('active');
    }
    
    // Sets interval for rotation
    function doRotate() {
        console.log("Resume");
        self.play = setInterval(function() { self.rotate(self); }, self.options['interval']); 
    }
    
    // Removes rotation interval
    function stopRotate() {
        console.log("Stop");
        clearInterval(self.play);
    }
    
    // Event handler for click on page
    pagesContainer.find(".page").click(function() {
        var targetPage = $(this).attr('data');

        stopRotate();

        self.changePage(targetPage);
        self.slide(targetPage);
        
        doRotate();
    });

    // On Hover
    $(".window", this.context).hover(function() {
        stopRotate();
    }, function() {
        doRotate()
    });

    
    // Start up Animation
    console.log('Start Animation Slider');
    doRotate();
};
