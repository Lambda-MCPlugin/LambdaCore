package lambda.core.common.gui

import lambda.core.api.gui.LambdaGui
import lambda.core.api.gui.LambdaGuiBuilder

object LambdaGuiProvider {

    fun create(): LambdaGuiBuilder {
        return LambdaGuiFactory.create()
    }

    fun title(title: String): LambdaGuiBuilder {
        return create().title(title)
    }

    fun rows(rows: Int): LambdaGui {
        return create().rows(rows)
    }

    fun size(size: Int): LambdaGui {
        return create().size(size)
    }
}