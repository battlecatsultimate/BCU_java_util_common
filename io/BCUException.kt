package common.io

class BCUException(str: String?) : RuntimeException(str) {
    companion object {
        private const val serialVersionUID = 1L
    }
}
