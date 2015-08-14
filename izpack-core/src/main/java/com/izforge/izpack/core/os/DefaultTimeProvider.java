package com.izforge.izpack.core.os;

import java.util.Date;


public class DefaultTimeProvider implements TimeProvider {

	@Override
	public Date getNow() {
		return new Date();
	}
}
