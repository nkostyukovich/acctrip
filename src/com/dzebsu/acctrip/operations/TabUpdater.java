package com.dzebsu.acctrip.operations;

public interface TabUpdater {

	public void registerTab(TabUpdateListener tab);

	public void updateTabs();

	public void unregisterTab(TabUpdateListener tab);
}
