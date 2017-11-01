package com.navarone.vo;

/**
 * @author Craig<br/>
 *         A value object for which if you buy 'buy' quantity you only pay for
 *         'pay' quantity, where 'pay' < 'buy'
 *
 */
public class BuyPayVO {

	private int buy;

	private int pay;

	public BuyPayVO(int buy, int pay) {
		super();
		this.buy = buy;
		this.pay = pay;
	}

	public int getBuy() {
		return buy;
	}

	public void setBuy(int buy) {
		this.buy = buy;
	}

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}
}
