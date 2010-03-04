function OddsCalculatorController() {
    this.currentStatsKey = null;
    this.jsonrpc = null;
    
	this.jsonrpc = new JSONRpcClient(function (result, exception) {
		if (exception) {
			alert("Could not initialize JSON RPC client:\n" + exception);
		}
	}, "/JSON-RPC");
}

OddsCalculatorController.prototype.initialize = function () {
    var me = this; // add a global reference to the this object

    // wire up event handlers
    $('#deck .card').click(function () {
        me.onDeckCardClicked(this);
        me.updateStatistics();
    });

    $('#table .card').click(function () {
        me.onTableCardClicked(this);
    });

    $('#reset')
        .click(function () {
            me.reset();
        });

    // reset the table
    me.reset();
    $('#deck, #table').css('visibility', 'visible');
}

OddsCalculatorController.prototype.reset = function () {
    $('.odds').addClass('hidden'); // hide all odds

    $('#table .card') // for all the table cards
        .attr('card-value', '') // remove the card value
        .removeClass('selected hidden') // reset the image state
        .filter('#players .card').first().addClass('selected'); // select the first player card
    $('#deck .card') // for all deck cards
        .animate({ left: 0, top: 0}, 200, null); // animate the cards to the deck
	
	$('#reset').addClass('disabled');
}

OddsCalculatorController.prototype.setNextPosition = function (card) {
    $('#table .card').removeClass('selected'); // make sure no card is selected.
    if (!$('#table .card[card-value=""]').length) return; // the table is completely filled

    // try to select the next card on the current postion.
    var next = $(card).parent().find('[card-value=""]').first(); // find the next 'empty' card on the current position 
    if (next.length) {
        next.addClass('selected');
        return;
    }
     
    if ($('#community .card[card-value=""]').length == 5 && $('#players .card[card-value!=""]').length == 4) {
        $('#community .card[card-value=""]').first().addClass('selected');
    }
    else if ($('#players .player .card[card-value=""]').length) {
        // move clockwise around the table to fill the gaps, starting at the position left to the current position.
        var id = $(card).parent().attr('id');
        var positions = $('#players .player');
        var position = positions.filter('#' + id);

        next = (position.next().length) ? position.next() : positions.first();
        while (next.attr('id') != id) {
            if (next.find('.card[card-value=""]').length) {
                next.find('.card[card-value=""]').first().addClass('selected');
                return;
            }
            next = (next.next().length) ? next.next() : positions.first();
        }
    }
    else {
        $('#community .card[card-value=""]').first().addClass('selected');
    }
}

OddsCalculatorController.prototype.onDeckCardClicked = function (card) {
    var card = $(card);
    if (!$('#table .card[card-value=' + card.attr('card-value') + ']').length) {
        var selected = $('#table .card.selected'); // get the selected card
        if (!selected.length) return; // no card selected on the deck
        selected.removeClass('selected').addClass('hidden'); // clear the selected status and hide the card
        selected.attr('card-value', card.attr('card-value')); // copy the card value to the card on the table

        card // on the deck card
            .animate({ // animate the card to the position of the selected card on the table
                left: selected.offset().left - card.offset().left,
                top: selected.offset().top - card.offset().top
            }, 200, null);

        this.setNextPosition(selected); // set the 'selected' indicator to the next position
    }
    else {
        $('#table .card.selected').removeClass('selected');
        $('#table .card[card-value=' + card.attr('card-value') + ']')
            .addClass('selected').removeClass('hidden')
            .attr('card-value', '');
        card // on the deck card
            .animate({ // animate the card to back to the deck
                left: 0,
                top: 0
            }, 200, null);
    }
    if ($('#table .card[card-value!=""]').length)
    {
		$('#reset').removeClass('disabled');
	}
	else
	{
		$('#reset').addClass('disabled');
	}
}

OddsCalculatorController.prototype.onTableCardClicked = function (card) {
    var card = $(card);
    if (!card.attr('card-value').length) {
        $('#table .card').removeClass('selected');
        card.addClass('selected');
    }
}

OddsCalculatorController.prototype.updateStatistics = function () {
    
    if (this.jsonrpc == null)
    	return;
    
    var players = [];
    var holes = [];
    var community = [];

    // get all players with 2 cards
    $('#table .player').each(function (index) {
        var cards = $(this).find('.card[card-value]');
        if (cards.length == 2) {
            players.push($(this).attr('id'));
            holes.push(cards.first().attr('card-value') + ',' + cards.next().attr('card-value'));
        }
    });

    // determine flop
    var flop = $('#community .card.flop[card-value!=""]');
    if (flop.length == 3) {
        // push flop cards
        flop.each(function (index) {
            community.push($(this).attr('card-value'));
        });

        // determine turn
        var turn = $('#community .card.turn[card-value!=""]');
        if (turn.length == 1) {
            // push turn card
            community.push(turn.attr('card-value'));
            // determine river
            var river = $('#community .card.river[card-value!=""]');
            if (river.length == 1) {
                // push river card
                community.push(river.attr('card-value'));
            }
        }
    }

    this.setStatistics(players, holes, community);
}

OddsCalculatorController.prototype.setStatistics = function (players, holes, community) {
    
    var newKey = players.toString() + '|' + holes.toString() + '|' + community.toString();
	if (this.currentStatsKey != newKey) {
    	this.currentStatsKey = newKey;

        $('.odds').addClass('hidden'); // hide all odds

        if (players.length < 2) return;
		
		this.jsonrpc.holdem.calculateOdds(function (result, error) {
			if (error) {
				window.alert(error.msg);
			}
			else {
				for (var i = 0; i < players.length; i++) {
                    var total = 0;
                    for (var j = 0; j < result[i].length; j++) {
                        total += result[i][j];
                    };

                    var win = (Math.round(result[i][1] * 10000 / total) / 100) + '%';
                    var loss = (Math.round(result[i][0] * 10000 / total) / 100) + '%';
                    var tie = (Math.round((total - result[i][0] - result[i][1]) * 10000 / total) / 100) + '%';

                    var odds = $('#' + players[i] + ' .odds');
		            odds.find('.win').text('Win: ' + win);
		            odds.find('.loss').text('Loss: ' + loss);
		            odds.find('.tie').text('Tie: ' + tie);
		            odds.removeClass('hidden');
                }
            }
        }, holes, community);
    }
}

$(function () {

    $.extend($.fn.disableTextSelect = function () {
        return this.each(function () {
            if ($.browser.mozilla) {//Firefox
                $(this).css('MozUserSelect', 'none');
            } else if ($.browser.msie) {//IE
                $(this).bind('selectstart', function () { return false; });
            } else {//Opera, etc.
                $(this).mousedown(function () { return false; });
            }
        });
    });
    $('#table, #deck, #reset').disableTextSelect(); // disable text selection

	new OddsCalculatorController().initialize(); // initialize the controller
});