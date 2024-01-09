package me.chan.multitype.demo.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import me.chan.mtrv.MultiTypeAdapter;
import me.chan.multitype.demo.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		MultiTypeAdapter adapter = new MultiTypeAdapter(this);

		for (int i = 0; i < 10; ++i) {
			adapter.append(new TextMsgRenderer.TextMsg("test text: " + i));
		}

		binding.rv.setLayoutManager(new LinearLayoutManager(this));
		binding.rv.setAdapter(adapter);
	}
}