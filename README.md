# MultiTypeRecyclerView
android multi type recycler view

# HOW TO USE

1. enable view binding
```groovy

android {
    viewBinding {
        enabled = true
    }
}
```

2. define your item
in my case, I define a text item

item_text.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/msg"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#000000"
        tools:text="hello world" />
</LinearLayout>
```

renderer type
```kotlin

class TextMsgRenderer(binding: ItemTextBinding): Renderer<ItemTextBinding, TextMsgRenderer.TextMsg>(binding) {

    override fun onRender(data: TextMsg) {
        mBinding.msg.text = data.text
    }

    // tell multi type system how to create View
    @BindRenderer(renderer = TextMsgRenderer::class)
    class TextMsg(
        val text: CharSequence
    ): Data()
}
```

3. use

```kotlin
MultiTypeAdapter adapter = new MultiTypeAdapter(this)

for (int i = 0; i < 10; ++i) {
    adapter.append(new TextMsgRenderer.TextMsg("test text: " + i))
}

binding.rv.setLayoutManager(new LinearLayoutManager(this))
binding.rv.setAdapter(adapter);
```