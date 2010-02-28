var jsonrpcReady = false;
var jsonrpc = new JSONRpcClient(function (result, exception) {
    if (exception) {
        window.alert("Could not initialize JSON RPC client:\n" + exception);
    }
    else {
        jsonrpcReady = true;
    }
}, "/JSON-RPC");


function DebugWrite(msg) {
    $("#debug").text(msg);
}

function IsValidCard(card) {
    if (card == null || typeof card != typeof '' || card.length != 2)
        return false;
    return card.match("[AKQJT0-9][shcd]") != null;
}

function ValidateQueryString() {
    // get querystring values
    var h = $.query.get('h');
    var c = $.query.get('c');

    // convert querystring values to array of strings
    h = (typeof h != typeof '' || h.length == 0) ? [] : h.split(',');
    c = (typeof c != typeof '' || c.length == 0) ? [] : c.split(',');

    // when wrong number of hole cards, return false
    if (h.length > 20 || c.length > 5)
        return false;

    // put the arrays together, and validate all cards in the total array
    var x = h.concat(c).sort();
    var previous = null;
    for (var i = 0; i < x.length; i++) {
        var current = x[i];

        // skip empty cards
        if (current.length == 0)
            continue;

        // validate the card
        if (!IsValidCard(current)) {
            DebugWrite('card (' + current + ') is invalid.');
            return false;
        }

        // compare with the previous card in the array to ensure the uniqueness of the cards
        if (previous != null && previous == current) {
            DebugWrite('card (' + current + ') is not unique.');
            return false;
        }
        previous = current;
    }

    DebugWrite('');
    return true;
}

function SetNextPosition(id) {
    // remove class 'selected-position' from the current selected position
    $('div.selected-position').removeClass('selected-position');

    // get all possible positions 
    var positions = $('#table div.position, div#community');
    // find position to select
    var position = positions.filter('#' + id);

    // find all empty 'slots' at this position
    var slots = position.find('div.card[card-value=""]');
    if (slots.length > 0) {
        // select the first empty slot
        slots.first().addClass('selected-position');
        return;
    }
    // this position doesn't have any empty slots.
    // itterate through all positions (starting at the next position) to find the first empty slot.
    // when no slots are empty, the board should be filled completely
    position = position.next().length == 0 ? positions.first() : position.next();
    while (position.attr('id') != id) {
        var slots = position.find('div.card[card-value=""]');
        if (slots.length > 0) {
            // select the first empty slot
            slots.first().addClass('selected-position');
            return;
        }
        position = position.next().length == 0 ? positions.first() : position.next();
    }
}

function Reset() {
    // hide odds stats
    $('div.position div.odds').css('visibility', 'hidden');

    // reset all the deck cards
    var deckCards = $('#deck div.card');
    deckCards.css('visibility', 'visible');

    // reset the board cards
    FlipCard('#table div.position div.card, div#community div.card');

    // set the first position as staring point
    SetNextPosition('p1');
}

function MoveCardFromDeckToBoard(cardValue) {
    var card = $('#deck div.card[card-value=' + cardValue + ']');
    var boardCard = $('div.selected-position');
    if (card.length == 1 && boardCard.length == 1) {
        card.css('visibility', 'hidden');
        boardCard.attr('card-value', cardValue);
        boardCard.removeClass('x').addClass(cardValue.charAt(1));
        boardCard.text(cardValue.charAt(0));
	return true;
    }
    return false;
}

function FlipCard(cards) {
    $(cards).attr('card-value', '');
    $(cards).removeClass('s d c h').addClass('x');
    $(cards).text('');
}

function ReturnCardToDeck(card) {
    var cardValue = $(card).attr('card-value');
    if (cardValue.length == 2) {
        // remove the card from the board
        FlipCard(card);

        // find card in deck and make it visible again
        $('#deck div.card[card-value=' + cardValue + ']').css('visibility', 'visible');
    }
}

function UpdateStatistics() {
    var holes = [];
    var community = [];
    var positions = [];

    // find the filled positions
    $('#table div.position').each(function (index) {
        var cards = $(this).find('div.card[card-value]');
        if (cards.length == 2) {
            holes.push(cards.first().attr('card-value') + ',' + cards.next().attr('card-value'));
            positions.push($(this).attr('id'));
        }
    });

    // get the filled communitycards
    if ($('div#community div.card[card-value!=""]').length >= 3) {
        $('div#community div.card[card-value!=""]').each(function (index) {
            community.push($(this).attr('card-value'));
        });
    }

    var key = holes.toString() + '|' + community.toString();
    if (OddsCalc.StatisticsKey != key) {
        // update the key
        OddsCalc.StatisticsKey = key;

        // hide odds stats
        $('div.position div.odds').css('visibility', 'hidden');

        // get statistics when at least two positions ar filled
        if (holes.length >= 2) {
            // ==================================================
            // sebster code
            // ==================================================
            if (!jsonrpcReady) {
                window.alert("JSON RPC client not ready.");
                return;
            }

            // The calculateOdds function takes a callback (like all JSON-RPC async calls), and two arguments:
            // holes: an array of two-card holes of the form "As,Jh".
            // board (optional): an array of 0, 3, 4, or 5 cards of the form "Tc".
            jsonrpc.holdem.calculateOdds(function (result, error) {
                if (error) {
                    window.alert(error.msg);
                }
                else {
                    for (var i = 0; i < positions.length; i++) {
                        var total = 0;
                        for (var j = 0; j < result[i].length; j++) {
                            total += result[i][j];
                        };

                        var win = (Math.round(result[i][1] * 10000 / total) / 100) + '%';
                        var loss = (Math.round(result[i][0] * 10000 / total) / 100) + '%';
                        var split = (Math.round((total - result[i][0] - result[i][1]) * 10000 / total) / 100) + '%';

                        $('#' + positions[i] + ' div.odds span.win').text(win);
                        $('#' + positions[i] + ' div.odds span.los').text(loss);
                        $('#' + positions[i] + ' div.odds span.tie').text(split);
                        $('#' + positions[i] + ' div.odds').css('visibility', 'visible');
                    }
                }
            }, holes, community);
            // ==================================================
        }
    }
}

var OddsCalc = {};
OddsCalc.StatisticsKey = null;

$(document).ready(function () {
        // reset all
        Reset();

        // disable text selection on the table
	$.extend($.fn.disableTextSelect = function() {
		return this.each(function(){
			if($.browser.mozilla){//Firefox
				$(this).css('MozUserSelect','none');
			}else if($.browser.msie){//IE
				$(this).bind('selectstart',function(){return false;});
			}else{//Opera, etc.
				$(this).mousedown(function(){return false;});
			}
		});
	});

	$('#table').disableTextSelect();

        // add click event on deck cards
        $('#deck div.card').click(function () {
            // move the card from the deck to the board
            if (MoveCardFromDeckToBoard($(this).attr('card-value'))) {
                // set the next possible position
                SetNextPosition($('div.selected-position').parent().attr('id'));

                // update the statistics
                UpdateStatistics();
            }
        })

        // add click event handlers on board cards
        $('#table div.position div.card, div#community div.card').click(function () {
            // return card to deck when the clicked card isn't empty
            ReturnCardToDeck(this);

            // set the 'new' active position
            SetNextPosition($(this).parent().attr('id'));

            // update the statistics
            UpdateStatistics();
        })
    }
);
