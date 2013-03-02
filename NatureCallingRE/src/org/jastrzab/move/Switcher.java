/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jastrzab.move;

/**
 *
 * @author Jastrzab
 */
public class Switcher {

	private volatile boolean mSwitch = false;

	public void setSwitchOn() {
		mSwitch = true;
	}

	public void setSwitchOff() {
		mSwitch = false;
	}

	public boolean isSwitchOn() {
		return mSwitch;
	}

}
