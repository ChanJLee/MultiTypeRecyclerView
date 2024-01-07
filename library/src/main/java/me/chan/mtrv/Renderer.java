package me.chan.mtrv;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class Renderer<V extends ViewBinding, D extends Data> {
	private Data mData;

	private final V mBinding;

	Renderer(V binding) {
		mBinding = binding;
	}

	void bind(Data data) {
		mData = data;
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
	protected D getData() {
		return (D) mData;
	}

	View getRoot() {
		return mBinding.getRoot();
	}

	public final void render(Data data) {
		onRender((D) data);
	}

	protected abstract void onRender(D data);
}
