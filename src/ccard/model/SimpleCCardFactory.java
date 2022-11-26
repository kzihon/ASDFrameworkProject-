package ccard.model;

import ccard.interfaces.CCardFactory;

public class SimpleCCardFactory {
	static public CCardFactory getGoldCCardFactory() {
		return new GoldCCardFactory();
	}
	static public CCardFactory getSilverCCardFactory() {
		return new SilverCCardFactory();
	}
	static public CCardFactory getBronzeCCardFactory() {
		return new BronzeCCardFactory();
	}
}
