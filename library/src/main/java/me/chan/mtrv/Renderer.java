package me.chan.mtrv;

import androidx.annotation.Nullable;

public abstract class Renderer<T extends Type> {
	private T mLastData;

	void bind(T data) {
		mLastData = data;
	}

	void attach() {
		onAttached();
	}

	protected void onAttached() {

	}

	void detach() {
		onDetached();
	}

	protected void onDetached() {

	}

	@Nullable
	protected T getBindData() {
		return mLastData;
	}
}
