package com.yves.spring.service;

import org.springframework.beans.factory.annotation.Autowired;

public class LoveServiceImpl implements LoveService {

	@Autowired
	private CombatService combatService;

	@Override
	public void doLove() {
		combatService.doInit();
		combatService.combating();
		combatService.doClean();
	}

}
