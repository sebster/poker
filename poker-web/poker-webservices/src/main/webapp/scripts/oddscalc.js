
var jsonrpcReady = false;
var jsonrpc = new JSONRpcClient(function(result, exception)
{
 if (exception)
 {
  window.alert("Could not initialize JSON RPC client:\n" + exception);
 }
 else
 {
  jsonrpcReady = true;
 }
}, "/JSON-RPC");

function scalculateOdds()
{
/*
 var hands = jsonCards();
 if (hands)
 {
  $.getJSON("/poker-tools/odds-calculator/texas-holdem", hands, function(data)
  {
   $.each(data, function(player, hand)
   {
    $('#' + player + ' div.odds span.win').text(hand.win_pct);

    $('#' + player + ' div.odds span.tie').text(hand.tie_pct);

    $('#' + player + ' div.odds').css('visibility', 'visible');

   });
  });
 }
*/

}

function jsonCards(community)
{
 var hands = {};
 var players = [];
 var holes = [];
 var handCount = 0;

 if (community)
 {
   return handVals('community');

 }
 else
 {
  $.each(table.positions, function(i,player) {
   if (player != 'community')
   { 
    cards = handVals(player);
    if (cards.length == 2)
    {
     ++handCount;
     players.push(player);
     holes.push(cards.join(","));
    }
   }
  })
 
  if (handCount > 1)
  {
   hands['holes'] = holes;
   hands['players'] = players;
   return hands;

  }
  else
  {
   return false;

  }

 }
}

function calculateOdds()
{
 if (!jsonrpcReady)
 {
  window.alert("JSON RPC client not ready.");
  return;
 }
 
 var players = jsonCards(false);
 if (!players)
  return;
 var holes = players['holes'];
 var board = jsonCards(true);

  // The calculateOdds function takes a callback (like all JSON-RPC async calls), and two arguments:
  // holes: an array of two-card holes of the form "As,Jh".
  // board (optional): an array of 0, 3, 4, or 5 cards of the form "Tc".

 jsonrpc.holdem.calculateOdds(function(result, error)
 { 
  if (error)
  { 
   alert(error.msg);
  }
  else
  {
   for (var i = 0; i < holes.length; i++)
   {
    var total = 0;
    for (var j = 0; j < result[i].length; j++)
    {
     total += result[i][j];
    };

    var player = players['players'][i];
    var win = (Math.round(result[i][1] * 10000 / total) / 100) + '%';
    var loss = (Math.round(result[i][0] * 10000 / total) / 100) + '%'; 
    var split = (Math.round((total - result[i][0] - result[i][1]) * 10000 / total) / 100) + '%';

    $('#' + player + ' div.odds span.win').text(win);

    $('#' + player + ' div.odds span.tie').text(split);

    $('#' + player + ' div.odds').css('visibility', 'visible');
   }
  }
 }, holes, board);
}

var table = {};
table.positions = ['p1', 'p2', 'community', 'p3', 'p4', 'p5', 'p6', 'p7', 'p8', 'p9', 'p10'];

table.position = 'p1';

function getNextPosition(last)
{
 if (!last)
 {
  last = 'p1';
 }
 // makes an independant copy of the array

 var myPositions = table.positions.slice();
 var idx = table.positions.indexOf(last);
 var front = myPositions.splice(idx);

 myPositions = front.concat(myPositions);
 var originalPosition = myPositions[0];

 var nextPos = 'p1';
 var cardIdx = 0;
 for (var i=0; i < myPositions.length; i++)
 {
  var pos = myPositions[i];
  if ((originalPosition == 'community') && (i > 0))
  {
  // If we start at the board, when we are done we need to start

  // back at seat1 cause seat 1 and seat 2 should always fill before
  // moving on.

  return getNetPosition('p1');
  }
  cardIdx = openCard(pos);
  if (cardIdx != null)
  {
   nextPos = pos;
   break;

  }
 }
 return [nextPos, cardIdx];

}



function openCard(pos)
{
 var cards = $('#' + pos + ' div.card');
 var idx = null;
 for (var i=0; i < cards.length; i++)
 {
  var card = $(cards[i]).attr('card');
  if (card == '' || card == undefined )
  {
   idx = i;
   break;
  }
 }
return idx;

}

function markCurrentPosition(pos, idx)
{
 $('div.selected-position').removeClass('selected-position')
;
 if (!idx)
  idx = openCard(pos);
 $($('#' + pos + ' div.card')[idx]).addClass('selected-position');

}

function setPosition(pos)
{
 table.position = pos;

 markCurrentPosition(table.position);

}


function getCard(card)
{
 var attr = $(card).attr('card');

 if (attr)
 {
  $('#deck div[card=' + attr + ']').css('visibility', 'visible');
  $(card).attr('card', '');
  $(card).removeClass('s d h c').addClass('x');

  $(card).text('');
  return true;
 }
 else
 {
  return false;

 }

}


function handVals(player)
{
 var hand = $.map($('#' + player + ' div.card'), function(v) {return $(v).attr('card')} );
 var ret = [];
 if (player == 'community')
 {
  if ((hand[0] != '' && hand[0] != undefined) && (hand[1] != '' && hand[1] != undefined) && (hand[2] != '' && hand[2] != undefined))
  {
   ret.push(hand[0]);
   ret.push(hand[1]);
   ret.push(hand[2]);

   if ((hand[3] != '' && hand[3] != undefined))
   {
    ret.push(hand[3]);
    if ((hand[4] != '' && hand[4] != undefined))
    {
     ret.push(hand[4]);

    }
   }
  }
 }
 else
 {
  $.each(hand, function(i,val)
  {
   if (val != '')

    ret.push(val);

  })
 }
 return ret;

}

$(function() {

// mark current position
markCurrentPosition(table.position);

// hide odds stats
$('div.player div.odds').css('visibility', 'hidden');

// add click event on player cards and community cards
$('#table div.player div.card, div#community div.card').click(function()
{
 var player = $(this).parent().attr('id');
 if (player)
 {
  var card = getCard(this);
  setPosition(player);
  if (card)
  {
   var hand = handVals(player);
   if ((player == 'community' && hand.length != 5) || hand.length == 1)
   {
    $('div.odds').find('span.win, span.tie').text('');
    $('div.player div.odds').css('visibility', 'hidden');
    calculateOdds();
   }
  }
 }
})


// add click event on deck cards

$('#deck div.card').click(function()
{
 var player = table.position;

 var cardIdx = openCard(table.position);

 if (cardIdx == null)
 {
  // This should mean that all of the cards are already dealt

  return false;

 }

 $(this).css('visibility', 'hidden');

 var card = $($('#' + table.position + ' div.card')[cardIdx]);

 card.attr('card', $(this).attr('card'));
 card.removeClass('x').addClass($(this).attr('card').charAt(1));

 card.text($(this).attr('card').charAt(0));

 try
 {
  table.position = getNextPosition(table.position)[0];

 }
 catch(e)
 {
  table.position = 'p1';
 }

 markCurrentPosition(table.position);

 if (player == 'community')
 {
  var community = handVals('community');
  if (community.length > 2)
  {
   calculateOdds();
  }
 }
 else
 {
  calculateOdds();
 }
});


})


if (!Array.prototype.indexOf)
{
    Array.prototype.indexOf = function(elt /*, from*/)
    {
        var len = this.length >>> 0;

        var from = Number(arguments[1]) || 0;
        from = (from < 0) ? Math.ceil(from) : Math.floor(from);
        if (from < 0)
            from += len;
        
        for (; from < len; from++)
        {
            if (from in this && this[from] === elt)
                return from;
        }
        return -1;
    };
}
