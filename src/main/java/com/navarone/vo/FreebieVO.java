package com.navarone.vo;

/**
 * @author Craig</br>
 *         A value object where if you purchase 'buyQty' units of buySku, you
 *         will get 'freeSkuQty' units of freeSku
 *
 */
public class FreebieVO {

	private String buySku;

	private int buyQty;

	private String freeSku;

	private int freeSkuQty;

	public FreebieVO(String buySku, int buyQty, String freeSku, int freeSkuQty) {
		super();
		this.buySku = buySku;
		this.buyQty = buyQty;
		this.freeSku = freeSku;
		this.freeSkuQty = freeSkuQty;
	}

	public int getBuyQty() {
		return buyQty;
	}

	public void setBuyQty(int buyQty) {
		this.buyQty = buyQty;
	}

	public String getFreeSku() {
		return freeSku;
	}

	public void setFreeSku(String freeSku) {
		this.freeSku = freeSku;
	}

	public int getFreeSkuQty() {
		return freeSkuQty;
	}

	public void setFreeSkuQty(int freeSkuQty) {
		this.freeSkuQty = freeSkuQty;
	}

	public String getBuySku() {
		return buySku;
	}

	public void setBuySku(String buySku) {
		this.buySku = buySku;
	}

}
