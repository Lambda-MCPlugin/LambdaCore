package lambda.core.common.nms

import lambda.core.api.nms.NmsAdapter
import org.bukkit.Bukkit

object NmsProvider {

    var adapter: NmsAdapter? = null
        private set

    fun init() {
        val minecraftVersion = Bukkit.getMinecraftVersion()

        val className = when {
            minecraftVersion.startsWith("1.21") -> {
                "lambda.core.nms.v1_21_R1.NmsAdapterImpl"
            }

            else -> {
                Bukkit.getLogger().warning(
                    "[LambdaCore] 지원하지 않는 Minecraft 버전입니다: $minecraftVersion"
                )
                return
            }
        }

        adapter = create(className)

        Bukkit.getLogger().info(
            "[LambdaCore] NMS Adapter loaded: $className"
        )
    }

    private fun create(className: String): NmsAdapter {
        val clazz = Class.forName(className)
        return clazz.getDeclaredConstructor().newInstance() as NmsAdapter
    }
}