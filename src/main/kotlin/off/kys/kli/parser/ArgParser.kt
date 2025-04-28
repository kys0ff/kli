@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.parser

class ArgParser(
    private val arguments: Map<String, String>,
    private val flags: Set<String>,
    private val params: List<String>,
) {
    companion object {
        fun from(args: Array<String>): ArgParser {
            val arguments = mutableMapOf<String, String>()
            val flags = mutableSetOf<String>()
            val params = mutableListOf<String>()

            var i = 0
            while (i < args.size) {
                when {
                    args[i].startsWith("--") -> {
                        val key = args[i].substring(2)
                        if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                            arguments[key] = args[i + 1]
                            i++
                        } else flags.add(key)
                    }

                    args[i].startsWith("-") -> {
                        val key = args[i].substring(1)
                        if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                            arguments[key] = args[i + 1]
                            i++
                        } else flags.add(key)
                    }

                    else -> params.add(args[i])
                }
                i++
            }

            return ArgParser(arguments, flags, params)
        }
    }

    fun getFlag(flag: String): Boolean = flags.contains(flag)
    fun getArgument(arg: String): String? = arguments[arg]
    operator fun get(arg: String): String? = getArgument(arg)
    fun getParams(): List<String> = params
}