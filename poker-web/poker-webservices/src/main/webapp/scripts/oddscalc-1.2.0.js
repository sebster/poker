$(function () {
    $('#table, #deck, #reset').disableTextSelect();
    
    var o = new OddsController();
    $('#selector')
	    .change(function () { o.initialize($(this).val()); })
		.change();
});

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

function OddsController() {
    me = this;
    
    try
    {
        this.jsonrpc = new JSONRpcClient(function (result, exception) {
            if (exception) {
                alert("Could not initialize JSON RPC client:\n" + exception);
            }
        }, "/JSON-RPC");
    }
    catch(e) {}

    $('#deck .card').click(function () {
        me.onDeckCardClicked(this);
        me.updateStatistics();
    });

    $('#community .card').click(function () { me.onTableCardClicked(this); });
    $('#reset').click(function () { me.reset(); });
}

OddsController.prototype.settings = { game: null, seats: null, holecards: null };
OddsController.prototype.currentStatsKey = null;
OddsController.prototype.jsonrpc = null;

OddsController.prototype.initialize = function (type) {
    var s = this.settings;
    switch (type) {
        case 'holdem-hu': // holdem, 2 seats
            s.game = 'holdem';
            s.seats = ['s9', 's4'];
            s.holecards = 2;
            break;
        case 'omaha': // omaha, 6 seats
            s.game = 'omaha';
            s.seats = ['s1', 's2', 's4', 's6', 's7', 's9'];
            s.holecards = 4;
            break;
        default: // holdem, 10 seats
            s.game = 'holdem';
            s.seats = ['s1', 's2', 's3', 's4', 's5', 's6', 's7', 's8', 's9', 's10'];
            s.holecards = 2;
            break;
    }
    
    var seatHtml = null;
    if (s.game == 'holdem') {
        seatHtml = '<div class="seat"><div class="cardspace"/><div class="card"/><div class="card"/><div class="cardspace"/></div>';
    } else if (s.game == 'omaha') {
        seatHtml = '<div class="seat"><div class="card"/><div class="card"/><div class="card"/><div class="card"/></div>';
    }

    var seats = $('#seats');
    seats.html('');
    for (var i = 0; i < s.seats.length; i++) {
        seats.append(
        $(seatHtml)
            .attr('id', s.seats[i])
            .append('<div class="odds"><div class="win"/><div class="tie"/></div>')
        );
    }

    seats.find('.card').click(function () { me.onTableCardClicked(this); });
    me.reset();
}

OddsController.prototype.reset = function () {
    $('.odds').addClass('hidden'); // hide all odds
    $('#table .card')
        .attr('card-value', '') // reset the card values of the board cards
		.removeClass('selected hidden') // reset the image state
    	.filter('#seats .card').first().addClass('selected'); // select the first card
    $('#deck .card').animate({ left: 0, top: 0 }, 200, null); // animate the cards to the deck
    $('#reset').addClass('disabled');

    this.currentStatsKey = null;
}

OddsController.prototype.onDeckCardClicked = function (card) {
    var $card = $(card);
    var $tablecards = $('#table .card');
    if (!$tablecards.filter('[card-value=' + $card.attr('card-value') + ']').length) {
        var $selected = $tablecards.filter('.selected'); // get the selected card
        if (!$selected.length) return; // no card selected on the table
        $selected.removeClass('selected'); // clear the selected status
        $selected.attr('card-value', $card.attr('card-value')); // copy the card value to the card on the table

        $card // using the deck card
        .animate({ // animate it to the position of the selected card on the table
            left: $selected.offset().left - $card.offset().left,
            top: $selected.offset().top - $card.offset().top
        }, 200, function () {
            $selected.addClass('hidden'); // hide the card	
        });
        me.setNextPosition($selected); // set the 'selected' indicator to the next position		
    } else {
        $tablecards // using the table cards
        .removeClass('selected') // remove the selected value 
        .filter('[card-value=' + $card.attr('card-value') + ']')
            .addClass('selected').removeClass('hidden')
            .attr('card-value', '').end();

        $card // using the deck card
        .animate({ // animate it back to the deck
            left: 0,
            top: 0
        }, 200, null);
    }

    if ($tablecards.filter('[card-value!=]').length) {
        $('#reset').removeClass('disabled');
    }
    else {
        $('#reset').addClass('disabled');
    }
}

OddsController.prototype.onTableCardClicked = function (card) {
    var $card = $(card);
    if (!$card.attr('card-value').length) {
        $('#table .card').removeClass('selected');
        $card.addClass('selected');
    }
}

OddsController.prototype.setNextPosition = function (card) {
    var $card = $(card);
    var $tablecards = $('#table .card');
    $tablecards.removeClass('selected'); // make sure no card is selected.
    if (!$tablecards.filter('[card-value=]').length) return; // the table is completely filled

    // try to select the next card on the current postion.
    var $next = $card.parent().find('[card-value=]').first(); // find the next 'empty' card on the current position 
    if ($next.length) {
        $next.addClass('selected');
        return;
    }

    var $unassignedcommunitycards = $('#community .card[card-value=]');
    var $seatcards = $('#seats .card');

    if ($unassignedcommunitycards.length == 5 && $seatcards.filter('[card-value!=]').length == me.settings.holecards * 2) {
        $unassignedcommunitycards.first().addClass('selected');
    }
    else if ($seatcards.filter('[card-value=]').length) {
        var id = $($card).parent().attr('id');
        var $seats = $('#seats .seat');
        var $current = $seats.filter('#' + id);

        $next = ($current.next().length) ? $current.next() : $seats.first();
        while ($next.attr('id') != id) {
            if ($next.find('[card-value=]').length) {
                $next.find('[card-value=]').first().addClass('selected');
                return;
            }
            $next = ($next.next().length) ? $next.next() : $seats.first();
        }
    }
    else {
        $unassignedcommunitycards.first().addClass('selected');
    }
}

OddsController.prototype.updateStatistics = function () {
    if (this.jsonrpc == null)
        return;

    var seats = [];
    var holes = [];
    var community = [];

    // get all seats with all cards selected
    $('#seats .seat').each(function () {
        var seat = $(this);
        if (seat.find('.card[card-value=]').length == 0) {
            var cards = seat.find('.card[card-value]');
            var hole = cards.map(function () { return $(this).attr('card-value'); }).get().join(',');
            holes.push(hole);
            seats.push(seat.attr('id'));
        }
    });

    // determine flop
    var flop = $('#community .card.flop[card-value!=]');
    if (flop.length == 3) {
        // push flop cards
        flop.each(function () {
            community.push($(this).attr('card-value'));
        });

        // determine turn
        var turn = $('#community .card.turn[card-value!=]');
        if (turn.length == 1) {
            // push turn card
            community.push(turn.attr('card-value'));
            // determine river
            var river = $('#community .card.river[card-value!=]');
            if (river.length == 1) {
                // push river card
                community.push(river.attr('card-value'));
            }
        }
    }

    this.getStatistics(seats, holes, community);
}

OddsController.prototype.getStatistics = function (seats, holes, community) {

    var newKey = seats.toString() + '|' + holes.toString() + '|' + community.toString();
    if (this.currentStatsKey != newKey) {
        this.currentStatsKey = newKey;

        $('.odds').addClass('hidden'); // hide all odds

        if (seats.length >= 2) {
            switch (this.settings.game) {
                case 'holdem':
                    this.jsonrpc.holdem.calculateOdds(function (result, error) { me.setStatistics(result, error, seats); }, holes, community);
                    break;
                case 'omaha':
                    this.jsonrpc.omaha.calculateOdds(function (result, error) { me.setStatistics(result, error, seats); }, holes, community);
                    break;
            }
        }
    }
}

OddsController.prototype.setStatistics = function (result, error, seats) {                       
    if (error) {
        window.alert(error.msg);
    }
    else {
        for (var i = 0; i < seats.length; i++) {
            var total = 0;
            for (var j = 0; j < result[i].length; j++) {
                total += result[i][j];
            };

            var win = (Math.round(result[i][1] * 10000 / total) / 100) + '%';
            var loss = (Math.round(result[i][0] * 10000 / total) / 100) + '%';
            var tie = (Math.round((total - result[i][0] - result[i][1]) * 10000 / total) / 100) + '%';

            var odds = $('#' + seats[i] + ' .odds');
            odds.find('.win').text('Win: ' + win);
            odds.find('.loss').text('Loss: ' + loss);
            odds.find('.tie').text('Tie: ' + tie);
            odds.removeClass('hidden');
        }
    }
}