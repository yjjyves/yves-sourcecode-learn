package com.yves.spring.service;

import org.springframework.stereotype.Component;

@Component
public class Abean {

	public void doSomething() {
		System.out.println(this + " do something .....");
	}
}
