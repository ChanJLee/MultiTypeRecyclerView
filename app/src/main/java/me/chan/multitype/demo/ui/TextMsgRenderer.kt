package me.chan.multitype.demo.ui

import me.chan.mtrv.Data
import me.chan.mtrv.Data.BindRenderer
import me.chan.mtrv.Renderer
import me.chan.multitype.demo.databinding.ItemTextBinding

class TextMsgRenderer(binding: ItemTextBinding): Renderer<ItemTextBinding, TextMsgRenderer.TextMsg>(binding) {

    override fun onRender(data: TextMsg) {
        mBinding.msg.text = data.text
    }

    // tell multi type system how to create View
    @BindRenderer(TextMsgRenderer::class)
    class TextMsg(
        val text: CharSequence
    ): Data()
}