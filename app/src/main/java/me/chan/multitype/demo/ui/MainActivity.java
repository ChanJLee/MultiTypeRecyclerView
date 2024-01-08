package me.chan.multitype.demo.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.chan.multitype.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());
	}
}