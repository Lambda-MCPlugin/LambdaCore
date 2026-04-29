package lambda.core.common.gui

import lambda.core.api.gui.LambdaGui
import lambda.core.api.gui.LambdaGuiBuilder

object LambdaGuiFactory {

    fun create(): LambdaGuiBuilder {
        return Builder()
    }

    private class Builder : LambdaGuiBuilder {

        private var title: String = "Menu"

        override fun title(title: String): LambdaGuiBuilder {
            this.title = title
            return this
        }

        override fun rows(rows: Int): LambdaGui {
            require(rows in 1..6) {
                "GUI rows must be between 1 and 6"
            }

            return SimpleLambdaGui(
                title = title,
                rows = rows
            )
        }

        override fun size(size: Int): LambdaGui {
            require(size in 9..54 && size % 9 == 0) {
                "GUI size must be multiple of 9 and between 9 and 54"
            }

            return SimpleLambdaGui(
                title = title,
                rows = size / 9
            )
        }
    }
}