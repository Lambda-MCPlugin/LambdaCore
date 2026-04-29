package lambda.core.api.gui

interface LambdaGuiBuilder {

    fun title(title: String): LambdaGuiBuilder

    fun rows(rows: Int): LambdaGui

    fun size(size: Int): LambdaGui
}