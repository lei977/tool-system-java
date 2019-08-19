package com.drore.cloud.tdp.common.util;

public class RandomNumber {
	public static int Number6() {
		int n = 0;
		while (n < 100000) {
			n = (int) (Math.random() * 1000000);
		}
		return n;
	}
}
