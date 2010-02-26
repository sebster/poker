package com.sebster.poker.stt.web;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.stereotype.Controller;

import com.sebster.poker.stt.web.pages.HomePage;
import com.sebster.poker.stt.web.pages.equity.EquityHomePage;

@Controller("wicketApplication")
public class PokerSTTApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected void init() {
		super.init();
		mountBookmarkablePage("/equity", EquityHomePage.class);
	}

}
