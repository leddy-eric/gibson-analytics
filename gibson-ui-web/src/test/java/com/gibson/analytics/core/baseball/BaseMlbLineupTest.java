package com.gibson.analytics.core.baseball;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseMlbLineupTest {
	AtomicInteger order = new AtomicInteger(1);
	List<MlbPlayer> players = new ArrayList<>();
	
	public void addPlayer(String name, String position, double bsr, double def, double obp, double slg) {
		players.add(new MlbPlayer.Builder()
						.add(order.getAndIncrement(), name, position)
						.bsr(bsr)
						.def(def)
						.obp(obp)
						.slg(slg)
						.build());
	}
	
	public MlbLineup getTestLineup() {
		MlbLineup lineup =  new MlbLineup();
		lineup.setLineup(players);
		
		return lineup;		
	}

}
