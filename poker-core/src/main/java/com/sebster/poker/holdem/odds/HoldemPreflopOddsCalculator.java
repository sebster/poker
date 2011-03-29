package com.sebster.poker.holdem.odds;

import com.sebster.poker.Hole;
import com.sebster.poker.odds.Odds;

public interface HoldemPreflopOddsCalculator {

	Odds[] calculateOdds(final Hole... holes);

}
