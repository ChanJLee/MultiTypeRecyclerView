package me.chan.mtrv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Data {

	private final boolean mReuseEnable;

	public Data() {
		this(true);
	}

	/**
	 * @param reuseEnable true if the item view can be reused, false otherwise.
	 */
	public Data(boolean reuseEnable) {
		this.mReuseEnable = reuseEnable;
	}

	public boolean isReuseEnable() {
		return mReuseEnable;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface BindRenderer {

		Class<? extends Renderer<?, ?>> value();
	}
}
