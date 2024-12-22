package me.chan.mtrv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTypeAdapter extends RecyclerView.Adapter<MultiTypeAdapter.Vh> {
	private static final boolean DEBUG = false;

	private final LayoutInflater mLayoutInflater;

	private List<Data> mList = new ArrayList<>();

	private final SparseArrayCompat<Factory> mType2FactoryRepo = new SparseArrayCompat<>();

	private final Map<Object, Integer> mTypeRepo = new HashMap<>();

	public MultiTypeAdapter(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public final Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Factory factory = mType2FactoryRepo.get(viewType);
		return new Vh(factory.create(mLayoutInflater, parent));
	}

	@Override
	public final void onBindViewHolder(@NonNull Vh holder, int position) {
		Data data = get(position);
		holder.mRenderer.bind(data);
		holder.mRenderer.render(data);
		if (DEBUG) {
			Log.d("MultiTypeAdapter", "onBindViewHolder: " + position);
		}
	}

	@Override
	public final int getItemCount() {
		return mList.size();
	}

	@Override
	public final void onViewAttachedToWindow(@NonNull Vh holder) {
		super.onViewAttachedToWindow(holder);
		holder.mRenderer.attach();
	}

	@Override
	public final void onViewDetachedFromWindow(@NonNull Vh holder) {
		holder.mRenderer.detach();
		super.onViewDetachedFromWindow(holder);
	}

	@Override
	public final int getItemViewType(int position) {
		Data data = get(position);
		Object key = data.isReuseEnable() ? data.getClass() : data;
		Integer type = mTypeRepo.get(key);
		if (type == null) {
			return registerType(key, data);
		}

		return type;
	}

	private int registerType(Object key, Data data) {
		Class<?> clazz = data.getClass();
		Data.BindRenderer bindRenderer = clazz.getAnnotation(Data.BindRenderer.class);
		if (bindRenderer == null) {
			throw new IllegalArgumentException("MultiTypeAdapter's Data should be annotated by Data.BindRenderer");
		}

		int type = mTypeRepo.size() + 1;
		mTypeRepo.put(key, type);

		Class<?> rendererClazz = bindRenderer.value();
		if (Renderer.class.isAssignableFrom(rendererClazz)) {
			mType2FactoryRepo.put(type, createFactory(rendererClazz));
		}

		return type;
	}

	private Factory createFactory(Class<?> rendererClazz) {
		ParameterizedType parameterizedType = (ParameterizedType) rendererClazz.getGenericSuperclass();
		assert parameterizedType != null;

		Class<?> viewBindingClazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];

		try {
			Method method = viewBindingClazz.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
			method.setAccessible(true);

			Constructor<?> constructor = rendererClazz.getConstructor(viewBindingClazz);
			constructor.setAccessible(true);

			return new Factory(constructor, method);
		} catch (Throwable throwable) {
			throw new RuntimeException("create renderer failed", throwable);
		}
	}

	public void setList(@NonNull List<? extends Data> list) {
		List<Data> copy = new ArrayList<>();
		for (Data data : list) {
			copy.add(data);
		}
		mList = copy;
	}

	public int indexOf(@NonNull Data data) {
		if (mList == null) {
			return -1;
		}

		return mList.indexOf(data);
	}

	public <T extends Data> T get(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IllegalArgumentException("get index out of range");
		}

		return (T) mList.get(index);
	}

	public void update(@NonNull Data data) {
		int index = indexOf(data);
		if (index < 0) {
			return;
		}

		update(index);
	}

	private static final Object SIG_UPDATE = new Object();

	public void update(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IllegalArgumentException("update index out of range");
		}

		notifyItemChanged(index, SIG_UPDATE);
	}

	public void remove(@NonNull Data data) {
		int index = indexOf(data);
		if (index < 0) {
			return;
		}

		remove(index);
	}

	public void remove(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IllegalArgumentException("update index out of range");
		}
		mList.remove(index);
		notifyItemRemoved(index);
	}

	public void append(Data data) {
		int start = mList.size();
		mList.add(data);
		notifyItemInserted(start);
	}

	public void append(@NonNull List<? extends Data> list) {
		int start = mList.size();
		mList.addAll(list);
		notifyItemRangeInserted(start, list.size());
	}

	public void insert(int index, Data data) {
		if (index < 0 || index >= mList.size()) {
			throw new IllegalArgumentException("insert index is out of range");
		}

		mList.add(index, data);
		notifyItemInserted(index);
	}

	public void insert(int index, @NonNull List<? extends Data> list) {
		if (index < 0 || index >= mList.size()) {
			throw new IllegalArgumentException("insert index is out of range");
		}

		int start = index;
		for (Data data : list) {
			mList.add(index++, data);
		}
		notifyItemRangeInserted(start, list.size());
	}

	static class Vh extends RecyclerView.ViewHolder {

		private final Renderer<?, ?> mRenderer;

		public Vh(@NonNull Renderer<?, ?> renderer) {
			super(renderer.getRoot());
			this.mRenderer = renderer;
		}
	}

	private static class Factory {
		private final Constructor<?> mRendererCtor;
		private final Method mViewBindingInflate;

		public Factory(Constructor<?> rendererCtor, Method viewBindingInflate) {
			mRendererCtor = rendererCtor;
			mViewBindingInflate = viewBindingInflate;
		}

		public Renderer<?, ?> create(LayoutInflater layoutInflater, ViewGroup parent) {
			try {
				Object viewBinding = mViewBindingInflate.invoke(null, layoutInflater, parent, false);
				return (Renderer<?, ?>) mRendererCtor.newInstance(viewBinding);
			} catch (Throwable throwable) {
				throw new RuntimeException("create renderer failed", throwable);
			}
		}
	}
}
