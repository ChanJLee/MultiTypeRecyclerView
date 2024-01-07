package me.chan.mtrv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter<MultiTypeAdapter.Vh> {

	private LayoutInflater mLayoutInflater;
	private List<Data> mList = new ArrayList<>();

	public MultiTypeAdapter(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// todo
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull Vh holder, int position) {
		Data data = get(position);
		holder.renderer.bind(data);
		holder.renderer.render(data);
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	@Override
	public void onViewAttachedToWindow(@NonNull Vh holder) {
		super.onViewAttachedToWindow(holder);
		holder.renderer.attach();
	}

	@Override
	public void onViewDetachedFromWindow(@NonNull Vh holder) {
		holder.renderer.detach();
		super.onViewDetachedFromWindow(holder);
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

	public void update(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IllegalArgumentException("update index out of range");
		}

		notifyItemChanged(index);
	}

	public void remove(@NonNull Data data) {
		int index = indexOf(data);
		if (index < 0) {
			return;
		}

		remove(index);
	}

	public void append(Data data) {
		int start = mList.size();
		mList.add(data);
		notifyItemInserted(start);
	}

	public void append(@NonNull List<? extends Data> list) {
		int start = mList.size();
		for (Data data : list) {
			mList.add(data);
		}
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


	public void remove(int index) {
		if (index < 0 || index >= getItemCount()) {
			throw new IllegalArgumentException("update index out of range");
		}
	}

	public static class Vh extends RecyclerView.ViewHolder {

		private final Renderer<?, ?> renderer;

		public Vh(@NonNull Renderer<?, ?> renderer) {
			super(renderer.getRoot());
			this.renderer = renderer;
		}
	}
}
